package net.oopscraft.application.core.jpa;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import javax.cache.spi.CachingProvider;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.cfg.spi.DomainDataRegionBuildingContext;
import org.hibernate.cache.cfg.spi.DomainDataRegionConfig;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.jcache.ConfigSettings;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.hibernate.cache.jcache.internal.JCacheAccessImpl;
import org.hibernate.cache.jcache.internal.JCacheDomainDataRegionImpl;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.DomainDataRegion;
import org.hibernate.cache.spi.SecondLevelCacheLogger;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.cache.spi.support.RegionFactoryTemplate;
import org.hibernate.cache.spi.support.RegionNameQualifier;
import org.hibernate.cache.spi.support.StorageAccess;
import org.hibernate.engine.spi.SessionFactoryImplementor;

public class JCacheRegionFactory extends RegionFactoryTemplate {

	private static final long serialVersionUID = 8309671751417009722L;
	private final CacheKeysFactory cacheKeysFactory;
	private volatile CacheManager cacheManager;
	private volatile MissingCacheStrategy missingCacheStrategy;
	
	public static long durationSecond = 3;

	public JCacheRegionFactory() {
		this( DefaultCacheKeysFactory.INSTANCE );
	}

	public JCacheRegionFactory(CacheKeysFactory cacheKeysFactory) {
		this.cacheKeysFactory = cacheKeysFactory;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}
	
	/**
	 * Custom default MutableConfiguration
	 * 캐시 객체의 기본 설정정보를 반환한다.
	 * 기본 Touched정책(create,update,access)로 부터 지정된 시간이 지날 경우 clear
	 * 기본 시간은 3초이다.(기본 설정은 성능 향상을 위한 것이 아니라 과도한 엑서스를 방지하기 위한 목적임. 성능향상을 위한 캐시설정은 spring 에서 처리할것.)
	 * PS. 해당 customized 메서드만 추가되었고 나머지 소스는  org.hibernate.cache.jcache.internal.JCacheRegionFactory에서 가저옴.(상속 및 엑세스 불가)
	 * @return
	 */
	private MutableConfiguration<Object, Object> getDefaultConfiguration() {
		return new MutableConfiguration<>()
				.setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, durationSecond)));
	}
	

	@Override
	protected CacheKeysFactory getImplicitCacheKeysFactory() {
		return cacheKeysFactory;
	}

	@Override
	public DomainDataRegion buildDomainDataRegion(
			DomainDataRegionConfig regionConfig, DomainDataRegionBuildingContext buildingContext) {
		return new JCacheDomainDataRegionImpl(
				regionConfig,
				this,
				createDomainDataStorageAccess( regionConfig, buildingContext ),
				cacheKeysFactory,
				buildingContext
		);
	}

	@Override
	protected DomainDataStorageAccess createDomainDataStorageAccess(
			DomainDataRegionConfig regionConfig,
			DomainDataRegionBuildingContext buildingContext) {
		return new JCacheAccessImpl(
				getOrCreateCache( regionConfig.getRegionName(), buildingContext.getSessionFactory() )
		);
	}

	protected Cache<Object, Object> getOrCreateCache(String unqualifiedRegionName, SessionFactoryImplementor sessionFactory) {
		verifyStarted();
		assert !RegionNameQualifier.INSTANCE.isQualified( unqualifiedRegionName, sessionFactory.getSessionFactoryOptions() );

		final String qualifiedRegionName = RegionNameQualifier.INSTANCE.qualify(
				unqualifiedRegionName,
				sessionFactory.getSessionFactoryOptions()
		);

		final Cache<Object, Object> cache = cacheManager.getCache( qualifiedRegionName );
		if ( cache == null ) {
			return createCache( qualifiedRegionName );
		}
		return cache;
	}

	protected Cache<Object, Object> createCache(String regionName) {
		switch ( missingCacheStrategy ) {
			case CREATE_WARN:
				SecondLevelCacheLogger.INSTANCE.missingCacheCreated(
						regionName,
						ConfigSettings.MISSING_CACHE_STRATEGY, MissingCacheStrategy.CREATE.getExternalRepresentation()
				);
				return cacheManager.createCache( regionName, getDefaultConfiguration() );
			case CREATE:
				return cacheManager.createCache( regionName, getDefaultConfiguration() );
			case FAIL:
				throw new CacheException( "On-the-fly creation of JCache Cache objects is not supported [" + regionName + "]" );
			default:
				throw new IllegalStateException( "Unsupported missing cache strategy: " + missingCacheStrategy );
		}
	}

	protected boolean cacheExists(String unqualifiedRegionName, SessionFactoryImplementor sessionFactory) {
		final String qualifiedRegionName = RegionNameQualifier.INSTANCE.qualify(
				unqualifiedRegionName,
				sessionFactory.getSessionFactoryOptions()
		);
		return cacheManager.getCache( qualifiedRegionName ) != null;
	}

	@Override
	protected StorageAccess createQueryResultsRegionStorageAccess(
			String regionName,
			SessionFactoryImplementor sessionFactory) {
		String defaultedRegionName = defaultRegionName(
				regionName,
				sessionFactory,
				DEFAULT_QUERY_RESULTS_REGION_UNQUALIFIED_NAME,
				LEGACY_QUERY_RESULTS_REGION_UNQUALIFIED_NAMES
		);
		return new JCacheAccessImpl(
				getOrCreateCache( defaultedRegionName, sessionFactory )
		);
	}

	@Override
	protected StorageAccess createTimestampsRegionStorageAccess(
			String regionName,
			SessionFactoryImplementor sessionFactory) {
		String defaultedRegionName = defaultRegionName(
				regionName,
				sessionFactory,
				DEFAULT_UPDATE_TIMESTAMPS_REGION_UNQUALIFIED_NAME,
				LEGACY_UPDATE_TIMESTAMPS_REGION_UNQUALIFIED_NAMES
		);
		return new JCacheAccessImpl(
				getOrCreateCache( defaultedRegionName, sessionFactory )
		);
	}

	protected final String defaultRegionName(String regionName, SessionFactoryImplementor sessionFactory,
			String defaultRegionName, List<String> legacyDefaultRegionNames) {
		if ( defaultRegionName.equals( regionName )
				&& !cacheExists( regionName, sessionFactory ) ) {
			// Maybe the user configured caches explicitly with legacy names; try them and use the first that exists

			for ( String legacyDefaultRegionName : legacyDefaultRegionNames ) {
				if ( cacheExists( legacyDefaultRegionName, sessionFactory ) ) {
					SecondLevelCacheLogger.INSTANCE.usingLegacyCacheName( defaultRegionName, legacyDefaultRegionName );
					return legacyDefaultRegionName;
				}
			}
		}

		return regionName;
	}

	@Override
	protected boolean isStarted() {
		return super.isStarted() && cacheManager != null;
	}

	@Override
	protected void prepareForUse(SessionFactoryOptions settings, @SuppressWarnings("rawtypes") Map configValues) {
		this.cacheManager = resolveCacheManager( settings, configValues );
		if ( this.cacheManager == null ) {
			throw new CacheException( "Could not locate/create CacheManager" );
		}
		this.missingCacheStrategy = MissingCacheStrategy.interpretSetting(
				getProp( configValues, ConfigSettings.MISSING_CACHE_STRATEGY )
		);
	}

	protected CacheManager resolveCacheManager(SessionFactoryOptions settings, @SuppressWarnings("rawtypes") Map properties) {
		final Object explicitCacheManager = properties.get( ConfigSettings.CACHE_MANAGER );
		if ( explicitCacheManager != null ) {
			return useExplicitCacheManager( settings, explicitCacheManager );
		}

		final CachingProvider cachingProvider = getCachingProvider( properties );
		final CacheManager cacheManager;
		@SuppressWarnings("unchecked")
		final URI cacheManagerUri = getUri( settings, properties );
		if ( cacheManagerUri != null ) {
			cacheManager = cachingProvider.getCacheManager( cacheManagerUri, getClassLoader( cachingProvider ) );
		}
		else {
			cacheManager = cachingProvider.getCacheManager( cachingProvider.getDefaultURI(), getClassLoader( cachingProvider ) );
		}
		return cacheManager;
	}

	protected ClassLoader getClassLoader(CachingProvider cachingProvider) {
		// todo (5.3) : shouldn't this use Hibernate's AggregatedClassLoader?
		return cachingProvider.getDefaultClassLoader();
	}

	protected URI getUri(SessionFactoryOptions settings, Map<String,String> properties) {
		String cacheManagerUri = getProp( properties, ConfigSettings.CONFIG_URI );
		if ( cacheManagerUri == null ) {
			return null;
		}

		URL url = settings.getServiceRegistry()
				.getService( ClassLoaderService.class )
				.locateResource( cacheManagerUri );

		if ( url == null ) {
			throw new CacheException( "Couldn't load URI from " + cacheManagerUri );
		}

		try {
			return url.toURI();
		}
		catch (URISyntaxException e) {
			throw new CacheException( "Couldn't load URI from " + cacheManagerUri, e );
		}
	}

	private String getProp(@SuppressWarnings("rawtypes") Map properties, String prop) {
		return properties != null ? (String) properties.get( prop ) : null;
	}

	protected CachingProvider getCachingProvider(@SuppressWarnings("rawtypes") final Map properties){
		final CachingProvider cachingProvider;
		final String provider = getProp( properties, ConfigSettings.PROVIDER );
		if ( provider != null ) {
			cachingProvider = Caching.getCachingProvider( provider );
		}
		else {
			cachingProvider = Caching.getCachingProvider();
		}
		return cachingProvider;
	}

	@SuppressWarnings("unchecked")
	private CacheManager useExplicitCacheManager(SessionFactoryOptions settings, Object setting) {
		if ( setting instanceof CacheManager ) {
			return (CacheManager) setting;
		}

		final Class<? extends CacheManager> cacheManagerClass;
		if ( setting instanceof Class ) {
			cacheManagerClass = (Class<? extends CacheManager>) setting;
		}
		else {
			cacheManagerClass = settings.getServiceRegistry().getService( ClassLoaderService.class )
					.classForName( setting.toString() );
		}

		try {
			return cacheManagerClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new CacheException( "Could not use explicit CacheManager : " + setting );
		}
	}

	@Override
	protected void releaseFromUse() {
		try {
			// todo (5.3) : if this is a manager instance that was provided to us we should probably not close it...
			//		- when the explicit `setting` passed to `#useExplicitCacheManager` is
			//		a CacheManager instance
			cacheManager.close();
		}
		finally {
			cacheManager = null;
		}
	}
}

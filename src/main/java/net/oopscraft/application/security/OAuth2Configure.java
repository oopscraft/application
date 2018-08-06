package net.oopscraft.application.security;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class OAuth2Configure {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

	    // Identifies this resource server. Usefull if the AuthorisationServer authorises multiple Resource servers
	    private static final String RESOURCE_ID = "*****";

	    //@Resource(name = "OAuth")
	    //@Autowired
	    //DataSource dataSource;

	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	        // @formatter:off
	        http    
	                .authorizeRequests().anyRequest().authenticated();
	        // @formatter:on
	    }

	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	        resources.resourceId(RESOURCE_ID);
	        resources.tokenStore(tokenStore());
	    }

	    @Bean
	    public TokenStore tokenStore() {
//	        return new JdbcTokenStore(dataSource);
	    	return new InMemoryTokenStore();
	    }
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	    //@Resource(name = "OAuth")
	    //@Autowired
	    //DataSource dataSource;

	    @Bean
	    public TokenStore tokenStore() {
	        //return new JdbcTokenStore(dataSource);
	    	return new InMemoryTokenStore();
	    }

	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints.tokenStore(tokenStore());
	    }

	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//	        clients.jdbc(dataSource);
	        clients.inMemory()
            .withClient("client")
                .authorizedGrantTypes("password")
                .secret("{noop}secret")
                .scopes("all");

	    }
	}

}

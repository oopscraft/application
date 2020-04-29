package net.oopscraft.application.core.spring;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.oopscraft.application.core.PasswordBasedEncryptor;

public class EncryptedPropertySourceFactory implements org.springframework.core.io.support.PropertySourceFactory {

	@Override
	public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			PasswordBasedEncryptor pbEncryptor = new PasswordBasedEncryptor();
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			for(String propertyName : properties.stringPropertyNames()) {
				String value = properties.getProperty(propertyName);
				map.put(propertyName, pbEncryptor.decryptIdentifiedValue(value));
			}
			return new MapPropertySource("application",map);
		}catch(Exception e) {
			throw new IOException(e);
		}
	}

}

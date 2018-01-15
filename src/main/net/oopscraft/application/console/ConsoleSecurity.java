/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.PbeStringEncryptor;

/**
 * @author chomookun@gmail.com
 *
 */
public class ConsoleSecurity {
	
	private static final Log LOG = LogFactory.getLog(ConsoleSecurity.class);
	private static final File ADMIN_RPOPERTIES = new File("conf/admin.properties");
	private static Properties adminProperties;
	
	public ConsoleSecurity() throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream(ADMIN_RPOPERTIES);
			adminProperties = new Properties();
			adminProperties.load(is);
		}catch(Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}finally {
			if(is != null) {
				try { is.close(); }catch(Exception ignore) {}
			}
		}
	}

	/**
	 * isValidUser
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static boolean isValidUsername(String username) throws Exception {
		if(adminProperties.containsKey(username)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * isValidPassword
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean isValidPassword(String username, String password) throws Exception {
		// checks valid user.
		if(!isValidUsername(username)) {
			return false;
		}
		// checks password is same.
		if(password.equals(PbeStringEncryptor.encryptIdentifiedValue(adminProperties.getProperty(username)))) {
			return true;
		}else {
			return false;
		}
		
	}

}

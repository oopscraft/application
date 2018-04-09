/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.PasswordBasedEncryptor;

/**
 * @author chomookun@gmail.com
 *
 */
public class AdminSecurity {
	
	private static final Log LOG = LogFactory.getLog(AdminSecurity.class);
	private static final File ADMIN_RPOPERTIES = new File("conf/admin.properties");
	private static Properties adminProperties;
	
	public AdminSecurity() throws Exception {
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
	 * isValidUsername
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public boolean isValidAdmin(String admin) throws Exception {
		if(adminProperties.containsKey(admin)) {
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
	public boolean isValidPassword(String admin, String password) throws Exception {
		// checks valid user.
		if(!isValidAdmin(admin)) {
			return false;
		}
		// checks password is same.
		if(password.equals(PasswordBasedEncryptor.decryptIdentifiedValue(adminProperties.getProperty(admin)))) {
			return true;
		}else {
			return false;
		}
	}

}

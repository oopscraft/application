package net.oopscraft.application.core;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class PbeStringEncryptor {
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String PASSWORD_ENV_NAME = "APP_ENCRYPTION_PASSWORD";
    private static final String DEFAULT_PASSWORD = "jasyptPass";
    private static final String ENCRYPT_IDENTIFIER = "(ENC\\()([^)]{1,})(\\))"; 
    private static EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
    private static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    static {
        config.setAlgorithm(ALGORITHM);
        config.setPasswordEnvName(PASSWORD_ENV_NAME);
        encryptor.setConfig(config);
        encryptor.setPassword(DEFAULT_PASSWORD);
    }
    
    /**
     * encrypt
     * @param decryptedString
     * @return
     * @throws Exception
     */
    public static String encrypt(String decryptedString) throws Exception {
        return encryptor.encrypt(decryptedString);
    }
    
    /**
     * decrypt
     * @param encryptedString
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedString) throws Exception {
        return encryptor.decrypt(encryptedString);
    }
    
    /**
     * encryptIdentifiedValue
     * @param decryptedString
     * @return
     * @throws Exception
     */
    public static String encryptIdentifiedValue(String decryptedString) throws Exception {
        Pattern p = Pattern.compile(ENCRYPT_IDENTIFIER);
        Matcher m = p.matcher(decryptedString);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String decryptedValue = m.group(2);
            String encryptedValue = encrypt(decryptedValue);
            m.appendReplacement(sb, m.group(1) + Matcher.quoteReplacement(encryptedValue) + m.group(3));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * decryptIdentifiedValue
     * @param encryptedString
     * @return
     * @throws Exception
     */
    public static String decryptIdentifiedValue(String encryptedString) throws Exception {
        Pattern p = Pattern.compile(ENCRYPT_IDENTIFIER);
        Matcher m = p.matcher(encryptedString);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String encryptedValue = m.group(2);
            String decryptedValue = decrypt(encryptedValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(decryptedValue));
        }
        m.appendTail(sb);
        return sb.toString();
    }
      /** 
       * Main method
       * @param args
       * @throws Exception
       */
    public static void main(String[] args) throws Exception {
        System.out.print("Please enter 1.Encryption or 2.Decryption:");	// NOPMD
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String type = scanner.nextLine();
        if("1".equals(type)) {
            System.out.print("enter original value:");	// NOPMD
            String value = scanner.nextLine();
            String encrypted = encryptIdentifiedValue(value);
            System.out.println(" encrypted:" + encrypted);	// NOPMD
            String decrypted = decryptIdentifiedValue(encrypted);
            System.out.println(" decrypted:" +  decrypted);	// NOPMD
        }
        else if("2".equals(type)) {
            System.out.print("enter encrypted value:");	// NOPMD
            String value = scanner.nextLine();
            String decrypted = decryptIdentifiedValue(value);
            System.out.println(" decrypted:" + decrypted);	// NOPMD
        }
    }
}
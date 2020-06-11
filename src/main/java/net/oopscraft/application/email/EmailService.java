package net.oopscraft.application.email;



import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.property.PropertyService;

@Service
public class EmailService {
	
	private static final String APP_EMAL_SMTP = "APP_EMAL_SMTP";
	
	@Autowired
	PropertyService propertyService;
	
	public void sendEmail(Email email) throws Exception {

		// creates email sender
		Properties properties = JsonConverter.toObject(propertyService.getProperty(APP_EMAL_SMTP).getValue(), Properties.class);
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(properties.getProperty("host"));
		javaMailSender.setPort(Integer.parseInt(properties.getProperty("port")));
		javaMailSender.setUsername(properties.getProperty("username"));
		javaMailSender.setPassword(properties.getProperty("password"));
		javaMailSender.setDefaultEncoding("UTF-8");
		javaMailSender.setJavaMailProperties(properties);

		// creates message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setContent(email.getContent(), "text/html;charset=utf-8");
		mimeMessage.setRecipient(RecipientType.TO , new InternetAddress(email.getReceiver()));
		javaMailSender.send(mimeMessage);
	}
	

}

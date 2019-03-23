package net.oopscraft.application;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.mapper.ArticleMapper;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.core.webserver.WebServerContext;
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageService;

public class Application {
	
	public static AnnotationConfigApplicationContext context = null;
	public static WebServer webServer = null;
	
	public static void main(String[] args) throws Exception {
		
		context = new AnnotationConfigApplicationContext(ApplicationContext.class);
		
		for(String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				try {
					context.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
				try {
					webServer.stop();
				}catch(Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}));
		
		MessageService messageService = context.getBean(MessageService.class);
		Message message = messageService.getMessage("fdafdsa");
		System.out.println(message);
		
		ArticleMapper articleMapper = context.getBean(ArticleMapper.class);
		List<Article> articles = articleMapper.selectLatestArticles("D6C484E2F9304E94A9454AF5C8879022", new RowBounds(0,10));
		System.out.println(new TextTable(articles));
		
		// creates web server
		WebServer webServer = new WebServer();
		webServer.setPort(10001);
		WebServerContext webServerContext = new WebServerContext();
		webServerContext.setContextPath("");
		webServerContext.setResourceBase("webapp");
		webServer.addContext(webServerContext);
		webServer.start();
		
		// wait
		Thread.currentThread().join();
	}

}

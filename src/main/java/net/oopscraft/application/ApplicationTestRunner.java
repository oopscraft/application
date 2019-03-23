package net.oopscraft.application;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	classes= ApplicationContext.class, 
	loader=AnnotationConfigContextLoader.class
)
@Transactional
@Rollback
@WebAppConfiguration(value="file:conf")
public class ApplicationTestRunner {
	
	@Autowired
	private WebApplicationContext webApplicationContext;

}

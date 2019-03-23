package net.oopscraft.application;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	classes= ApplicationContext.class, 
	loader=AnnotationConfigContextLoader.class
)
@Transactional
@Rollback
public class ApplicationTestRunner {

}

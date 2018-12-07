package net.oopscraft.application.test;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:conf/spring.xml","file:conf/dispatcher-servlet.xml"})
@WebAppConfiguration(value="file:conf")
@Transactional(transactionManager = "jpaTransactionManager")
@Rollback
public class ApplicationSpringTestRunner {

}

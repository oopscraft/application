package net.oopscraft.application;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import net.oopscraft.application.ApplicationJUnit4ClassRunner;

@RunWith(ApplicationJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:webapp/application/WEB-INF/conf/spring.xml")
@WebAppConfiguration
@Transactional
public class ApplicationTest {

}

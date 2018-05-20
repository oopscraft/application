package net.oopscraft.application;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import net.oopscraft.application.ApplicationJUnit4ClassRunner;

@RunWith(ApplicationJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:src/test/java/net/oopscraft/application/spring-test.xml")
@WebAppConfiguration
@Transactional
public class ApplicationTest {

}

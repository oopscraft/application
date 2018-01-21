package net.oopscraft.application;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(ApplicationJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:src/test/net/oopscraft/application/spring.xml")
@WebAppConfiguration
public class ApplicationTest {
	
//	@Before
//	public void before() {
//		try {
//			ApplicationContainer.launchForTest(Application.class, new String[] {});
//		}catch(Exception e){
//			e.printStackTrace(System.err);
//			assert(false);
//		}
//	}


}

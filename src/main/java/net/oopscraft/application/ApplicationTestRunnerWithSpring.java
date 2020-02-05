package net.oopscraft.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	classes=ApplicationContext.class, 
	loader=AnnotationConfigContextLoader.class
)
//@ContextConfiguration(locations = {"classpath:conf/spring.xml","classpath:conf/dispatcher-servlet.xml"})
//@WebAppConfiguration(value="file:conf")
//@Transactional
//@Rollback
public class ApplicationTestRunnerWithSpring {
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * Returns MockMvc instance.
	 * @return
	 */
	public final MockMvc getMockMvc() {
		return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	public final void performGet(String uri) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get(uri))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	public final void performPostJson(String uri, String payload) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		RequestBuilder requestBuilder = post(uri)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(payload);
		getMockMvc().perform(requestBuilder)
			.andDo(print())
			.andExpect(status().isOk());
	}

}

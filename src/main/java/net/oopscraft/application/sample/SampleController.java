package net.oopscraft.application.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.sample.SampleService;
import net.oopscraft.application.sample.entity.Sample;

@Controller
@RequestMapping("/sample")
public class SampleController {

	@Autowired
	HttpServletResponse response;

	@Autowired
	SampleService sampleService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("sample/sample.html");
		return modelAndView;
	}
	
	/**
	 * Returns samples
	 * @param role
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSamples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Sample> getSamples(@ModelAttribute Sample sample, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Sample> samples = sampleService.getSamples(sample, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return samples;
	}
	
	@RequestMapping(value = "getSampleSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Map<String,Object>> getSampleSummary(@RequestParam("key1") String key1, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Map<String,Object>> samples = sampleService.getSampleSummary(key1, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return samples;
	}
	
	/**
	 * Returns sample
	 * @param sample
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSample", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Sample getSample(@ModelAttribute Sample sample) throws Exception {
		return sampleService.getSample(sample);
	}

	/**
	 * Saves sample
	 * @param sample
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveSample", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Sample saveSample(@RequestBody Sample sample) throws Exception {
		return sampleService.saveSample(sample);
	}
	
	/**
	 * Deletes sample
	 * @param sample
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteSample", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteSample(@RequestBody Sample sample) throws Exception {
		sampleService.deleteSample(sample);
	}
	
}

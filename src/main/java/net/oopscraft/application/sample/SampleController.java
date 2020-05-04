package net.oopscraft.application.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import net.oopscraft.application.core.Pagination;

@Controller
@RequestMapping("/sample")
public class SampleController {
	
	@Autowired
	SampleService sampleService;
	
	/**
	 * Forwards page
	 * 샘플 화면 이동
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
	 * 샘플 데이터 목록 조회
	 * MYBATIS MAPPER로 Entity Class를 이용하여 조회한다.
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSamples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Sample> getSamples(@ModelAttribute Sample sample, @ModelAttribute Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Sample> samples = sampleService.getSamples(sample, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return samples;
	}
	
	/**
	 * Returns sample summary
	 * 샘플데이터 통계내역 조회
	 * MYBATIS MAPPER로 비정형 데이터구조를 Map형태로 조회하여 반환한다.
	 * @param key1
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSampleSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Map<String,Object>> getSampleSummary(
		 @RequestParam(value="key1", required=false) String key1
	) throws Exception {
		List<Map<String,Object>> samples = sampleService.getSampleSummary(key1);
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

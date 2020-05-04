package net.oopscraft.application.sample;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.core.mybatis.PageRowBounds;

@Service
public class SampleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);
	
	@Autowired
	SampleMapper sampleMapper;
	
	@Autowired
	SampleRepository sampleRepository;
	
	/**
	 * Returns samples
	 * @param sample
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Sample> getSamples(final Sample sample, Pagination pagination) throws Exception {
		PageRowBounds rowBounds = pagination.toRowBounds();
		List<Sample> samples = sampleMapper.getSamples(sample, rowBounds);
		LOGGER.debug("{}",new TextTable(samples));
		pagination.setTotalCount(rowBounds.getTotalCount());
		return samples;
	}
	
	/**
	 * getSampleSummary
	 * @param key1
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getSampleSummary(String key1) throws Exception {
		List<Map<String,Object>> samples = sampleMapper.getSampleSummary(key1);
		LOGGER.debug("{}",new TextTable(samples));
		return samples;
	}
	
	/**
	 * Returns sample
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Sample getSample(String key1, String key2) {
		Sample sample = sampleRepository.findOne(new Sample.Pk(key1,key2));
		LOGGER.debug("{}",new TextTable(sample));
		return sample;
	}
	
	/**
	 * Returns sample
	 * @param sample
	 * @return
	 * @throws Exception
	 */
	public Sample getSample(Sample sample) throws Exception {
		return getSample(sample.getKey1(), sample.getKey2());
	}
	
	/**
	 * Saves sample
	 * @param sample
	 * @return
	 * @throws Exception
	 */
	public Sample saveSample(Sample sample) throws Exception {
		LOGGER.debug("{}",new TextTable(sample));
		Sample one = sampleRepository.findOne(new Sample.Pk(sample.getKey1(), sample.getKey2()));
		if(one == null) {
			one = new Sample();
			one.setKey1(sample.getKey1());
			one.setKey2(sample.getKey2());
		}
		one.setValueChar(sample.getValueChar());
		one.setValueClob(sample.getValueClob());
		one.setValueInt(sample.getValueInt());
		one.setValueLong(sample.getValueLong());
		one.setValueYn(sample.isValueYn());
		one.setValueEnum(sample.getValueEnum());
		return sampleRepository.save(one);
	}
	
	/**
	 * Deletes sample
	 * @param sample
	 * @throws Exception
	 */
	public void deleteSample(Sample sample) throws Exception {
		LOGGER.debug("{}",new TextTable(sample));
		sampleRepository.delete(sample);
	}

}

package net.oopscraft.application.sample;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.mybatis.PageRowBounds;
import net.oopscraft.application.sample.entity.Sample;

@Service
public class SampleService {
	
	@Autowired
	SampleMapper sampleMapper;
	
	@Autowired
	SampleRepository sampleRepository;
	
	/**
	 * Returns samples
	 * @param sample
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Sample> getSamples(final Sample sample, PageInfo pageInfo) throws Exception {
		PageRowBounds rowBounds = pageInfo.toRowBounds();
		List<Sample> samples = sampleMapper.getSamples(sample, rowBounds);
		pageInfo.setTotalCount(rowBounds.getTotalCount());
		return samples;
	}
	
	/**
	 * getSampleSummary
	 * @param key1
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getSampleSummary(String key1,PageInfo pageInfo) throws Exception {
		PageRowBounds rowBounds = pageInfo.toRowBounds();
		List<Map<String,Object>> samples = sampleMapper.getSampleSummary(key1, rowBounds);
		pageInfo.setTotalCount(rowBounds.getTotalCount());
		return samples;
	}
	
	/**
	 * Returns sample
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Sample getSample(String key1, String key2) {
		return sampleRepository.findOne(new Sample.Pk(key1,key2));
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
		one.setValueBoolean(sample.isValueBoolean());
		one.setValueEnum(sample.getValueEnum());
		return sampleRepository.save(one);
	}
	
	/**
	 * Deletes sample
	 * @param sample
	 * @throws Exception
	 */
	public void deleteSample(Sample sample) throws Exception {
		sampleRepository.delete(sample);
	}

}

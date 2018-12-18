package net.oopscraft.application.property;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.property.repository.PropertyRepository;

@Service
public class PropertyService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(PropertyService.class);
	
	@Autowired
	PropertyRepository propertyRepository;
	

	/**
	 * Search condition class 
	 *
	 */
	public class SearchCondition {
		String id;
		String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	/**
	 * Gets properties
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Property> getProperties(SearchCondition searchCondition, PageInfo pageInfo ) throws Exception {
		List<Property> properties = null;
		Page<Property> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if(!StringUtils.isEmpty(searchCondition.getId())) {
			page = propertyRepository.findByIdStartingWith(searchCondition.getId(), pageable);
		}else if(!StringUtils.isEmpty(searchCondition.getId())) {
			page = propertyRepository.findByNameStartingWith(searchCondition.getName(), pageable);
		}else {
			page = propertyRepository.findAllByOrderBySystemDataYn(pageable);
		}
		properties = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		LOGGER.debug("+ properties: {}", new TextTable(properties));
		return page.getContent();
	}
	
	/**
	 * Gets detail of property
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Property getProperty(String id) throws Exception {
		Property Property = propertyRepository.findOne(id);
		return Property;
	}
	
	/**
	 * Saves property
	 * 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public Property saveProperty(Property property) throws Exception {
		Property one = propertyRepository.findOne(property.getId());
		if(one == null) {
			one = new Property();
			one.setId(property.getId());
		}
		one.setName(property.getName());
		one.setValue(property.getValue());
		one.setDescription(property.getDescription());
		propertyRepository.save(one);
		return propertyRepository.findOne(property.getId());
	}
	
	/**
	 * Removes property
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Property removeProperty(String id) throws Exception {
		Property property = propertyRepository.getOne(id);
		propertyRepository.delete(property);
		return property;
	}
}

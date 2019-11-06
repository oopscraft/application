package net.oopscraft.application.property;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;

@Service
public class PropertyService {
	
	@Autowired
	PropertyRepository propertyRepository;

	public enum PropertySearchType { ID,NAME	}
	
	/**
	 * Gets properties
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Property> getProperties(PageInfo pageInfo, PropertySearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Property> propertiesPage = null;
		if(searchType == null) {
			propertiesPage = propertyRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					propertiesPage = propertyRepository.findByIdContaining(searchValue, pageable);
				break;
				case NAME :
					propertiesPage = propertyRepository.findByNameContaining(searchValue, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(propertiesPage.getTotalElements());
		}
		List<Property> properties = propertiesPage.getContent();
		return properties;
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
	public void saveProperty(Property property) throws Exception {
		Property one = propertyRepository.findOne(property.getId());
		if(one == null) {
			one = new Property();
			one.setId(property.getId());
		}
		one.setName(property.getName());
		one.setValue(property.getValue());
		one.setDescription(property.getDescription());
		propertyRepository.save(one);
	}
	
	/**
	 * deletes property
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteProperty(String id) throws Exception {
		Property property = propertyRepository.getOne(id);
		propertyRepository.delete(property);
	}

}

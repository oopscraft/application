package net.oopscraft.application.property;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.property.entity.Property;

@Service
public class PropertyService {
	
	@Autowired
	PropertyRepository propertyRepository;

	/**
	 * Returns properties.
	 * @param property
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Property> getProperties(final Property property, Pagination pagination) throws Exception {
		Page<Property> propertiesPage = propertyRepository.findAll(new  Specification<Property>() {
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(property.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), property.getId() + '%'));
					predicates.add(predicate);
				}
				if(property.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), property.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(propertiesPage.getTotalElements());
		return propertiesPage.getContent();
	}
	
	/**
	 * Return property
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Property getProperty(String id) throws Exception {
		return propertyRepository.findOne(id);
	}
	
	/**
	 * Saves property 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public Property saveProperty(Property property) throws Exception {
		Property one = propertyRepository.findOne(property.getId());
		if(one == null) {
			one = new Property(property.getId());
		}
		one.setName(property.getName());
		one.setDescription(property.getDescription());
		one.setValue(property.getValue());
		return propertyRepository.save(one);
	}
	
	/**
	 * Deletes property
	 * @param property
	 * @throws Exception
	 */
	public void deleteProperty(Property property) throws Exception {
		propertyRepository.delete(property);
	}

}

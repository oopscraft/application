package net.oopscraft.application.property.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.property.entity.Property;

public interface PropertyRepository extends JpaRepository<Property,String>{

	public Page<Property> findAllByOrderBySystemDataYn(Pageable pageable) throws Exception;

	public Page<Property> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Property> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}
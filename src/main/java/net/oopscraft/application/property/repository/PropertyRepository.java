package net.oopscraft.application.property.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.property.Property;

public interface PropertyRepository extends JpaRepository<Property,String>{

	public Page<Property> findAllByOrderBySystemDataYn(Pageable pageable) throws Exception;

	public Page<Property> findByIdStartingWith(String id, Pageable pageable) throws Exception;
	
	public Page<Property> findByNameStartingWith(String name, Pageable pageable) throws Exception;
	
}

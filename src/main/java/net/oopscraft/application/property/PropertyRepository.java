package net.oopscraft.application.property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.property.entity.Property;

public interface PropertyRepository extends JpaRepository<Property,String>{

	public Page<Property> findAllBy(Pageable pageable) throws Exception;

	public Page<Property> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Property> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}

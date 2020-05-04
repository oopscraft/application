package net.oopscraft.application.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyRepository extends JpaRepository<Property,String>, JpaSpecificationExecutor<Property> {

}

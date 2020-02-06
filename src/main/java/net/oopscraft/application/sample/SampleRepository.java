package net.oopscraft.application.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.oopscraft.application.sample.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample,Sample.Pk>, JpaSpecificationExecutor<Sample> {

}

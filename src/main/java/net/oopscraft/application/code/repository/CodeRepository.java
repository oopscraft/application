package net.oopscraft.application.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.code.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code,String> {

}

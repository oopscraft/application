package net.oopscraft.application.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String>, JpaSpecificationExecutor<Authority> {

}

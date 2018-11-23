package net.oopscraft.application.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {


}

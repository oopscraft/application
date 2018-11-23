package net.oopscraft.application.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {


}

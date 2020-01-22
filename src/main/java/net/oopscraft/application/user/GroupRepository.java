package net.oopscraft.application.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,String>, JpaSpecificationExecutor<Group> {

}

package net.oopscraft.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User> {

}

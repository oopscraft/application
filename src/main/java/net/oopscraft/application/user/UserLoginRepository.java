package net.oopscraft.application.user;


import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.user.entity.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, UserLogin.Pk> {

}

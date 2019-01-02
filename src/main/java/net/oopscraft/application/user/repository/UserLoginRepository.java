package net.oopscraft.application.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.user.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, UserLogin.Pk> {

}

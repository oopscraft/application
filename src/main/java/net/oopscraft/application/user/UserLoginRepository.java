package net.oopscraft.application.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, UserLogin.Pk> {

}

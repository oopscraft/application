package net.oopscraft.application.user.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.user.entity.UserLoginHistory;

public interface UserLoginRepository extends JpaRepository<UserLoginHistory, UserLoginHistory.Pk> {

}

package net.oopscraft.application.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, String>,JpaSpecificationExecutor<Verification> {
		
}

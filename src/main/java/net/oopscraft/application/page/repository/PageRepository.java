package net.oopscraft.application.page.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.page.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {

}

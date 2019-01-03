package net.oopscraft.application.layout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.layout.Layout;

@Repository
public interface LayoutRepository extends JpaRepository<Layout,String> {

}

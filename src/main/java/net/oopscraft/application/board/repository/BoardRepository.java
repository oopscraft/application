package net.oopscraft.application.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board,String>{
	
	public Page<Board> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Board> findByNameContaining(String name, Pageable pageable) throws Exception;

}

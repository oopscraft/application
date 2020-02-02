package net.oopscraft.application.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board,String>, JpaSpecificationExecutor<Board>{

}
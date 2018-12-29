package net.oopscraft.application.board;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.board.repository.BoardRepository;

public class BoardServiceBuilder {

	EntityManager entityManager;
	
	public BoardServiceBuilder setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		return this;
	}
	
	public BoardService build() {
		BoardService boardService = new BoardService();
		boardService.entityManager = entityManager;
		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
		boardService.boardRepository = jpaRepositoryFactory.getRepository(BoardRepository.class);
		return boardService;
	}
}

package net.oopscraft.application.board;

import javax.persistence.EntityManager;

public class BoardServiceBuilder {

	EntityManager entityManager;
	
	public BoardServiceBuilder setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		return this;
	}
	
	public BoardService build() {
		BoardService boardService = new BoardService();
//		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
//		codeService.codeRepository = jpaRepositoryFactory.getRepository(CodeRepository.class);
		return boardService;
	}
}

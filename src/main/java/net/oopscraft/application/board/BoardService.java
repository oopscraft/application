package net.oopscraft.application.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.entity.ArticleFile;
import net.oopscraft.application.board.entity.ArticleReply;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.board.entity.BoardArticle;
import net.oopscraft.application.board.entity.BoardCategory;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;

@Service
public class BoardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
	
    @Value("${application.upload.path}")
    String uploadPath;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BoardArticleRepository boardArticleRepository;
	
	@Autowired
	ArticleReplyRepository articleReplyRepository;

	/**
	 * Gets list of board by search condition and value
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoards(final Board board, Pagination pagination) throws Exception {
		Page<Board> boardsPage = boardRepository.findAll(new Specification<Board>() {
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(board.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), board.getId() + '%'));
					predicates.add(predicate);
				}
				if(board.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), board.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(boardsPage.getTotalElements());
		return boardsPage.getContent();
	}

	/**
	 * Gets detail of board
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Board getBoard(String id) throws Exception {
		return boardRepository.findOne(id);
	}

	/**
	 * Saves board details
	 * @param board
	 * @throws Exception
	 */
	public Board saveBoard(Board board) throws Exception {
		Board one = boardRepository.findOne(board.getId());
		if(one == null) {
			one = new Board(board.getId());
		}
		one.setName(board.getName());
		one.setIcon(board.getIcon());
		one.setDescription(board.getDescription());
		one.setSkin(board.getSkin());
		one.setRows(board.getRows());
		one.setReplyUse(board.isReplyUse());
		one.setFileUse(board.isFileUse());
		one.setFileAllowCount(board.getFileAllowCount());
		one.setFileAllowSize(board.getFileAllowSize());

		// category
		one.setCategoryUse(board.isCategoryUse());
		one.getCategories().clear();
		for(int i = 0, size = board.getCategories().size(); i < size; i ++ ) {
			BoardCategory category = board.getCategories().get(i);
			category.setBoardId(board.getId());
			category.setDisplayNo(i+1);
			one.getCategories().add(category);
		}
		
		// access policy
		one.setAccessPolicy(board.getAccessPolicy());
		one.getAccessAuthorities().clear();
		one.getAccessAuthorities().addAll(board.getAccessAuthorities());
		
		// read policy
		one.setReadPolicy(board.getReadPolicy());
		one.getReadAuthorities().clear();
		one.getReadAuthorities().addAll(board.getReadAuthorities());
		
		// write policy
		one.setWritePolicy(board.getWritePolicy());
		one.getWriteAuthorities().clear();
		one.getWriteAuthorities().addAll(board.getWriteAuthorities());
		
		// returns
		return boardRepository.save(one);
	}

	/**
	 * Removes board details
	 * @param board
	 * @throws Exception
	 */
	public void deleteBoard(Board board) throws Exception {
		boardRepository.delete(board);
	}	
	
	
	/**
	 * Returns board articles
	 * @param code
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<BoardArticle> getBoardArticles(final BoardArticle boardArticle, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "notice"));
		orders.add(new Order(Direction.DESC, "registerDate"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
		Page<BoardArticle> boardArticlesPage = boardArticleRepository.findAll(new  Specification<BoardArticle>() {
			@Override
			public Predicate toPredicate(Root<BoardArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(!StringUtils.isBlank(boardArticle.getBoardId())) {
					predicates.add(cb.and(cb.equal(root.get("boardId").as(String.class), boardArticle.getBoardId())));
				}
				if(!StringUtils.isBlank(boardArticle.getCategoryId())) {
					predicates.add(cb.and(cb.equal(root.get("categoryId").as(String.class), boardArticle.getCategoryId())));
				}
				if(!StringUtils.isBlank(boardArticle.getTitle())) {
					predicates.add(cb.and(cb.like(root.get("title").as(String.class), '%'+boardArticle.getTitle()+'%')));
				}
				if(!StringUtils.isBlank(boardArticle.getContents())) {
					predicates.add(cb.and(cb.like(root.get("contents").as(String.class), '%'+boardArticle.getContents()+'%')));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageRequest);
		pagination.setTotalCount(boardArticlesPage.getTotalElements());
		return boardArticlesPage.getContent();
	}
	
	/**
	 * Gets board article
	 * @param boardId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BoardArticle getBoardArticle(String boardId, String id) throws Exception {
		return boardArticleRepository.findOne(id);
	}
	
	/**
	 * Returns board article
	 * @param boardArticle
	 * @return
	 * @throws Exception
	 */
	public BoardArticle saveBoardArticle(BoardArticle boardArticle) throws Exception {
		
		BoardArticle one = null;
		if(boardArticle.getId()==null 
		||(one = boardArticleRepository.findOne(boardArticle.getId())) == null) {
			one = new BoardArticle();
			one.setId(IdGenerator.uuid());
			one.setAuthorName(boardArticle.getAuthorName());
			one.setRegisterDate(new Date());
		}
		one.setBoardId(boardArticle.getBoardId());
		one.setCategoryId(boardArticle.getCategoryId());
		one.setTitle(boardArticle.getTitle());
		one.setContents(boardArticle.getContents());
		
		// add new file
		for (ArticleFile file : boardArticle.getFiles()) {
			if(one.getFile(file.getId()) == null) {
				file.setArticleId(boardArticle.getId());
				one.getFiles().add(file);
				File temporaryFile = new File(uploadPath + File.separator + file.getId());
				File realFile = new File(uploadPath + File.separator + "board" + File.separator + file.getId());
				try {
					FileUtils.moveFile(temporaryFile, realFile);
				} catch (Exception ignore) {
					LOGGER.warn(ignore.getMessage(), ignore);
				}
			}
		}

		// remove deleted file
		for (int index = one.getFiles().size() - 1; index >= 0; index--) {
			ArticleFile file = one.getFiles().get(index);
			if(boardArticle.getFile(file.getId()) == null) {
				one.removeFile(file.getId());
				File realFile = new File(uploadPath + File.separator + "board" + File.separator + file.getId());
				FileUtils.deleteQuietly(realFile);
			}
		}
		
		// saves
		return boardArticleRepository.save(one);
	}
	
	/**
	 * Deletes board article
	 * @param code
	 * @throws Exception
	 */
	public void deleteBoardArticle(BoardArticle boardArticle) throws Exception {
		BoardArticle one = boardArticleRepository.findOne(boardArticle.getId());
		for(ArticleFile articleFile : one.getFiles()) {
			File file = new File(uploadPath + File.separator + "board" + File.separator + articleFile.getId());
			FileUtils.deleteQuietly(file);
		}
		boardArticleRepository.delete(one);
	}
	
	/**
	 * Returns board article replies
	 * @param boardId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public List<ArticleReply> getBoardArticleReplies(final String articleId) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "articleId").nullsLast());
		List<ArticleReply> boardArticleReplies = articleReplyRepository.findAll(new Specification<ArticleReply>() {
			@Override
			public Predicate toPredicate(Root<ArticleReply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(cb.and(cb.equal(root.get("articleId").as(String.class), articleId)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		});
		return boardArticleReplies;
	}

	/**
	 * Saves board article reply
	 * @param articleReply
	 * @return
	 * @throws Exception
	 */
	public ArticleReply saveBoardArticleReply(ArticleReply articleReply) throws Exception {
		ArticleReply.Pk pk = new ArticleReply.Pk(articleReply.getArticleId(), articleReply.getId());
		ArticleReply one = articleReplyRepository.findOne(pk);
		if(one == null) {
			one = new ArticleReply();
			one.setArticleId(articleReply.getArticleId());
			one.setId(IdGenerator.uuid());
		}
		one.setUpperId(articleReply.getUpperId());
		one.setContents(articleReply.getContents());
		return articleReplyRepository.save(one);
	}
	
	/**
	 * Deletes board article reply
	 * @param boardId
	 * @param articleId
	 * @param id
	 * @throws Exception
	 */
	public void deleteBoardArticleReply(String boardId, String articleId, String id) throws Exception {
		ArticleReply.Pk pk = new ArticleReply.Pk(articleId, id);
		articleReplyRepository.delete(pk);
	}

}

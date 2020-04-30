package org.edu.test;
import java.util.List;

import javax.inject.Inject;

import org.edu.dao.IF_BoardDAO;
import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })
@WebAppConfiguration
public class BoardDAOTest {

	@Inject //자바규격 또는 @Autowired //스프링 전용
	private IF_BoardDAO dao;

	private static Logger logger = LoggerFactory.getLogger(BoardDAOTest.class);

	@Test
	public void testCreate() throws Exception {
		BoardVO board = new BoardVO();
		board.setTitle("새로운 글을 넣습니다. ");
		board.setContent("새로운 글을 넣습니다. ");
		board.setWriter("user00");
		dao.create(board);
	}

	@Test
	public void testRead() throws Exception {
		logger.info(dao.read(1).toString());
	}

	@Test
	public void testUpdate() throws Exception {
		BoardVO board = new BoardVO();
		board.setBno(1);
		board.setTitle("수정된 글입니다.");
		board.setContent("수정 테스트 ");
		dao.update(board);
	}

	@Test
	public void testDelete() throws Exception {
		dao.delete(2);
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testListAll() throws Exception {
		int page = 3;
		//List<BoardVO> list = dao.listAll(page); //Criteria 클래스 지정으로 getPageStart()대체
		//Criteria cri = new Criteria();//검색기능 추가로 주석처리
		SearchCriteria cri = new SearchCriteria();
		cri.setPage(page);
		cri.setPerPageNum(10);
		List<BoardVO> list = dao.listAll(cri);
		for(BoardVO boardVO : list) {
			logger.info(boardVO.getBno() + ":" + boardVO.getTitle());
		}
	}
	
	@Test
	public void testURI() throws Exception {
		UriComponents uricomponents =
				UriComponentsBuilder.newInstance()
				.path("/admin/{module}/{page}")
				.queryParam("page", 1)
				.queryParam("perPageNum", 20)
				.build()
				.expand("board","listAll")
				.encode();
		logger.info("/admin/board/listAll?page=1&perPageNum=20");
		logger.info(uricomponents.toString());
	}
	
	//...326p.log4jdbc-log4j2설정(139p, 160p)이 정상적이면 실행되는 SQL문장이 출력됨.
	//...예) INFO : jdbc.sqltiming - select count(bno) from tbl_board where bno > 0 
	@Test
	public void testDynamic1() throws Exception {

		logger.info("...S.testDynamic1");
	    SearchCriteria cri = new SearchCriteria();
	    cri.setPage(1);
	    cri.setKeyword("홈페이지 오픈 테스트");
	    cri.setSearchType("t");

	    logger.info("=====================================");

	    List<BoardVO> list = dao.listAll(cri);

	    for (BoardVO boardVO : list) {
	      logger.info("testDynamic1 : " + boardVO.getBno() + ": " + boardVO.getTitle());
	    }

	    logger.info("=====================================");

	    logger.info("COUNT: " + dao.countBno(cri));
	    
	    logger.info("...E.testDynamic1");
	}

}

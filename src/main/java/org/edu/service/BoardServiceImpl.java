package org.edu.service;
//...187p.
import java.util.List;

import javax.inject.Inject;

import org.edu.dao.IF_BoardDAO;
import org.edu.dao.IF_ReplyDAO;
import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

//...187, 376, 470p.@Service가 스프링의 빈으로 인식하게 함. root-context.xml::Beans Graph 확인할 것.
@Service
public class BoardServiceImpl implements IF_BoardService {

	@Inject
	private IF_BoardDAO boardDAO;
	
	@Inject
	private IF_ReplyDAO replyDAO;//...505p.dao→replyDAO로 변경.

	@Transactional
	@Override
	public void create(BoardVO board) throws Exception {
		boardDAO.create(board);
		//use for 첨부파일
		String[] files = board.getFiles();
	    if(files == null) { return; } 
	    for (String fileName : files) {
	    	boardDAO.addAttach(fileName);
	    }   
	}

	/*
	 * ...512p.주석처리.
	@Override
	public BoardVO read(Integer bno) throws Exception {
	    return dao.read(bno);
	}
	* ...512p.트랜잭션의 격리수준을 활용하여, 다른 연결이 커밋하지 않은 데이터는 볼 수 없도록 함.
	* 	 488p 참조 : Isolation.READ_COMMITTED 커밋된 데이터에 대해 읽기 허용.
	*/
	@Transactional(isolation=Isolation.READ_COMMITTED)
	@Override
	public BoardVO read(Integer bno) throws Exception {
		boardDAO.updateViewCnt(bno);
	    return boardDAO.read(bno);
	}

	@Transactional
	@Override
	public void update(BoardVO board) throws Exception {
		boardDAO.update(board);
		//use for 첨부파일
		Integer bno = board.getBno();
		boardDAO.deleteAttach(bno);
	    String[] files = board.getFiles();
	    if(files == null) { return; } 
	    for (String fileName : files) {
	    	boardDAO.replaceAttach(fileName, bno);
	    }
	}

	@Transactional
	@Override
	public void delete(Integer bno) throws Exception {
		replyDAO.deleteAllReply(bno);
		boardDAO.deleteAttach(bno);//use for 첨부파일
		boardDAO.delete(bno);
	}

	@Override
	public List<BoardVO> listAll(SearchCriteria cri) throws Exception {
		return boardDAO.listAll(cri);
	}
	
	//...280p.
	@Override
	public int countBno(SearchCriteria cri) throws Exception {
		return boardDAO.countBno(cri);
	}
	
	//use for 첨부파일
	@Override
	public List<String> getAttach(Integer bno) throws Exception {
	    return boardDAO.getAttach(bno);
	}

}

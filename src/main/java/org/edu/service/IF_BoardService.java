package org.edu.service;

import java.util.List;

import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;

//...186p.
public interface IF_BoardService {

	public void create(BoardVO board) throws Exception;
	
	public BoardVO read(Integer bno) throws Exception;
	
	public void update(BoardVO board) throws Exception;
	
	public void delete(Integer bno) throws Exception;
	
	public List<BoardVO> listAll(SearchCriteria cri) throws Exception;
	
	public int countBno(SearchCriteria cri) throws Exception;
	//use for 첨부파일
	public List<String> getAttach(Integer bno)throws Exception;
	
}

package org.edu.dao;

import java.util.List;

import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;

/***
 * DAO = Persistence 패키지.
 * @author Administrator
 *
 */
public interface IF_BoardDAO {

	public void create(BoardVO vo) throws Exception;
	
	public BoardVO read(Integer bno) throws Exception;
	
	public void update(BoardVO vo) throws Exception;
	
	public void delete(Integer bno) throws Exception;
	
	public List<BoardVO> listAll(SearchCriteria cri) throws Exception;	
		  
	//...279p.
	public int countBno(SearchCriteria cri) throws Exception;
		
	//...504p.
	public void updateReplyCnt(Integer bno, int amount)throws Exception;
	
	//...511p.
	public void updateViewCnt(Integer bno)throws Exception;
	
	//use for 첨부파일
	public void addAttach(String fullName)throws Exception;
	  
	public List<String> getAttach(Integer bno)throws Exception;  
   
	public void deleteAttach(Integer bno)throws Exception;
  
	public void replaceAttach(String fullName, Integer bno)throws Exception;
}

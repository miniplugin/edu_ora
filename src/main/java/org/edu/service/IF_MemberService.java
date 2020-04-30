package org.edu.service;

import java.util.List;

import org.edu.vo.MemberVO;
import org.edu.vo.SearchCriteria;

//...186p.
public interface IF_MemberService {

	public void create(MemberVO member) throws Exception;
	
	public MemberVO read(String user_id) throws Exception;
	
	public void update(MemberVO member) throws Exception;
	
	public void delete(String user_id) throws Exception;
	
	public int countUser_id(SearchCriteria cri) throws Exception;
	
	public List<MemberVO> listAll(SearchCriteria cri) throws Exception;
	
}

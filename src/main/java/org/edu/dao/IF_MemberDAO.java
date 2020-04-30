package org.edu.dao;
import java.util.List;
import org.edu.vo.MemberVO;
import org.edu.vo.SearchCriteria;

public interface IF_MemberDAO {

	public String getTime();
	
	public void insertMember(MemberVO vo) throws Exception;
	
	public void updateMember(MemberVO vo) throws Exception;
	
	public MemberVO selectMember(String userid)throws Exception;
	
	public int countUser_id(SearchCriteria cri) throws Exception;
	
	public List<MemberVO> listAll(SearchCriteria cri) throws Exception;	
	  
	public MemberVO readWithPW(String userid, String userpw)throws Exception;
	
	public void delete(String userid) throws Exception;//학생 실습용
}

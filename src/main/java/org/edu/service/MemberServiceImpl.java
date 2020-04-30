package org.edu.service;
//...187p.
import java.util.List;

import javax.inject.Inject;

import org.edu.dao.IF_MemberDAO;
import org.edu.vo.MemberVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

//...187, 376, 470p.@Service가 스프링의 빈으로 인식하게 함. root-context.xml::Beans Graph 확인할 것.
@Service
public class MemberServiceImpl implements IF_MemberService {

	@Inject
	private IF_MemberDAO memberDAO;
	
	@Transactional
	@Override
	public void create(MemberVO member) throws Exception {
		memberDAO.insertMember(member);   
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
	public MemberVO read(String user_id) throws Exception {
	    return memberDAO.selectMember(user_id);
	}

	@Transactional
	@Override
	public void update(MemberVO member) throws Exception {
		memberDAO.updateMember(member);
	}

	@Transactional
	@Override
	public void delete(String user_id) throws Exception {
		memberDAO.delete(user_id);
	}
	
	@Override
	public int countUser_id(SearchCriteria cri) throws Exception {
		return memberDAO.countUser_id(cri);
	}

	@Override
	public List<MemberVO> listAll(SearchCriteria cri) throws Exception {
		return memberDAO.listAll(cri);
	}

}

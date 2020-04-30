//...179p.
package org.edu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.SearchCriteria;
import org.springframework.stereotype.Repository;

/*
 * @Repository는 DAO를 스프링에 인식시키기 위해서 주로 사용함.
 * root-context.xml에서 context:component-scan base-package의 속성값이 정확해야
 * root-context.xml의 BeansGraph에 BoardDaoImpl이 나타남.
 * http://www.mybatis.org/mybatis-3/ko/java-api.html
 * SqlSessions 참조.
    <T> T selectOne(String statement, Object parameter)
	<E> List<E> selectList(String statement, Object parameter)
	<K,V> Map<K,V> selectMap(String statement, Object parameter, String mapKey)
	int insert(String statement, Object parameter)
	int update(String statement, Object parameter)
	int delete(String statement, Object parameter)
 */
@Repository
public class BoardDAOImpl implements IF_BoardDAO {

	//...root-context.xml의 org.mybatis.spring.SqlSessionTemplate을 주입받아 사용함.
	@Inject //자바규격 또는 @Autowired //스프링 전용
	private SqlSession session;
	
	//.../zex01/src/main/resources/mappers/boardMapper.xml에서 설정한 namespace 참조.	
	private static String namespace = "org.edu.mapper.BoardMapper";
	
	
	@Override
	public void create(BoardVO vo) throws Exception {
		session.insert(namespace + ".create", vo);
	}

	@Override
	public BoardVO read(Integer bno) throws Exception {
		return session.selectOne(namespace + ".read", bno);
	}

	@Override
	public void update(BoardVO vo) throws Exception {
	    session.update(namespace + ".update", vo);
	}

	@Override
	public void delete(Integer bno) throws Exception {
	    session.delete(namespace + ".delete", bno);
	}

	@Override
	public List<BoardVO> listAll(SearchCriteria cri) throws Exception {
		/*if(page <= 0) { //Criteria 클래스에서 getPageStart() 지정으로 대체
			page = 1;
		}
		page = (page -1) * 10;*/
	    return session.selectList(namespace + ".listAll", cri);
	}
	
	//...280p.
	@Override
	public int countBno(SearchCriteria cri) throws Exception {
	    return session.selectOne(namespace + ".countBno", cri);
	}
		
	//...504p.
	@Override
	public void updateReplyCnt(Integer bno, int amount) throws Exception {

	    Map<String, Object> paramMap = new HashMap<String, Object>();

	    paramMap.put("bno", bno);
	    paramMap.put("amount", amount);

	    session.update(namespace + ".updateReplyCount", paramMap);
	}
	
	//...511p.
	@Override
	public void updateViewCnt(Integer bno) throws Exception {
		session.update(namespace+".updateViewCount", bno);
	}
	
	//use for 첨부파일
	@Override
	public void addAttach(String fullName) throws Exception {
		session.insert(namespace+".addAttach", fullName);
	}
  
	@Override
	public List<String> getAttach(Integer bno) throws Exception {
		return session.selectList(namespace +".getAttach", bno);
	}
 
 	@Override
	public void deleteAttach(Integer bno) throws Exception {
		session.delete(namespace+".deleteAttach", bno);
	}

	@Override
	public void replaceAttach(String fullName, Integer bno) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("bno", bno);
		paramMap.put("fullName", fullName);
		session.insert(namespace+".replaceAttach", paramMap);
	}

}

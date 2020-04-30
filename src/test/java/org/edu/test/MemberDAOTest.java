//...137p.
package org.edu.test;
import javax.inject.Inject;

import org.junit.Test;
//...137p.
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.edu.dao.IF_MemberDAO;
import org.edu.vo.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	locations ={"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
@WebAppConfiguration
public class MemberDAOTest {

	@Inject
	private IF_MemberDAO dao;
	
	private static Logger logger = LoggerFactory.getLogger(MemberDAOTest.class);
	/* hsql 디비에서는 에러 mysql 은 정상
	@Test
	public void testTime()throws Exception{		
		System.out.println("edu.web.testTime() = " + dao.getTime());
		
	}	
	*/
	@Test
	public void testInsertMember()throws Exception{		
		MemberVO vo = new MemberVO();

		//학생 실습용 vo, if문과 else추가
		logger.info("vo in selectMember = " + dao.selectMember("user02"));
		if(dao.selectMember("user02") == null) {
			//스프링 시큐리티 4.x BCryptPasswordEncoder 암호 사용
			BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
			String bcryptPassword = bcryptPasswordEncoder.encode("user02");
			vo.setUser_id("user02");
			vo.setUser_pw(bcryptPassword);
			vo.setPoint(0);
			vo.setEnabled(true);
			vo.setLevels("ROLE_ADMIN");
			vo.setUser_name("user02");
			vo.setEmail("user02@edu.com");

			dao.insertMember(vo);
		}else {
			logger.info("testInsertMember in selectMember = " + vo.toString());
			logger.info("vo.user_id = " + vo.getUser_id());
			dao.delete("user02");
		}
	}	


	@Test
	public void selectMember()throws Exception{		
		dao.selectMember("user02");		
	}
	
	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}



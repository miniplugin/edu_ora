package org.edu.controller;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.edu.vo.MemberVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, HttpServletRequest request) {
		logger.info("Welcome home! The client locale is {}.", locale);
		HttpSession session = request.getSession();
		System.out.println("session_enabled= " + session.getAttribute("session_enabled"));
		System.out.println("session_username= " + session.getAttribute("session_username"));
		System.out.println("session_levels= " + session.getAttribute("session_levels"));
	    return "home";
	}
	
	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public String login_success(Locale locale,HttpServletRequest request, RedirectAttributes rdat) {
		logger.info("Welcome login_success! The client locale is {}.", locale);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = "";//anonymousUser
		String levels = "";//ROLE_ANONYMOUS
		//String password = "";
		Boolean enabled = false;
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			enabled = ((UserDetails)principal).isEnabled();
		}
		HttpSession session = request.getSession();
		if (enabled) {
		//if (authentication != null) {
        	//username = authentication.getName();
			/*
			Collection<? extends GrantedAuthority>  authorities = ((UserDetails)principal).getAuthorities();
			Iterator<? extends GrantedAuthority> iter = authorities.iterator();
			while (iter.hasNext()) { 
        		GrantedAuthority auth = iter.next(); 
        		levels = auth.getAuthority();
        		}
        	*/
			Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ANONYMOUS")).findAny().isPresent())
			{levels = "ROLE_ANONYMOUS";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_USER,")).findAny().isPresent())
			{levels = "ROLE_USER,";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent())
			{levels = "ROLE_ADMIN";}
			username =((UserDetails)principal).getUsername();
			//password =((UserDetails)principal).getPassword();
			/* 세션으로 처리로 변경해서 주석처리
			ModelAndView mav = new ModelAndView();
			MemberVO memberVO = new MemberVO();
	        memberVO.setUser_id(username);
	        memberVO.setUser_pw(password);
	        memberVO.setLevels(levels);
	        memberVO.setEnabled(enabled);
	        mav.addObject("memberVO",memberVO);
	        */
	        System.out.println("enabled= " + enabled);
		    System.out.println("username= " + username);
	    	System.out.println("levels= " + levels);
	    	System.out.println("로그인OK ======================");
	        //로그인 세션 저장
	        session.setAttribute("session_enabled", enabled);//인증확인
	        session.setAttribute("session_username", username);//사용자명
	        session.setAttribute("session_levels", levels);//사용자권한
        }
		rdat.addFlashAttribute("msg", "success");//result 데이터를 숨겨서 전송
		//String referer = request.getHeader("Referer");
		return "redirect:/";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminHome(Locale locale, Model model) {
		logger.info("Welcome admin! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "/admin/home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(Locale locale) {
		logger.info("Welcome login page! The client locale is {}.", locale);
		//암호를 잊어 버렸을때 더미암호 확인
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		System.out.println("더미암호= " + bcryptPasswordEncoder.encode("user02"));
	    return "login";
	}
	
	//...396p.
	@RequestMapping(value = "/testAjax", method = RequestMethod.GET)
	public void testAjax() {
	
	}
}

package org.edu.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.edu.service.IF_BoardService;
import org.edu.vo.BoardVO;
import org.edu.vo.Criteria;
import org.edu.vo.PageMaker;
import org.edu.vo.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles requests for the application 관리자 page.
 */
@Controller
@RequestMapping("/admin/board/*")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Inject
	private IF_BoardService service;
	/**
	 * Simply selects the admin view to render by returning its name.
	 */

	 @RequestMapping(value = "/create", method = RequestMethod.GET)
	 public void createGET(BoardVO board, Model model) throws Exception {
		 logger.info("createGet called ...........");
	    
	 }
	  
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createPOST(BoardVO board, Model model, RedirectAttributes rdat) throws Exception {
	
		logger.info("create post ...........");
		logger.info(board.toString());
		  
		service.create(board);
		  
		//model.addAttribute("msg", "success");//result 데이터를 보이면서 전송
		rdat.addFlashAttribute("msg", "success");//result 데이터를 숨겨서 전송
		//return "/admin/board/success";//새로고침시 기존 데이터 재전송 및 신규등록
		return "redirect:/admin/board/listAll";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	   
	}
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public String listAll(Locale locale, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("Welcome listAll! The client locale is {}.", locale);
		logger.info("cri.toString()= ", cri.toString());
		model.addAttribute("list", service.listAll(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		//pageMaker.setTotalCount(131);
		pageMaker.setTotalCount(service.countBno(cri));// ...281p.
		model.addAttribute("pageMaker", pageMaker);
		return "/admin/board/listAll";
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void read(Locale locale, @RequestParam("bno") int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("Welcome read! The client locale is {}.", locale);
		model.addAttribute(service.read(bno));
		//return "/admin/board/read";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam("bno") int bno, SearchCriteria cri, RedirectAttributes rdat) throws Exception {
	
		logger.info("delete post ...........");
		  
		service.delete(bno);
		rdat.addAttribute("page", cri.getPage());//페이징 넘버
		rdat.addAttribute("perPageNum", cri.getPerPageNum());//페이지당 게시물수
		rdat.addAttribute("searchType", cri.getSearchType());//검색 타입 전송
		rdat.addAttribute("keyword", cri.getKeyword());//검색어 전송
		rdat.addFlashAttribute("msg", "success");//result 데이터를 숨겨서 전송
		return "redirect:/admin/board/listAll";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public void modifyGET(Locale locale, int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("Welcome updateGET! The client locale is {}.", locale);
		model.addAttribute(service.read(bno));
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Locale locale, BoardVO board, SearchCriteria cri, Model model, RedirectAttributes rdat) throws Exception {
		logger.info("Welcome updatePOST! The client locale is {}.", locale);
		service.update(board);
		rdat.addAttribute("page", cri.getPage());//페이징 넘버
		rdat.addAttribute("perPageNum", cri.getPerPageNum());//페이지당 게시물수
		rdat.addAttribute("searchType", cri.getSearchType());//검색 타입 전송
		rdat.addAttribute("keyword", cri.getKeyword());//검색어 전송
		rdat.addFlashAttribute("msg", "success");//result 데이터를 숨겨서 전송
		return "redirect:/admin/board/listAll";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	}
}

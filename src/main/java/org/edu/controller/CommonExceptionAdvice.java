package org.edu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application error page.
 */
@ControllerAdvice
public class CommonExceptionAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);
	
	/**
	 * Simply selects the Exception view to render by returning its name.
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView errorModelAndView(Exception ex) {
		logger.info(ex.toString());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error_common");
		modelAndView.addObject("exception", ex);
		return modelAndView;
	}
}
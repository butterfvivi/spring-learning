package org.vivi.framework.ureport.simple.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements  org.springframework.boot.web.servlet.error.ErrorController {

	@RequestMapping(value="/error",produces = MediaType.TEXT_HTML_VALUE)
	public String handlePageError(HttpServletRequest request, HttpServletResponse response) {
		int status = response.getStatus();
		if(status == 404) {
			response.setStatus(200);
			return "index.html";
		}
		return null;
	}
}


package yyl.mvc.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class DefaultController {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();


	@RequestMapping(value = "/**/**.html")
	public String route(HttpServletRequest request, HttpServletResponse response) {
		String path = extractPathWithinPattern(request);
		return FilenameUtils.removeExtension(path);
	}

	private String extractPathWithinPattern(HttpServletRequest request) {
		return antPathMatcher.extractPathWithinPattern(//
				(String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE), //
				(String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)//
		);
	}
}

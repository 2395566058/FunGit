package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyPostController {
	@RequestMapping(value = "/myPost.action", method = RequestMethod.GET)
	public ModelAndView getHtml(HttpServletRequest request, ModelAndView model) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model.setViewName("myPost");
		return model;
	}
	
	@RequestMapping(value = "/myPost1.action", method = RequestMethod.GET)
	public ModelAndView getHtml1(HttpServletRequest request, ModelAndView model) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model.setViewName("myPost-part1");
		return model;
	}
}

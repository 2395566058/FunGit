package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value = "/home.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		mv.setViewName("home");
		return mv;
	}
}

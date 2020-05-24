package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import keilen.local.servlet.PersonalHomeServlet;

@Controller
public class PersonalHomeController {
	@Autowired
	private PersonalHomeServlet personalHomeServlet;

	@RequestMapping(value = "/personalHome.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request, String id) {
		if (request.getSession().getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		if (id.equals(request.getSession().getAttribute("id"))) {
			mv.addObject("oneself", "true");
			mv = personalHomeServlet.getPersonalHome(mv, id,"0");
		} else {
			mv.addObject("oneself", "false");
			mv = personalHomeServlet.getPersonalHome(mv, id,"1");
		}
		mv.setViewName("personalHome");
		return mv;
	}
}

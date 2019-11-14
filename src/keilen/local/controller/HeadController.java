package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HeadController {

	@RequestMapping(value = "/head.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request, String num) {
		HttpSession session = request.getSession();
		mv.addObject("num", Integer.parseInt(num));
		mv.addObject("name", session.getAttribute("name"));
		mv.addObject("head", session.getAttribute("head"));
		mv.setViewName("head");
		return mv;
	}

	@RequestMapping(value = "/exit", method = RequestMethod.POST)
	@ResponseBody
	public String exit(HttpServletRequest request) {
		request.getSession().invalidate();
		return "已退出登录";
	}
}

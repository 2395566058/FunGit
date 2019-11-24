package keilen.local.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.entity.PostPersonal;
import keilen.local.servlet.PostServlet;

@Controller
public class HomeController {
	@Autowired
	private PostServlet postServlet;

	@RequestMapping(value = "/home.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request) throws ParseException {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		List<PostPersonal> hotlist = postServlet.getHotPost(0);
		List<PostPersonal> newlist = postServlet.getNewPost(0);
		mv.addObject("hotlist", hotlist);
		mv.addObject("newlist", newlist);
		mv.setViewName("home");
		return mv;
	}

	@RequestMapping(value = "/getMoreHotPost", method = RequestMethod.POST)
	@ResponseBody
	public String getMoreHotPost(int num) {
		List<PostPersonal> hotlist = postServlet.getHotPost(num);
		if (hotlist.size() != 0) {
			return postServlet.List2JsonTypePostPersonal(hotlist);
		}
		return "";
	}

	@RequestMapping(value = "/getMoreNewPost", method = RequestMethod.POST)
	@ResponseBody
	public String getMoreNewPost(int num) throws ParseException {
		List<PostPersonal> newlist = postServlet.getNewPost(num);
		if (newlist.size() != 0) {
			return postServlet.List2JsonTypePostPersonal(newlist);
		}
		return "";
	}
}

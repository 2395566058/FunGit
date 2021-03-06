package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.mapper.PostPersonalMapper;
import keilen.local.servlet.PersonalHomeServlet;
import keilen.local.servlet.PostServlet;

@Controller
public class PersonalHomeController {
	@Autowired
	private PersonalHomeServlet personalHomeServlet;
	@Autowired
	private PostPersonalMapper postPersonalMapper;
	@Autowired
	private PostServlet postServlet;

	@RequestMapping(value = "/personalHomeById.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request, String id) {
		if (request.getSession().getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		if (id.equals(request.getSession().getAttribute("id"))) {
			mv.addObject("oneself", "true");
			mv = personalHomeServlet.getPersonalHome(mv, id, "0");
		} else {
			mv.addObject("oneself", "false");
			mv = personalHomeServlet.getPersonalHome(mv, id, "1");
		}
		mv.setViewName("personalHome");
		return mv;
	}

	@RequestMapping(value = "/personalHomeByName.action", method = RequestMethod.GET)
	public ModelAndView getHtml2(ModelAndView mv, HttpServletRequest request, String name) {
		String id = postPersonalMapper.getColumnByArg("user_personal", "id", "name", name);
		return getHtml(mv, request, id);
	}

	@RequestMapping(value = "/personalHome-post.action", method = RequestMethod.GET)
	public ModelAndView getHtml3(ModelAndView mv, String userid,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		mv = postServlet.searchPostByUserid(mv, userid, page);
		mv.setViewName("personalHome-post");
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/updateErmission", method = RequestMethod.POST)
	public String updateErmission(HttpServletRequest request, String column, String columnValue) {
		return personalHomeServlet.updateErmission((String) request.getSession().getAttribute("id"), column,
				columnValue);
	}
}

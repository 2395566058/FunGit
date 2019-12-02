package keilen.local.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.entity.PostPersonal;
import keilen.local.servlet.PostServlet;

@Controller
public class ShowSearchPostController {
	@Autowired
	private PostServlet postServlet;

	@RequestMapping(value = "/showSearchPost.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, String input, String select,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page) throws ParseException {
		mv = postServlet.searchPost(mv, input, select, page);
		mv.setViewName("showSearchPost");
		return mv;
	}
}

package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import keilen.local.servlet.PostServlet;

@Controller
public class MyPostController {
	@Autowired
	private PostServlet postServlet;

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
	public ModelAndView getHtml1(HttpServletRequest request, ModelAndView model,
			@RequestParam(name = "page",required = false,defaultValue = "1")String page) {
		System.out.println("page="+page);
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model = postServlet.getMyPostById(request.getSession(), model,Integer.parseInt(page));
		model.setViewName("myPost-part1");
		return model;
	}
	
	@RequestMapping(value = "/deletePost",method = RequestMethod.POST)
	@ResponseBody
	public String deletePost(String id) {
		return postServlet.deletePost(id);
	}
}

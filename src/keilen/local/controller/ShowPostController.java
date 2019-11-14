package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.servlet.PostServlet;

@Controller
public class ShowPostController {

	@Autowired
	private PostServlet postServlet;

	@RequestMapping(value = "/showPost.action", params = { "id" }, method = RequestMethod.GET)
	public ModelAndView getHtml(@RequestParam("id") String id,@RequestParam(name = "page",required = false,defaultValue = "1")String page,ModelAndView model, HttpServletRequest request) {
		System.out.println("page="+page);
		return postServlet.getPost(request, id, model, page);
	}

	@RequestMapping(value = "/reviewPost", method = RequestMethod.POST)
	@ResponseBody
	public String reviewPost(HttpServletRequest request, String postid, String content) {
		boolean result = false;
		while (!result) {
			result = postServlet.reviewPost(request.getSession(), postid, content);
		}
		return "发表成功！";
	}
	
	@RequestMapping(value="/addclicknum",method = RequestMethod.POST)
	@ResponseBody
	public String addclicknum(HttpServletRequest request,String postid) {
		postServlet.addclicknum(request.getSession(), postid);
		return "1";
	}
}

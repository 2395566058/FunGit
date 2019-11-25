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
	public ModelAndView getHtml(HttpServletRequest request,
			@RequestParam(name = "part", required = false, defaultValue = "1") String part, ModelAndView model) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model.addObject("part", part);
		model.setViewName("myPost");
		return model;
	}

	@RequestMapping(value = "/myPost1.action", method = RequestMethod.GET)
	public ModelAndView getHtml1(HttpServletRequest request, ModelAndView model,
			@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model = postServlet.getMyPostById(request.getSession(), model, Integer.parseInt(page));
		model.setViewName("myPost-part1");
		return model;
	}

	@RequestMapping(value = "/myPost2.action", method = RequestMethod.GET)
	public ModelAndView getHtml2(HttpServletRequest request, ModelAndView model,
			@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model = postServlet.getMyPostReviewUtil(model, request.getSession(), Integer.parseInt(page));
		model.setViewName("myPost-part2");
		return model;
	}

	@RequestMapping(value = "/myPost3.action", method = RequestMethod.GET)
	public ModelAndView getHtml3(HttpServletRequest request, ModelAndView model,
			@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			model.setViewName("login");
			return model;
		}
		model = postServlet.getReviewMeCards(model, request.getSession(), Integer.parseInt(page));
		model.setViewName("myPost-part3");
		return model;
	}

	//
	@RequestMapping(value = "/showReviewMeByPostId", method = RequestMethod.POST)
	@ResponseBody
	public String showReviewMeByPostId(HttpServletRequest request, String postid) {
		return postServlet.getShowReviewMeByPostid(postid, request.getSession());
	}

	//
	@RequestMapping(value = "/showPostReviewByPostId", method = RequestMethod.POST)
	@ResponseBody
	public String showPostReviewByPostId(HttpServletRequest request, String postid) {
		return postServlet.showPostReviewByPostId(request.getSession(), postid);
	}

	@RequestMapping(value = "/deleteReviewAll", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReviewAll(HttpServletRequest request, String postid) {
		return postServlet.deleteReviewAll(request.getSession(), postid);
	}

	@RequestMapping(value = "/deleteReviewByFloor", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReviewByFloor(String postid, String floor) {
		return postServlet.deleteReviewFloor(postid, floor);
	}

	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	@ResponseBody
	public String deletePost(String id) {
		return postServlet.deletePost(id);
	}
}

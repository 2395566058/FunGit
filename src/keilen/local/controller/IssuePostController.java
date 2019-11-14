package keilen.local.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.entity.PostPersonal;
import keilen.local.servlet.PostServlet;
import keilen.local.servlet.RedisCacheServlet;

@Controller
public class IssuePostController {
	@Autowired
	private RedisCacheServlet redisCacheServlet;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private PostServlet issuePostServlet;

	@RequestMapping(value = "/issuePost.action", method = RequestMethod.GET)
	public ModelAndView getHtml(HttpServletRequest request, ModelAndView model) {
		if (request.getSession().getAttribute("id") != null) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) redisTemplate.opsForValue()
					.get("forumName");
			if(list.size()==0) {
				redisCacheServlet.refreshHotPost();
				list = (List<HashMap<String, Object>>) redisTemplate.opsForValue()
						.get("forumName");
			}
			model.addObject("list", list);
			model.setViewName("issuePost");
			return model;
		}
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/issuePost", method = RequestMethod.POST)
	@ResponseBody
	public String issuePost(HttpServletRequest request, PostPersonal postPersonal) {
		return issuePostServlet.addPostPersonal(request.getSession(), postPersonal);
	}

	@RequestMapping(value = "/addImagefromIssuePost", method = RequestMethod.POST)
	@ResponseBody
	public String addImage(HttpServletRequest request, @RequestParam("imgData") MultipartFile imgData)
			throws Exception {
		return issuePostServlet.addimage(request, imgData);
	}
}

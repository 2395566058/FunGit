package keilen.local.controller;

import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.servlet.PostServlet;

@Controller
public class BlockController {
	@Autowired
	private PostServlet postServlet;

	@RequestMapping(value = "/showBlockPost.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, String block, String type,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page) throws ParseException {
		mv = postServlet.getBlockPost(mv, page, block, type);
		mv.setViewName("block-hot");
		return mv;
	}
}

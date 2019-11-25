package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import keilen.local.servlet.EmailServlet;
import keilen.local.servlet.FindPasswordServlet;

@Controller
public class FoegetController {
	@Autowired
	private FindPasswordServlet findPasswordServlet;
	@Autowired
	private EmailServlet emailServlet;

	@RequestMapping(value = "/forget.action", method = RequestMethod.GET)
	public String getHtml() {
		return "forget";
	}

	@RequestMapping(value = "/updateCode", method = RequestMethod.POST)
	@ResponseBody
	public String updateCode(HttpServletRequest request, String username) {
		return emailServlet.sendForgetCodeEmail(username, request.getSession());
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	@ResponseBody
	public String forgetPassword(HttpServletRequest request, String username, String code) {
		String result = findPasswordServlet.findPassword(request.getSession(), username, code);
		if (result.equals("true")) {
			result = emailServlet.sendForgetSuccessEmail(username, request.getSession());
		}
		return result;
	}
}

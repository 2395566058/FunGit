package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import keilen.local.servlet.EmailServlet;
import keilen.local.servlet.LoginServlet;

@Controller
public class LoginController {
	@Autowired
	private EmailServlet emailServlet;
	@Autowired
	private LoginServlet loginServlet;

	@RequestMapping(value = "/login.action", method = RequestMethod.GET)
	public String getHtml() {
		return "login";
	}

	@RequestMapping(value = "/getcode", method = RequestMethod.POST)
	@ResponseBody
	public String getCode(HttpServletRequest request) {
		String code = emailServlet.getCode(5);
		request.getSession().setAttribute("code", code);
		return code;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public String login(String username, String password, String code, HttpServletRequest request) {
		return loginServlet.login(username, password, code, request);
	}
}

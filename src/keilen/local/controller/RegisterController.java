package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import keilen.local.servlet.EmailServlet;
import keilen.local.servlet.RegisterServlet;

@Controller
public class RegisterController {
	@Autowired
	private EmailServlet emailServlet;

	@Autowired
	private RegisterServlet registerServlet;

	@RequestMapping(value = "/register.action", method = RequestMethod.GET)
	public String getHtml() {
		return "register";
	}

	@RequestMapping(value = "/getCode", method = RequestMethod.POST)
	@ResponseBody
	public String getCode(String email, HttpServletRequest request) {
		return emailServlet.sendRegisterEmail(email, request.getSession());
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(String username, String password, String email, String code, HttpServletRequest request) {
		return registerServlet.register(username, password, email, code, request,
				emailServlet.getCode(5) + System.currentTimeMillis());
	}
}

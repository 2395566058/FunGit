package keilen.local.servlet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import keilen.local.entity.User;
import keilen.local.mapper.UserMapper;

@Service
public class FindPasswordServlet {
	@Autowired
	private UserMapper userMapper;

	public String findPassword(HttpSession session, String username, String code) {
		String forgetUsername = (String) session.getAttribute("forgetUsername");
		String forgetCode = (String) session.getAttribute("forgetCode");
		if (!username.equals(forgetUsername)) {
			return "用户名或密码错误";
		}
		if (!code.equals(forgetCode)) {
			return "验证码错误";
		}
		return "true";
	}
}

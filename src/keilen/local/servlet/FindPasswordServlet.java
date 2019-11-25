package keilen.local.servlet;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class FindPasswordServlet {

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

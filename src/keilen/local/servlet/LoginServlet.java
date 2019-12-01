package keilen.local.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import keilen.local.entity.User;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;

@Service
public class LoginServlet {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPersonalMapper userPersonalMapper;
	@Autowired
	private RedisCacheServlet redisCacheServlet;

	public String login(String username, String password, String code, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		String sessionCode = (String) httpSession.getAttribute("code");
		if (sessionCode == null || !sessionCode.equals(code)) {
			return "验证码错误！";
		}
		boolean result = false;
		if (username.length() > 7) {
			String nameOrEmail = username.substring(username.length() - 7, username.length());
			if (nameOrEmail.equals("@qq.com")) {
				// 邮箱登录
				result = userMapper.existArgsByCloumn("user", username, "email");
				if (!result) {
					return "该邮箱不存在！";
				}
				result = userMapper.loginByEmail(username, password);
				if (result) {
					List<User> userList = userMapper.getListByArg("user", "email", username);
					User user = userList.get(0);
					httpSession.setMaxInactiveInterval(3600);
					httpSession.setAttribute("id", user.getId());
					httpSession.setAttribute("name", user.getName());
					httpSession.setAttribute("head", userPersonalMapper.getColumnByArg("user_personal", "head", "name", user.getName()));
					redisCacheServlet.addOnline(user.getId(), httpSession.getId());
					return "true";
				} else {
					return "密码错误！";
				}
			}
		}
		result = userMapper.existArgsByCloumn("user", username, "username");
		if (!result) {
			return "该账号不存在！";
		}
		result = userMapper.loginByUsername(username, password);
		if (result) {
			List<User> userList = userMapper.getListByArg("user", "username", username);
			User user = userList.get(0);
			httpSession.setMaxInactiveInterval(3600);
			httpSession.setAttribute("id", user.getId());
			httpSession.setAttribute("name", user.getName());
			httpSession.setAttribute("head", userPersonalMapper.getColumnByArg("user_personal", "head", "name", user.getName()));
			redisCacheServlet.addOnline(user.getId(), httpSession.getId());
			return "true";
		}
		return "密码错误！";
	}
}

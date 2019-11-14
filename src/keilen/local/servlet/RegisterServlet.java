package keilen.local.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import keilen.local.entity.User;
import keilen.local.entity.UserPersonal;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.util.NowTimeFormatUtil;

@Service
public class RegisterServlet {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPersonalMapper userPersonalMapper;

	@Transactional
	public String register(String username, String password, String email, String code, HttpServletRequest request,
			String name) {
		boolean result = userMapper.existUsername(username);
		if (!result) {
			result = userMapper.existEmail(email);
		} else {
			return "用户账号已被注册！";
		}
		if (!result) {
			String sessioncode = (String) request.getSession().getAttribute(email);
			if (sessioncode == null || !sessioncode.equals(code)) {
				return "验证码错误！";
			}
			User user = new User();
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name.substring(0, name.length() - 1));
			userMapper.insertUser(user);
			if (user.getId() != null) {
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(3600);
				session.removeAttribute(user.getEmail());
				// 创建用户userPersonal数据
				UserPersonal userPersonal = new UserPersonal();
				userPersonal.setId(user.getId());
				userPersonal.setName(user.getName());
				userPersonal.setHead("image/default-head.png");
				String date = NowTimeFormatUtil.getNowTime();
				userPersonal.setRegister(date);
				result = userPersonalMapper.insertUserPersonal(userPersonal);
				if (result) {
					session.setMaxInactiveInterval(3600);
					session.setAttribute("id", user.getId());
					session.setAttribute("name", user.getName());
					session.setAttribute("head", userPersonalMapper.getHeadByName(user.getName()));
					return "注册成功！";
				}
			}
			return "注册失败，请稍后重试!";
		} else {
			return "邮箱已被注册！";
		}
	}
}

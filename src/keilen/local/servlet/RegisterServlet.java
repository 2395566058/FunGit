package keilen.local.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import keilen.local.entity.User;
import keilen.local.entity.UserPersonal;
import keilen.local.mapper.SystemInfoMapper;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.mapper.ViewErmissionsMapper;
import keilen.local.util.NowTimeFormatUtil;

@Service
public class RegisterServlet {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPersonalMapper userPersonalMapper;
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	@Autowired
	private RedisCacheServlet redisCacheServlet;
	@Autowired
	private ViewErmissionsMapper viewErmissionsMapper;

	@Transactional
	public String register(String username, String password, String email, String code, HttpServletRequest request,
			String name) {
		try {
			boolean result = userMapper.existArgsByCloumn("user", username, "username");
			if (!result) {
				result = userMapper.existArgsByCloumn("user", email, "email");
			} else {
				return "用户账号已被注册！";
			}
			if (!result) {
				String sessioncode = (String) request.getSession().getAttribute(email);
				if (sessioncode == null || !sessioncode.equals(code)) {
					return "验证码错误！";
				}
				// 创建用户userl数据
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
						// 创建用户ViewErmissions数据
						result = viewErmissionsMapper.insertOne(user.getId());
						if (result) {
							session.setMaxInactiveInterval(3600);
							session.setAttribute("id", user.getId());
							session.setAttribute("name", user.getName());
							session.setAttribute("head",
									userPersonalMapper.getColumnByArg("user_personal", "head", "name", user.getName()));
							systemInfoMapper.addSystemInfo(user.getId(), "系统通知",
									"欢迎你注册乐趣论坛，本论坛可能有多处地方没有完善，请谅解。", NowTimeFormatUtil.getNowTime(), "0");
							redisCacheServlet.addOnline(user.getId(), session.getId());
							return "注册成功！";
						}
					}
				}
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return "注册失败，请稍后重试!";
			} else {
				return "邮箱已被注册！";
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "注册失败，请稍后重试!";
		}
	}
}

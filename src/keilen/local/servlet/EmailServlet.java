package keilen.local.servlet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import keilen.local.entity.User;
import keilen.local.mapper.UserMapper;

@Service
public class EmailServlet {
	static String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
			"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z" };
	static List list = Arrays.asList(beforeShuffle);// 将数组转换为集合

	@Autowired
	private UserMapper userMapper;

	/*
	 * 发送验证码到用户注册的邮箱，并设置session过期为10分钟
	 */
	public String sendRegisterEmail(String email, HttpSession session) {
		boolean result = userMapper.existEmail(email);
		if (result) {
			return "邮箱已被注册！";
		}
		String code = getCode(6);
		String status = sendEmail(email, code, "乐趣论坛注册验证码",
				"你好!感谢你注册乐趣论坛账号。\n你的注册验证码为:" + code + "\n" + "  (有效期为10分钟)");
		if (!status.equals("OK")) {
			return status;
		}
		session.setMaxInactiveInterval(600);
		session.setAttribute(email, code);
		return "验证码已经发送";
	}

	public String sendForgetCodeEmail(String username, HttpSession session) {
		session.setAttribute("forgetUsername", username);
		if (username.length() > 7) {
			String nameOrEmail = username.substring(username.length() - 7, username.length());
			if (nameOrEmail.equals("@qq.com")) {
				boolean result = userMapper.existEmail(username);
				if (!result) {
					return "邮箱不存在！";
				}
			} else {
				boolean result = userMapper.existUsername(username);
				if (!result) {
					return "用户账号不存在！";
				}
				User user = userMapper.getUserByUsername(username);
				username = user.getEmail();
			}
		} else {
			boolean result = userMapper.existUsername(username);
			if (!result) {
				return "用户账号不存在！";
			}
			User user = userMapper.getUserByUsername(username);
			username = user.getEmail();
		}
		String code = getCode(6);
		String status = sendEmail(username, code, "乐趣论坛用户密码找回验证码",
				"你好!这是您找回乐趣论坛账号的验证邮件。\n你的找回密码验证码为:" + code + "\n" + "  (有效期为5分钟)");
		if (!status.equals("OK")) {
			return status;
		}
		session.setMaxInactiveInterval(300);
		session.setAttribute("forgetCode", code);
		return "true";

	}

	public String sendForgetSuccessEmail(String username, HttpSession session) {
		if (username.length() > 7) {
			String nameOrEmail = username.substring(username.length() - 7, username.length());
			if (nameOrEmail.equals("@qq.com")) {

			} else {
				User user = userMapper.getUserByUsername(username);
				username = user.getEmail();
			}
		} else {
			User user = userMapper.getUserByUsername(username);
			username = user.getEmail();
		}
		String code = getCode(8);
		String status = sendEmail(username, code, "乐趣论坛用户随机密码",
				"你好!这是您成功找回乐趣论坛账号的邮件。\n你的新密码为:" + code + "\n" + "  请尽快登录后进行修改！");
		if (status.equals("OK")) {
			boolean result = userMapper.updatePasswordByEmail(code, username);
			if (result) {
				session.invalidate();
				return "true";
			}
		}
		return status;
	}

	public String sendEmail(String email, String code, String title, String msg) {
		try {
			SimpleEmail mail = new SimpleEmail();
			mail.setHostName("smtp.qq.com");// 发送邮件的服务器
			mail.setAuthentication("2395566058@qq.com", "dbgjwiqrhhmjecdj");// 刚刚记录的授权码，是开启SMTP的密码
			mail.setFrom("2395566058@qq.com", "乐趣论坛"); // 发送邮件的邮箱和发件人
			mail.setSSLOnConnect(true); // 使用安全链接
			mail.addTo(email);// 接收的邮箱
			mail.setSubject(title);// 设置邮件的主题
			mail.setMsg(msg);// 设置邮件的内容
			mail.send();// 发送
		} catch (EmailException e) {
			e.printStackTrace();
			return "未知错误";
		}
		return "OK";
	}

	public String getCode(int length) {
		Collections.shuffle(list); // 打乱集合顺序
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)); // 将集合转化为字符串
		}
		return sb.toString().substring(3, length + 3);// 要发送的验证码
	}
}

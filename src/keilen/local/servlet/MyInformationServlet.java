package keilen.local.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import keilen.local.entity.UserPersonal;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.util.ImageSaveUtil;

@Service
public class MyInformationServlet {
	@Autowired
	private UserPersonalMapper userPersonalMapper;
	@Autowired
	private UserMapper userMapper;

	public UserPersonal getInformationByName(HttpSession session) {
		return userPersonalMapper.getUserPersonalByName((String) session.getAttribute("name"));
	}

	@Transactional
	public String updateInformation(UserPersonal userPersonal, HttpSession session) {
		if (userPersonal.getName().equals(session.getAttribute("name"))) {
			userPersonal.setName("");
		} else {
			if (userPersonal.getName() == "" || userPersonal.getName().length() < 2) {
				return "昵称不符合规范";
			} else {
				boolean result = userPersonalMapper.existArgsByCloumn("user_personal", userPersonal.getName(), "name");				
				if (result) {
					return "昵称已经被使用！";
				}
			}
			userMapper.updateNameById(userPersonal.getName(), (String) session.getAttribute("id"));
		}
		if (userPersonal.getBirthday() != "") {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				df.parse(userPersonal.getBirthday());
			} catch (ParseException e) {
				return "生日格式错误，应为1999-09-09这样的格式！";
			}
		}
		userPersonal.setId((String) session.getAttribute("id"));
		boolean result = userPersonalMapper.updateUserPersonal(userPersonal);
		if (result) {
			if (!userPersonal.getName().equals("")) {
				session.setAttribute("name", userPersonal.getName());
			}
		}
		return "修改成功！";
	}

	@Transactional
	public String updateHead(MultipartFile data, HttpServletRequest request) throws Exception {
		String headUrl = ImageSaveUtil.saveImage(data, request);
		boolean result = userPersonalMapper.updateHeadById(headUrl, (String) request.getSession().getAttribute("id"));
		if (!result) {
			return "修改失败！请稍后重试！";
		}
		request.getSession().setAttribute("head", headUrl);
		return "修改成功！";
	}

	@Transactional
	public String updatePassword(String oldPassword, String newPassword, String id) {
		String password = userMapper.getColumnByArg("user", "password", "id", id);
		if (oldPassword.equals(password)) {
			boolean result = userMapper.updatePasswordById(newPassword, id);
			if (result) {
				return "修改成功！";
			}
			return "修改失败！请稍后重试";
		}
		return "密码错误！无法修改";
	}

}

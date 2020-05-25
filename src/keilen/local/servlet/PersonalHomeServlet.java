package keilen.local.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import keilen.local.entity.UserPersonal;
import keilen.local.entity.ViewErmissions;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.mapper.ViewErmissionsMapper;

@Service
public class PersonalHomeServlet {

	@Autowired
	private UserPersonalMapper userPersonalMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ViewErmissionsMapper viewErmissionsMapper;

	public ModelAndView getPersonalHome(ModelAndView mv, String id, String oneself) {
		UserPersonal userPersonal = userPersonalMapper.getOneById("user_personal", id);
		String email = userMapper.getColumnByArg("user", "email", "id", id);
		ViewErmissions viewErmissions = viewErmissionsMapper.getOneById("view_ermissions", id);
		if (oneself == "0") {
			mv.addObject("viewbirthday0", "0".equals(viewErmissions.getBirthday()) ? "checked" : "");
			mv.addObject("viewbirthday1", "0".equals(viewErmissions.getBirthday()) ? "" : "checked");
			mv.addObject("viewqq0", "0".equals(viewErmissions.getQq()) ? "checked" : "");
			mv.addObject("viewqq1", "0".equals(viewErmissions.getQq()) ? "" : "checked");
			mv.addObject("viewcity0", "0".equals(viewErmissions.getCity()) ? "checked" : "");
			mv.addObject("viewcity1", "0".equals(viewErmissions.getCity()) ? "" : "checked");
			mv.addObject("viewregister0", "0".equals(viewErmissions.getRegister()) ? "checked" : "");
			mv.addObject("viewregister1", "0".equals(viewErmissions.getRegister()) ? "" : "checked");
			mv.addObject("viewemail0", "0".equals(viewErmissions.getEmail()) ? "checked" : "");
			mv.addObject("viewemail1", "0".equals(viewErmissions.getEmail()) ? "" : "checked");
			mv.addObject("viewpost0", "0".equals(viewErmissions.getPost()) ? "checked" : "");
			mv.addObject("viewpost1", "0".equals(viewErmissions.getPost()) ? "" : "checked");

			mv.addObject("birthday", userPersonal.getBirthday() == null ? "" : userPersonal.getBirthday());
			mv.addObject("qq", userPersonal.getQq() == null ? "" : userPersonal.getQq());
			mv.addObject("city", userPersonal.getCity() == null ? "" : userPersonal.getCity());
			mv.addObject("register", userPersonal.getRegister());
			mv.addObject("email", email);
		} else {
			if ("0".equals(viewErmissions.getBirthday())) {
				mv.addObject("birthday", userPersonal.getBirthday() == null ? "" : userPersonal.getBirthday());
			} else {
				mv.addObject("birthday", "用户不允许他人查看");
			}
			if ("0".equals(viewErmissions.getQq())) {
				mv.addObject("qq", userPersonal.getQq() == null ? "" : userPersonal.getQq());
			} else {
				mv.addObject("qq", "用户不允许他人查看");
			}
			if ("0".equals(viewErmissions.getCity())) {
				mv.addObject("city", userPersonal.getCity() == null ? "" : userPersonal.getCity());
			} else {
				mv.addObject("city", "用户不允许他人查看");
			}
			if ("0".equals(viewErmissions.getRegister())) {
				mv.addObject("register", userPersonal.getRegister());
			} else {
				mv.addObject("register", "用户不允许他人查看");
			}
			if ("0".equals(viewErmissions.getEmail())) {
				mv.addObject("email", email);
			} else {
				mv.addObject("email", "用户不允许他人查看");
			}
		}
		mv.addObject("name", userPersonal.getName());
		mv.addObject("head2", userPersonal.getHead());
		mv.addObject("introduce", userPersonal.getIntroduce() == null ? "" : userPersonal.getIntroduce());
		mv.addObject("viewid", id);
		return mv;
	}

	@Transactional
	public String updateErmission(String id, String column, String columnValue) {
		boolean result = viewErmissionsMapper.updateOne(id, column, columnValue);
		if (result == true) {
			return "修改成功";
		} else {
			return "修改失败，未知原因。";
		}
	}
}

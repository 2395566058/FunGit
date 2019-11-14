package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.entity.UserPersonal;
import keilen.local.servlet.MyInformationServlet;

@Controller
public class MyInformationController {
	@Autowired
	private MyInformationServlet myInformationServlet;

	@RequestMapping(value = "/myInformation.action", method = RequestMethod.GET)
	public ModelAndView getHtml(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		mv.setViewName("myInformation");
		return mv;
	}

	@RequestMapping(value = "/myInformation1.action", method = RequestMethod.GET)
	public ModelAndView getHtml1(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		UserPersonal userPersonal = myInformationServlet.getInformationByName(session);
		mv.addObject("name", userPersonal.getName());
		mv.addObject("birthday", userPersonal.getBirthday() == null ? "" : userPersonal.getBirthday());
		mv.addObject("qq", userPersonal.getQq() == null ? "" : userPersonal.getQq());
		mv.addObject("city", userPersonal.getCity() == null ? "" : userPersonal.getCity());
		mv.addObject("register", userPersonal.getRegister());
		mv.addObject("introduce", userPersonal.getIntroduce() == null ? "" : userPersonal.getIntroduce());
		mv.setViewName("myInformation-part1");
		return mv;
	}

	@RequestMapping(value = "/myInformation2.action", method = RequestMethod.GET)
	public ModelAndView getHtml2(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		mv.addObject("head", session.getAttribute("head"));
		mv.setViewName("myInformation-part2");
		return mv;
	}

	@RequestMapping(value = "/myInformation3.action", method = RequestMethod.GET)
	public ModelAndView getHtml3(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			mv.setViewName("login");
			return mv;
		}
		mv.setViewName("myInformation-part3");
		return mv;
	}

	@RequestMapping(value = "/updateInformation", method = RequestMethod.POST)
	@ResponseBody
	public String updateInformation(HttpServletRequest request, UserPersonal userPersonal) {
		return myInformationServlet.updateInformation(userPersonal, request.getSession());
	}

	@RequestMapping(value = "/updateImg", method = RequestMethod.POST)
	@ResponseBody
	public String updateImg(HttpServletRequest request, @RequestParam("imgData") MultipartFile imgData)
			throws Exception {
		return myInformationServlet.updateHead(imgData, request);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public String updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {
		return myInformationServlet.updatePassword(oldPassword, newPassword,
				(String) request.getSession().getAttribute("id"));
	}
}

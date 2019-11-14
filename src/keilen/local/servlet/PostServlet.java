package keilen.local.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import keilen.local.entity.PostFloor;
import keilen.local.entity.PostPersonal;
import keilen.local.mapper.ForumMapper;
import keilen.local.mapper.PostFloorMapper;
import keilen.local.mapper.PostPersonalMapper;
import keilen.local.mapper.UserMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.util.ImageSaveUtil;
import keilen.local.util.NowTimeFormatUtil;
import keilen.local.util.ShowPostReviewUtil;
import keilen.local.util.ShowPostUtil;

@Service
public class PostServlet {
	@Autowired
	private ForumMapper forumMapper;
	@Autowired
	private PostPersonalMapper postPersonalMapper;
	@Autowired
	private UserPersonalMapper userPersonalMapper;
	@Autowired
	private PostFloorMapper postFloorMapper;

	public String addimage(HttpServletRequest request, MultipartFile imgData) {
		try {
			return ImageSaveUtil.saveImage(imgData, request);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}

	@Transactional
	public boolean reviewPost(HttpSession session, String postid, String content) {
		PostFloor pf = new PostFloor();
		int index = content.indexOf("<input disabled=\"disabled\" id=\"reviewfloor");
		if (index != -1) {
			String newContent = content.substring(index + 42, index + 52);
			String floor = newContent.substring(0, newContent.indexOf("\""));
			String reviewid = postFloorMapper.getIdByPostidAndFloor(postid, floor);
			pf.setReviewid(reviewid);
			int stringHead = index;
			int stringFoot = content.indexOf("楼:\">") + 4;
			String head = content.substring(0, stringHead);
			String foot = content.substring(stringFoot, content.length());
			content = head.concat(foot);
		}
		pf.setPostid(postid);
		pf.setContent(content);
		String userid = (String) session.getAttribute("id");
		String issuetime = NowTimeFormatUtil.getNowTime();
		int count = postFloorMapper.getCountByPostid(postid);
		pf.setUserid(userid);
		pf.setFloor(String.valueOf(count + 1));
		pf.setIssuetime(issuetime);
		boolean result = postFloorMapper.insertOne(pf);
		if (result) {
			int nowCount = postFloorMapper.getCountByPostidAndFloor(postid, pf.getFloor());
			if (nowCount != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			postPersonalMapper.addreviewnumByid(postid);
			return true;
		}
		return false;

	}

	@Transactional
	public String addPostPersonal(HttpSession session, PostPersonal postPersonal) {
		String id = (String) session.getAttribute("id");
		postPersonal.setUserid(id);
		postPersonal.setForumid(forumMapper.getIdByName(postPersonal.getForumid()));
		String date = NowTimeFormatUtil.getNowTime();
		postPersonal.setIssuetime(date);
		boolean result = postPersonalMapper.insertOne(postPersonal);
		if (result) {
			return postPersonal.getId();
		}
		return "false";
	}

	public ModelAndView getPost(HttpServletRequest request, String id, ModelAndView model, String page) {
		// postid title clicknum writerName reviewnum forumName <list>floor
		// floor: floor content useName useHead issuetime
		PostPersonal pp = postPersonalMapper.getPersonalById(id);
		if (pp.getId() == null) {
			model.addObject("404", "找不到该帖子了啦！");
			model.addObject("url", request.getRequestURL());
			model.setViewName("error");
			return model;
		}
		model.addObject("postid", id);
		model.addObject("title", pp.getTitle());
		model.addObject("clicknum", pp.getClicknum());
		String writerName = userPersonalMapper.getNameById(pp.getUserid());
		model.addObject("writerName", writerName);
		model.addObject("reviewnum", pp.getReviewnum());
		String forumName = forumMapper.getNameById(pp.getForumid());
		model.addObject("forumName", forumName);
		int page_int = Integer.parseInt(page);
		List<ShowPostUtil> floorlist = postFloorMapper.getFloorsById(id, 5 * page_int - 4, 5 * page_int);
		if (page.equals("1")) {
			ShowPostUtil spu = new ShowPostUtil();
			spu.setFloor("0");
			spu.setContent(pp.getContent());
			spu.setUseName(writerName);
			spu.setUseHead(userPersonalMapper.getHeadByName(writerName));
			spu.setIssuetime(pp.getIssuetime());
			floorlist.add(0, spu);
		}
		model.addObject("floor", floorlist);
		model.addObject("review", null);
		if (request.getSession().getAttribute("id") != null) {
			model.addObject("loginHead", request.getSession().getAttribute("head"));
			model.addObject("loginName", request.getSession().getAttribute("name"));
			model.addObject("isLogin", "true");
		} else {
			model.addObject("isLogin", "false");
		}
		int floorNum=postFloorMapper.getCountByPostid(id);
		model.addObject("floorNum", floorNum);
		model.setViewName("showPost");
		return model;
	}

	@Transactional
	public void addclicknum(HttpSession session, String postid) {
		String status = (String) session.getAttribute("clicknum" + postid);
		if (status == null) {
			postPersonalMapper.addclicknumByid(postid);
			session.setAttribute("clicknum" + postid, "1");
		}
	}
}

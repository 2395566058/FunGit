package keilen.local.servlet;

import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import keilen.local.entity.Deleted;
import keilen.local.entity.PostFloor;
import keilen.local.entity.PostPersonal;
import keilen.local.entity.SystemInfo;
import keilen.local.mapper.DeletedMapper;
import keilen.local.mapper.ForumMapper;
import keilen.local.mapper.PostFloorMapper;
import keilen.local.mapper.PostPersonalMapper;
import keilen.local.mapper.SystemInfoMapper;
import keilen.local.mapper.UserPersonalMapper;
import keilen.local.mapper.ViewErmissionsMapper;
import keilen.local.util.ImageSaveUtil;
import keilen.local.util.NowTimeFormatUtil;
import keilen.local.util.ReviewMeUtil;
import keilen.local.util.ShowMyPostReviewUtil;
import keilen.local.util.ShowPostReviewUtil;
import keilen.local.util.ShowPostUtil;
import keilen.local.util.ShowReviewUtil;

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
	@Autowired
	private DeletedMapper deletedMapper;
	@Autowired
	private RedisCacheServlet redisCacheServlet;
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	@Autowired
	private ViewErmissionsMapper viewErmissionsMapper;

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
		try {
			PostFloor pf = new PostFloor();
			String reviewUserid;
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
				reviewUserid = postFloorMapper.getColumnByArg("post_floor", "userid", "id", reviewid);
			}
			reviewUserid = postPersonalMapper.getPersonalById(postid).getUserid();
			pf.setPostid(postid);
			pf.setContent(content);
			String userid = (String) session.getAttribute("id");
			String issuetime = NowTimeFormatUtil.getNowTime();
			int count = postFloorMapper.getCountByArg("post_floor", "postid", postid);
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
				if (!reviewUserid.equals(userid)) {
					String title = postPersonalMapper.getTitleById(postid);
					String object = "您在帖子 " + title + " 中有一条回复：<br>" + content;
					redisCacheServlet.addMessage(reviewUserid, object, "ReviewReview");
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	@Transactional
	public String addPostPersonal(HttpSession session, PostPersonal postPersonal) {
		try {
			String id = (String) session.getAttribute("id");
			postPersonal.setUserid(id);
			postPersonal.setForumid(forumMapper.getColumnByArg("forum", "id", "name", postPersonal.getForumid()));
			String date = NowTimeFormatUtil.getNowTime();
			postPersonal.setIssuetime(date);
			String content=postPersonal.getContent();
			content=content.replaceAll("<div>", "<p>");
			content=content.replaceAll("</div>", "</p>");
			postPersonal.setContent(content);
			boolean result = postPersonalMapper.insertOne(postPersonal);
			if (result) {
				return postPersonal.getId();
			}
			return "false";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "false";
		}
	}

	public ModelAndView getPost(HttpServletRequest request, String id, ModelAndView model, String page) {
		// postid title clicknum writerName reviewnum forumName <list>floor
		// floor: floor content useName useHead issuetime
		PostPersonal pp = postPersonalMapper.getPersonalById(id);
		if (!pp.getUserid().equals(request.getSession().getAttribute("id"))) {
			String viewpost = viewErmissionsMapper.getColumnByArg("view_ermissions", "post", "id", pp.getUserid());
			if ("1".equals(viewpost)) {
				model.addObject("info", "该帖子只有发帖用户可以看");
				return model;
			}
		}
		model.addObject("info", "0");
		if (pp.getId() == null) {
			model.addObject("404", "找不到该帖子了啦！");
			model.addObject("url", request.getRequestURL());
			model.setViewName("error");
			return model;
		}
		model.addObject("postid", id);
		model.addObject("title", pp.getTitle());
		model.addObject("clicknum", pp.getClicknum());
		String writerName = userPersonalMapper.getColumnByArg("user_personal", "name", "id", pp.getUserid());
		model.addObject("writerName", writerName);
		model.addObject("reviewnum", pp.getReviewnum());
		String forumName = forumMapper.getColumnByArg("forum", "name", "id", pp.getForumid());
		model.addObject("forumName", forumName);
		int floorNum = postFloorMapper.getCountByArg("post_floor", "postid", id);
		model.addObject("floorNum", floorNum);
		int page_int = 1;
		if (page.equals("final")) {
			if (floorNum % 5 > 0) {
				page_int = floorNum / 5 + 1;
			} else {
				page_int = floorNum / 5;
			}
			page=String.valueOf(page_int);
		} else {
			page_int = Integer.parseInt(page);
		}
		List<ShowPostUtil> floorlist = postFloorMapper.getFloorsById(id, 5 * page_int - 4, 5 * page_int);
		if (page.equals("1")) {
			ShowPostUtil spu = new ShowPostUtil();
			spu.setFloor("0");
			spu.setContent(pp.getContent());
			spu.setUseName(writerName);
			spu.setUseHead(userPersonalMapper.getColumnByArg("user_personal", "head", "name", writerName));
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
		model.setViewName("showPost");
		return model;
	}

	@Transactional
	public void addclicknum(HttpSession session, String postid) {
		try {
			String status = (String) session.getAttribute("clicknum" + postid);
			if (status == null) {
				postPersonalMapper.addclicknumByid(postid);
				session.setAttribute("clicknum" + postid, "1");
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

	public ModelAndView getMyPostReviewUtil(ModelAndView model, HttpSession session, int page) {
		String userid = (String) session.getAttribute("id");
		int count = postFloorMapper.getCountByUserid(userid);
		if (count == 0) {
			model.addObject("list", null);
			return model;
		}
		int countpage = 0;
		if (count % 5 != 0) {
			countpage = count / 5 + 1;
		} else {
			countpage = count / 5;
		}
		List<ShowMyPostReviewUtil> list = postFloorMapper.getMyPostReviewUtil(userid, page);
		model.addObject("list", list);
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		return model;
	}

	public ModelAndView getMyPostById(HttpSession session, ModelAndView model, int page) {
		String userid = (String) session.getAttribute("id");
		int count = postPersonalMapper.getCountbyUserid(userid);
		if (count == 0) {
			model.addObject("myPost", null);
			return model;
		}
		int countpage = 0;
		if (count % 5 != 0) {
			countpage = count / 5 + 1;
		} else {
			countpage = count / 5;
		}
		List<PostPersonal> pp = postPersonalMapper.getPersonalsByUseid(userid, page);
		model.addObject("myPost", pp);
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		return model;
	}

	@Transactional
	public String deletePost(String id) {
		try {
			Deleted deleteEntity = new Deleted();
			deleteEntity.setDeletetime(NowTimeFormatUtil.getNowTime());
			deleteEntity.setOperationid("0");
			deletedMapper.deleteTable(deleteEntity);
			postPersonalMapper.deletePostPersonalById(deleteEntity.getId(), id);
			return "true";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "false";
		}
	}

	public String showPostReviewByPostId(HttpSession session, String postid) {
		List<ShowPostReviewUtil> list = postFloorMapper.getReviewByUseridAndPostId((String) session.getAttribute("id"),
				postid);
		if (list.size() == 0) {
			return "";
		}
		StringBuffer result = new StringBuffer("[");
		for (ShowPostReviewUtil value : list) {
			StringBuffer result2 = new StringBuffer("{");
			result2.append("\"floor\":\"" + value.getFloor() + "\",");
			String result3 = new String(value.getContent());
			result3 = result3.replace("\"", "\'");
			result2.append("\"content\":\"" + result3 + "\",");
			result2.append("\"issuetime\":\"" + value.getIssuetime() + "\",");
			if (value.getReviewName() == null || value.getReviewName().equals("")) {
				result2.append("\"reviewName\":\"\"");
			} else {
				result2.append("\"reviewName\":\"" + value.getReviewName() + "\"");
			}
			result2.append("}");
			result.append(result2 + ",");
		}
		result = result.deleteCharAt(result.length() - 1);
		result.append("]");
		return result.toString();
	}

	@Transactional
	public String deleteReviewFloor(String postid, String floor) {
		try {
			Deleted deleted = new Deleted();
			deleted.setOperationid("0");
			deleted.setDeletetime(NowTimeFormatUtil.getNowTime());
			deletedMapper.deleteTable(deleted);
			boolean result = postFloorMapper.deletePostReview(postid, floor, deleted.getId());
			if (result) {
				return "true";
			}
			return "false";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "false";
		}
	}

	@Transactional
	public String deleteReviewAll(HttpSession session, String postid) {
		try {
			Deleted deleted = new Deleted();
			deleted.setOperationid("0");
			deleted.setDeletetime(NowTimeFormatUtil.getNowTime());
			deletedMapper.deleteTable(deleted);
			boolean result = postFloorMapper.deletePostReviewAll(postid, (String) session.getAttribute("id"),
					deleted.getId());
			if (result) {
				return "true";
			}
			return "false";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "false";
		}
	}

	public ModelAndView getReviewMeCards(ModelAndView model, HttpSession session, int page) {
		int count = postFloorMapper.getCountforReviewUtilBuUserid((String) session.getAttribute("id"));
		if (count == 0) {
			model.addObject("list", null);
			return model;
		}
		int countpage = 0;
		if (count % 5 != 0) {
			countpage = count / 5 + 1;
		} else {
			countpage = count / 5;
		}
		List<ReviewMeUtil> list = postFloorMapper.getReviewUtilByUserid((String) session.getAttribute("id"), page);
		model.addObject("list", list);
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		return model;
	}

	public ModelAndView getSystemInfo(ModelAndView model, HttpSession session, int page) {
		int count = systemInfoMapper.getCountByArg("systeminfo", "userid", (String) session.getAttribute("id"));
		if (count == 0) {
			model.addObject("list", null);
			return model;
		}
		int countpage = 0;
		if (count % 5 != 0) {
			countpage = count / 5 + 1;
		} else {
			countpage = count / 5;
		}
		List<SystemInfo> list = systemInfoMapper.getSystemInfoByUserid((String) session.getAttribute("id"), page);
		model.addObject("list", list);
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		return model;
	}

	public String getShowReviewMeByPostid(String postid, HttpSession session) {
		List<ShowReviewUtil> list = postFloorMapper.getMyReviewByPostid((String) session.getAttribute("id"), postid);
		StringBuffer result = new StringBuffer("[");
		for (ShowReviewUtil value : list) {
			StringBuffer result2 = new StringBuffer("{");
			result2.append("\"floor\":\"" + value.getFloor() + "\",");
			String result3 = new String(value.getContent());
			result3 = result3.replace("\"", "\'");
			result2.append("\"content\":\"" + result3 + "\",");
			result2.append("\"issuetime\":\"" + value.getIssuetime() + "\",");
			StringBuffer result4 = new StringBuffer("[");
			for (PostFloor value2 : value.getReview()) {
				StringBuffer result5 = new StringBuffer("{");
				result5.append("\"floor\":\"" + value2.getFloor() + "\",");
				result5.append("\"userName\":\"" + value2.getUserid() + "\",");
				String result6 = new String(value2.getContent());
				result6 = result6.replace("\"", "\'");
				result5.append("\"content\":\"" + result6 + "\",");
				result5.append("\"issuetime\":\"" + value2.getIssuetime() + "\"");
				result5.append("}");
				result4.append(result5 + ",");
			}
			result4 = result4.deleteCharAt(result4.length() - 1);
			result4.append("]");
			String review = result4.toString();
			result2.append("\"review\":" + review);
			result2.append("}");
			result.append(result2 + ",");
		}
		result = result.deleteCharAt(result.length() - 1);
		result.append("]");
		return result.toString();
	}

	public List<PostPersonal> getHotPost(int num) {
		return postPersonalMapper.getHotPostForHome(num);
	}

	public List<PostPersonal> getNewPost(int num) throws ParseException {
		List<PostPersonal> list = postPersonalMapper.getNewPostForHome(num);
		for (PostPersonal value : list) {
			value.setIssuetime(NowTimeFormatUtil.getTimeFormat(value.getIssuetime()));
		}
		return list;
	}

	public ModelAndView getBlockPost(ModelAndView model, int page, String block, String type) {
		model.addObject("dhblock", block);
		int num = 6 * (page - 1);
		if ("".equals(block)) {
			return model;
		}
		List<PostPersonal> list = null;
		int countpage = 0;
		String blockid = forumMapper.getColumnByArg("forum", "id", "name", block);
		int count = postPersonalMapper.getCountByForumid(blockid);
		if (count % 6 != 0) {
			countpage = count / 6 + 1;
		} else {
			countpage = count / 6;
		}
		model.addObject("count", count);
		if ("hot".equals(type)) {
			list = postPersonalMapper.getHotListByForumid(blockid, num);
		} else if ("new".equals(type)) {
			list = postPersonalMapper.getNewListByForumid(blockid, num);
		} else {
			list = postPersonalMapper.getRandowListByForumid(blockid, num);
		}
		if (list == null || list.size() == 0) {
			return model;
		}
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		model.addObject("searchlist", list);
		return model;
	}

	public ModelAndView searchPostByUserid(ModelAndView model, String userid, int page) {
		model.addObject("dhinput", userid);
		model.addObject("viewid", userid);
		int num = 8 * (page - 1);
		if ("".equals(userid)) {
			return model;
		}
		List<PostPersonal> list;
		int countpage = 0;
		int count = postPersonalMapper.getCountbyUserid(userid);
		if (count % 8 != 0) {
			countpage = count / 8 + 1;
		} else {
			countpage = count / 8;
		}
		model.addObject("count", count);
		list = postPersonalMapper.getListByUserid(userid, num);
		if (list == null || list.size() == 0) {
			return model;
		}
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		model.addObject("searchlist", list);
		return model;
	}

	public ModelAndView searchPost(ModelAndView model, String input, String select, int page) {
		model.addObject("dhinput", input);
		model.addObject("dhselect", select);
		int num = 8 * (page - 1);
		if ("".equals(input)) {
			return model;
		}
		String inputlike = "%" + input + "%";
		List<PostPersonal> list;
		int countpage = 0;
		if ("title".equals(select)) {
			int count = postPersonalMapper.getCountLikeTitle(inputlike);
			if (count % 8 != 0) {
				countpage = count / 8 + 1;
			} else {
				countpage = count / 8;
			}
			model.addObject("count", count);
			list = postPersonalMapper.getListLikeTitle(inputlike, num);
		} else if ("name".equals(select)) {
			int count = postPersonalMapper.getCountLikeUserid(inputlike);
			if (count % 8 != 0) {
				countpage = count / 8 + 1;
			} else {
				countpage = count / 8;
			}
			model.addObject("count", count);
			list = postPersonalMapper.getListLikeUserid(inputlike, num);
		} else {
			return model;
		}
		if (list == null || list.size() == 0) {
			return model;
		}
		model.addObject("localpage", page);
		model.addObject("countpage", countpage);
		model.addObject("searchlist", list);
		return model;
	}

	public String List2JsonTypePostPersonal(List<PostPersonal> list) {
		StringBuffer listJson = new StringBuffer("[");
		for (PostPersonal pp : list) {
			listJson.append(pp.toString() + ",");// 重写toString
		}
		listJson = listJson.deleteCharAt(listJson.length() - 1);
		listJson.append("]");
		return listJson.toString();
	}

	@Transactional
	public String isreadById(String id) {
		Boolean result = systemInfoMapper.isreadById(id);
		if (result == true) {
			return "true";
		} else {
			return "false";
		}
	}

	public String existNotRead(HttpSession session) throws InterruptedException {
		Boolean result = systemInfoMapper.existNotRead((String) session.getAttribute("id"));
		if (result == true) {
			return "true";
		} else {
			return "false";
		}
	}
}

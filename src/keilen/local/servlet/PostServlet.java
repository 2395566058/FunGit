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
import keilen.local.mapper.DeletedMapper;
import keilen.local.mapper.ForumMapper;
import keilen.local.mapper.PostFloorMapper;
import keilen.local.mapper.PostPersonalMapper;
import keilen.local.mapper.UserPersonalMapper;
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
			reviewUserid = postFloorMapper.getUseridById(reviewid);
		}
		reviewUserid = postPersonalMapper.getPersonalById(postid).getUserid();
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
			if (!reviewUserid.equals(userid)) {
				String title = postPersonalMapper.getTitleById(postid);
				String object = "您在帖子 " + title + " 中有一条回复：<br>" + content;
				redisCacheServlet.addMessage(reviewUserid, object, "ReviewReview");
			}
			return true;
		}
		return false;

	}

	@Transactional
	public String addPostPersonal(HttpSession session, PostPersonal postPersonal) {
		String id = (String) session.getAttribute("id");
		postPersonal.setUserid(id);
		postPersonal.setForumid(forumMapper.getColumnByArg("forum", "id", "name", postPersonal.getForumid()));
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
		String forumName = forumMapper.getColumnByArg("forum", "name", "id", pp.getForumid());
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
		int floorNum = postFloorMapper.getCountByPostid(id);
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
		Deleted deleteEntity = new Deleted();
		deleteEntity.setDeletetime(NowTimeFormatUtil.getNowTime());
		deleteEntity.setOperationid("0");
		deletedMapper.deleteTable(deleteEntity);
		postPersonalMapper.deletePostPersonalById(deleteEntity.getId(), id);
		return "true";
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
		Deleted deleted = new Deleted();
		deleted.setOperationid("0");
		deleted.setDeletetime(NowTimeFormatUtil.getNowTime());
		deletedMapper.deleteTable(deleted);
		boolean result = postFloorMapper.deletePostReview(postid, floor, deleted.getId());
		if (result) {
			return "true";
		}
		return "false";
	}

	@Transactional
	public String deleteReviewAll(HttpSession session, String postid) {
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

	public String List2JsonTypePostPersonal(List<PostPersonal> list) {
		StringBuffer listJson = new StringBuffer("[");
		for (PostPersonal pp : list) {
			listJson.append(pp.toString() + ",");// 重写toString
		}
		listJson = listJson.deleteCharAt(listJson.length() - 1);
		listJson.append("]");
		return listJson.toString();
	}
}

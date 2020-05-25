package keilen.local.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.PostPersonal;

@Mapper
public interface PostPersonalMapper extends CommonMapper<PostPersonal> {

	public boolean insertOne(PostPersonal postPersonal);

	public PostPersonal getPersonalById(@Param("id") String id);

	public void addclicknumByid(@Param("id") String postid);

	public void addreviewnumByid(@Param("id") String postid);

	public List<PostPersonal> getPersonalsByUseid(@Param("userid") String userid, @Param("page") int page);

	public int getCountbyUserid(@Param("userid") String userid);

	public void deletePostPersonalById(@Param("deletedid") String deletedid, @Param("id") String id);

	public String getTitleById(@Param("id") String id);

	public List<PostPersonal> getHotPostForHome(@Param("num") int num);

	public List<PostPersonal> getNewPostForHome(@Param("num") int num);
	
	public int getCountLikeTitle(@Param("title") String title);
	
	public int getCountLikeUserid(@Param("userid") String userid);
	
	public int getCountByForumid(@Param("id") String id);

	public List<PostPersonal> getListLikeTitle(@Param("title") String title, @Param("num") int num);
	
	public List<PostPersonal> getListByUserid(@Param("userid") String userid, @Param("num") int num);

	public List<PostPersonal> getListLikeUserid(@Param("userid") String userid, @Param("num") int num);

	public List<PostPersonal> getHotListByForumid(@Param("forumid") String forumid, @Param("num") int num);
	
	public List<PostPersonal> getNewListByForumid(@Param("forumid") String forumid, @Param("num") int num);
	
	public List<PostPersonal> getRandowListByForumid(@Param("forumid") String forumid, @Param("num") int num);
}

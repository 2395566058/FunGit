package keilen.local.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.PostPersonal;

@Mapper
public interface PostPersonalMapper {

	public boolean insertOne(PostPersonal postPersonal);

	public PostPersonal getPersonalById(@Param("id") String id);

	public void addclicknumByid(@Param("id") String postid);

	public void addreviewnumByid(@Param("id") String postid);

	public List<PostPersonal> getPersonalsByUseid(@Param("userid") String userid, @Param("page") int page);

	public int getCountbyUserid(@Param("userid") String userid);

	public void deletePostPersonalById(@Param("deletedid") String deletedid, @Param("id") String id);
}

package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.PostPersonal;

@Mapper
public interface PostPersonalMapper {

	public boolean insertOne(PostPersonal postPersonal);

	public PostPersonal getPersonalById(@Param("id") String id);
	
	public void addclicknumByid(@Param("id")String postid);
	
	public void addreviewnumByid(@Param("id")String postid);
	
}

package keilen.local.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import keilen.local.entity.PostFloor;
import keilen.local.util.ShowPostReviewUtil;
import keilen.local.util.ShowPostUtil;

@Mapper
public interface PostFloorMapper {
	public int getCountByPostid(@Param("postid") String postid);

	public boolean insertOne(PostFloor postFloor);

	public int getCountByPostidAndFloor(@Param("postid") String postid, @Param("floor") String floor);

	public List<ShowPostUtil> getFloorsById(@Param("postid") String postid, @Param("head") int head,
			@Param("foot") int foot);

	public ShowPostReviewUtil getFloorById(@Param("reviewid") String id);

	public String getIdByPostidAndFloor(@Param("postid") String postid, @Param("floor") String floor);
	
}

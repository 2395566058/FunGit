package keilen.local.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import keilen.local.entity.PostFloor;
import keilen.local.util.ShowMyPostReviewUtil;
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
	
	public List<ShowMyPostReviewUtil> getMyPostReviewUtil(@Param("userid")String userid,@Param("page")int page);
	
	public int getCountByUserid(@Param("userid")String userid);
	
	public List<ShowPostReviewUtil> getReviewByUseridAndPostId(@Param("userid")String userid,@Param("postid")String postid);
	
	public String getUseNameById(@Param("id")String id);
	
	public boolean deletePostReview(@Param("postid")String postid,@Param("floor")String floor,@Param("deleted")String deleted);
	
	public boolean deletePostReviewAll(@Param("postid")String postid,@Param("userid")String userid,@Param("deleted")String deleted);
	
}

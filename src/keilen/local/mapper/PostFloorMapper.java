package keilen.local.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import keilen.local.entity.PostFloor;
import keilen.local.util.ReviewMeUtil;
import keilen.local.util.ShowMyPostReviewUtil;
import keilen.local.util.ShowPostReviewUtil;
import keilen.local.util.ShowPostUtil;
import keilen.local.util.ShowReviewUtil;

@Mapper
public interface PostFloorMapper extends CommonMapper<PostFloor>{

	public int getCountByPostidAndFloor(@Param("postid") String postid, @Param("floor") String floor);

	public int getCountforReviewUtilByUserid(@Param("userid") String userid);

	public int getCountByUserid(@Param("userid") String userid);

	public String getIdByPostidAndFloor(@Param("postid") String postid, @Param("floor") String floor);

	public String getUseNameById(@Param("id") String id);

	public ShowPostReviewUtil getFloorByid(@Param("reviewid") String id);

	public List<ShowPostUtil> getFloorsById(@Param("postid") String postid, @Param("head") int head,
			@Param("foot") int foot);

	public List<ShowMyPostReviewUtil> getMyPostReviewUtil(@Param("userid") String userid, @Param("page") int page);

	public List<ShowPostReviewUtil> getReviewByUseridAndPostId(@Param("userid") String userid,
			@Param("postid") String postid);

	public List<ReviewMeUtil> getReviewUtilByUserid(@Param("userid") String userid, @Param("page") int page);

	public List<ShowReviewUtil> getMyReviewByPostid(@Param("userid") String userid, @Param("postid") String postid);

	public boolean insertOne(PostFloor postFloor);

	public boolean deletePostReview(@Param("postid") String postid, @Param("floor") String floor,
			@Param("deleted") String deleted);

	public boolean deletePostReviewAll(@Param("postid") String postid, @Param("userid") String userid,
			@Param("deleted") String deleted);

}

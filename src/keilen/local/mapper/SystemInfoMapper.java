package keilen.local.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import keilen.local.entity.SystemInfo;

@Mapper
public interface SystemInfoMapper extends CommonMapper<SystemInfo> {

	public List<SystemInfo> getSystemInfoByUserid(@Param("userid") String userid, @Param("page") int page);

	public Boolean isreadById(@Param("id") String id);

	public Boolean existNotRead(@Param("userid") String userid);

	public Boolean addSystemInfo(@Param("userid") String userid, @Param("title") String title,
			@Param("content") String content, @Param("issuetime") String issuetime, @Param("issueid") String issueid,@Param("content_title") String content_title);
}

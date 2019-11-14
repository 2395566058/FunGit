package keilen.local.mapper;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ForumMapper {
	public List<HashMap<String, Object>> getForumName();
	
	public String getIdByName(@Param("name")String name);
	
	public String getNameById(@Param("id")String id);
	
}

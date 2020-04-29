package keilen.local.mapper;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import keilen.local.entity.Forum;

@Mapper
public interface ForumMapper extends CommonMapper<Forum>{
	public List<HashMap<String, Object>> getForumName();

}
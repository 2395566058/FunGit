package keilen.local.mapper;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Param;

>>>>>>> branch 'master' of https://github.com/2395566058/FunGit.git
import keilen.local.entity.Forum;

@Mapper
public interface ForumMapper extends CommonMapper<Forum>{
	public List<HashMap<String, Object>> getForumName();

}

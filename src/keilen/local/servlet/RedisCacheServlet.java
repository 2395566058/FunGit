package keilen.local.servlet;

import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import keilen.local.mapper.ForumMapper;

@Service
public class RedisCacheServlet {
	private static final Logger log = Logger.getLogger(RedisCacheServlet.class);
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private ForumMapper forumMapper;

	@Scheduled(cron = "0 */5 * * * ?") // 5分钟执行一次
	@Transactional
	public void refreshHotPost() {
		getForum();
	}

	public void getForum() {
		List<HashMap<String, Object>> forumName = forumMapper.getForumName();
		redisTemplate.opsForValue().set("forumName", forumName);
		log.info("redis存入：List-forumName");
	}
}

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
import keilen.local.util.MessageUtil;

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

	public void addMessage(String id, Object object) {
		MessageUtil message = new MessageUtil();
		message.setObject(object);
		message.setStatus(false);
		redisTemplate.opsForValue().set("Message" + id, message);
		log.info("redis存入：MessageUtil-Messageid");
	}

	public MessageUtil getMessage(String id) {
		MessageUtil message = new MessageUtil();
		return (MessageUtil) redisTemplate.opsForValue().get("Message" + id);
	}

	public void addOnline(String id, String sessionid) {
		redisTemplate.opsForValue().set("Online" + id, sessionid);
	}

	public boolean getOnline(String id, String sessionid) {
		String sessionid_2 = (String) redisTemplate.opsForValue().get("Online" + id);
		if (sessionid.equals(sessionid_2)) {
			return true;
		} else {
			return false;
		}
	}
}

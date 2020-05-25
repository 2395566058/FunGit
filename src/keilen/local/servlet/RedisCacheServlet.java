package keilen.local.servlet;

import java.util.ArrayList;
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

	@Transactional
	public void getForum() {
		List<HashMap<String, Object>> forumName = forumMapper.getForumName();
		redisTemplate.opsForValue().set("forumName", forumName);
		log.info("redis存入：List-forumName");
	}

	@Transactional
	public void addMessage(String id, Object object, String type) {
		MessageUtil message = new MessageUtil();
		message.setObject(object);
		message.setType(type);
		ArrayList<MessageUtil> arraylist = (ArrayList) redisTemplate.opsForValue().get("Message" + id);
		if (arraylist == null) {
			arraylist = new ArrayList<MessageUtil>();
		}
		arraylist.add(message);
		redisTemplate.opsForValue().set("Message" + id, arraylist);
		log.info("redis存入：MessageUtil-Message" + id);
	}

	@Transactional
	public String getMessage(String id) {
		ArrayList<MessageUtil> list = (ArrayList<MessageUtil>) redisTemplate.opsForValue().get("Message" + id);
		if (list == null || list.size() == 0) {
			return "";
		}
		StringBuffer result1 = new StringBuffer("[");
		for (MessageUtil value : list) {
			StringBuffer result2 = new StringBuffer("{");
			result2.append("\"type\":\"" + value.getType() + "\",\"object\":\"" + value.getObject().toString() + "\"}");
			result2.append(",");
			result1.append(result2);
		}
		result1 = result1.deleteCharAt(result1.length() - 1);
		result1.append("]");
		log.info("redis获取：MessageUtil-Message" + id + ":" + list.toString());
		return result1.toString();
	}

	@Transactional
	public void addOnline(String id, String sessionid) {
		redisTemplate.opsForValue().set("Online" + id, sessionid);
		log.info("redis存入：sessionid-Online" + id);
	}

	@Transactional
	public boolean getOnline(String id, String sessionid) {
		String sessionid_2 = (String) redisTemplate.opsForValue().get("Online" + id);
		if (sessionid_2 == null) {
			return false;
		}
		if (sessionid.equals(sessionid_2)) {
			return true;
		} else {
			log.info("redis获取：sessionid-Online" + id + ":" + sessionid_2);
			return false;
		}
	}
}

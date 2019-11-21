package keilen.local.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import keilen.local.util.MessageUtil;

@Service
public class LongSessionServlet {
	@Autowired
	private RedisCacheServlet redisCacheServlet;

	public MessageUtil waitMessageAlert(HttpServletRequest request) throws InterruptedException {
		MessageUtil message = new MessageUtil();
		String id = (String) request.getAttribute("id");
		message = redisCacheServlet.getMessage("Message" + id);
		return message;
	}

	public String getOnlineStatus(HttpSession session) {
		boolean result = redisCacheServlet.getOnline((String) session.getAttribute("id"), session.getId());
		if (result) {
			return "1";
		} else {
			session.invalidate();
			return "0";
		}
	}
}

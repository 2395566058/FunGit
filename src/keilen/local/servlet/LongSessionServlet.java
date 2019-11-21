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
		String id = (String) request.getAttribute("id");
		MessageUtil message = redisCacheServlet.getMessage("Message" + id);
		boolean status = false;
		while (!status)
			if (message == null || message.equals("")) {
				Thread.sleep(1500);
			} else {
				status = false;
			}
		return message;
	}

	public String getOnlineStatus(HttpSession session) throws InterruptedException {
		boolean result = redisCacheServlet.getOnline((String) session.getAttribute("id"), session.getId());
		while (result) {
			Thread.sleep(1500);
			result = redisCacheServlet.getOnline((String) session.getAttribute("id"), session.getId());
		}
		return "0";
	}
}

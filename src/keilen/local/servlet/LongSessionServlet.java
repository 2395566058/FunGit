package keilen.local.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LongSessionServlet {
	@Autowired
	private RedisCacheServlet redisCacheServlet;

	public String waitMessageAlert(HttpServletRequest request) throws InterruptedException {
		String id = (String) request.getSession().getAttribute("id");
		String result = redisCacheServlet.getMessage(id);
		boolean status = true;
		int num = 0;
		while (status) {
			if ("".equals(result)) {
				num++;
				if (num == 5) {
					return "false";
				}
				Thread.sleep(1500);
				result = redisCacheServlet.getMessage(id);
			} else {
				status = false;
			}
		}
		return result;
	}

	public String getOnlineStatus(HttpSession session) throws InterruptedException {
		boolean result = redisCacheServlet.getOnline((String) session.getAttribute("id"), session.getId());
		int num = 0;
		while (result) {
			Thread.sleep(1500);
			num++;
			if (num == 5) {
				return "false";
			}
			result = redisCacheServlet.getOnline((String) session.getAttribute("id"), session.getId());
		}
		return "0";
	}

}

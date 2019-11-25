package keilen.local.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import keilen.local.servlet.LongSessionServlet;

@Controller
public class MessageAlertController {
	@Autowired
	private LongSessionServlet longSessionServlet;

	@RequestMapping(value = "/getMessage", method = RequestMethod.POST)
	@ResponseBody
	public String getMessage(HttpServletRequest request) throws InterruptedException {
		return longSessionServlet.waitMessageAlert(request);
	}

	@RequestMapping(value = "/getOnlineStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getOnlineStatus(HttpServletRequest request) throws InterruptedException {
		return (String) longSessionServlet.getOnlineStatus(request.getSession());
	}
}

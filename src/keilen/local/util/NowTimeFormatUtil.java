package keilen.local.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NowTimeFormatUtil {
	private NowTimeFormatUtil() {
	}

	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());
		return date;
	}

	public static String getTimeFormat(String time) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowdate = new Date();
		Date date = df.parse(time);
		long between=(nowdate.getTime()-date.getTime())/1000;
		if(between>31536000) {
			return between/31536000+"年前";
		}
		if(between>2592000) {
			return between/2592000+"个月前";
		}
		if(between>86400) {
			return between/86400+"天前";
		}
		if(between>3600) {
			return between/3600+"小时前";
		}
		if(between>60) {
			return between/60+"分钟前";
		}
		if(between>4) {
			return between+"秒前";
		}
		return "刚刚";
	}

}

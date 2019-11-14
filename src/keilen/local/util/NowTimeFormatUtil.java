package keilen.local.util;

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

}

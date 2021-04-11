package keilen.local.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public class ImageSaveUtil {
	private static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

	private ImageSaveUtil() {
	}

	public static String saveImage(MultipartFile data, HttpServletRequest request) throws Exception {
		String exName = data.getOriginalFilename().substring(data.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID().toString().replaceAll("-", "") + exName;
		String uploadPicPath = null;
		if (isWindows) {
			uploadPicPath = "file:///D:/FunGitData/images/" + fileName;
		} else {
			uploadPicPath = "file:/root/FunGitData/images/" + fileName;
		}
		File file = new File(new URI(uploadPicPath));
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			data.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		String strBackUrl = "images/" + fileName;
		return strBackUrl;
	}
}

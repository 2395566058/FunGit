package keilen.local.util;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewMeUtil implements Serializable {
	private static final long serialVersionUID = 3056730481244142281L;
	private String postid;
	private String title;
	private String count;
	private String lastReviewTime;
}

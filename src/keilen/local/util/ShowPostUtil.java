package keilen.local.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowPostUtil implements Serializable {
	private static final long serialVersionUID = 8119077178816313842L;
	
	private String floor;
	private String content;
	private String issuetime;
	private String useName;
	private String useHead;
	private ShowPostReviewUtil review;
}

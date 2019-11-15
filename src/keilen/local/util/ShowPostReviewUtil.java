package keilen.local.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowPostReviewUtil implements Serializable {
	private static final long serialVersionUID = 2599599267974828918L;
	private String floor;
	private String content;
	private String issuetime;
	private String useName;
	private String reviewName;
}

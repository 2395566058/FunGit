package keilen.local.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostFloor {
	private String id;
	private String postid;
	private String floor;
	private String content;
	private String userid;
	private String issuetime;
	private String reviewid;
}

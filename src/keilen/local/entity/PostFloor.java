package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostFloor implements Serializable {
	private static final long serialVersionUID = -6728088316184718661L;
	private String id;
	private String postid;
	private String floor;
	private String content;
	private String userid;
	private String issuetime;
	private String reviewid;
}

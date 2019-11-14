package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPersonal implements Serializable {
	private static final long serialVersionUID = -4016017046050398071L;

	private String id;
	private String title;
	private String userid;
	private String forumid;
	private String issuetime;
	private String clicknum;
	private String reviewnum;
	private String content;
}

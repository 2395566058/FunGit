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

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("{");
		result.append("\"id\":\"" + this.id + "\",");
		result.append("\"title\":\"" + this.title + "\",");
		result.append("\"userid\":\"" + this.userid + "\",");
		result.append("\"forumid\":\"" + this.forumid + "\",");
		result.append("\"issuetime\":\"" + this.issuetime + "\",");
		result.append("\"clicknum\":\"" + this.clicknum + "\",");
		result.append("\"reviewnum\":\"" + this.reviewnum + "\",");
		result.append("\"content\":\"" + this.content + "\"}");
		return result.toString();
	}
}

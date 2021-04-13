package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemInfo implements Serializable {
	private static final long serialVersionUID = 7892185201573426743L;
	public String id;
	public String userid;
	public String title;
	public String content;
	public String issuetime;
	public String issueid;
	public String isread;
	public String content_title;
}

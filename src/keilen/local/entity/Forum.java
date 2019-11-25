package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Forum implements Serializable {
	private static final long serialVersionUID = -7505515263576046850L;
	private String id;
	private String name;
	private String issuetime;
	private String issueid;
	private String managementid;
}

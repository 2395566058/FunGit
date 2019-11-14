package keilen.local.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPersonal implements Serializable {
	private static final long serialVersionUID = -4003973081642424181L;
	
	private String id;
	private String name;
	private String birthday;
	private String qq;
	private String city;
	private String introduce;
	private String head;
	private String register;
}

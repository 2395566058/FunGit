package keilen.local.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User implements Serializable {
	private static final long serialVersionUID = -4192123872283152504L;
	
	private String id;
	private String email;
	private String username;
	private String password;
	private String name;
}

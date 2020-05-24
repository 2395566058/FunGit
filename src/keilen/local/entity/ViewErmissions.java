package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewErmissions implements Serializable {
	private static final long serialVersionUID = 6280529409066438069L;
	private String id;
	private String birthday;
	private String qq;
	private String city;
	private String register;
	private String email;
	private String post;
}

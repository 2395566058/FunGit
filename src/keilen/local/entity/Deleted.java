package keilen.local.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Deleted implements Serializable {
	private static final long serialVersionUID = 8120592834725302111L;
	private String id;
	private String operationid;
	private String deletetime;
}

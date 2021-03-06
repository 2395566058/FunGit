package keilen.local.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageUtil implements Serializable {
	private static final long serialVersionUID = -4380581936003098413L;

	public MessageUtil() {
		this.object = null;
	}

	private String type;
	private Object object;
}

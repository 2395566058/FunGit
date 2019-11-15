package keilen.local.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowMyPostReviewUtil implements Serializable{
	private static final long serialVersionUID = 6379191977502469020L;
	private String id;
	private String title;
	private String count;
	private String name;
	private String issuetime;

}

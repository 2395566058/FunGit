package keilen.local.util;

import java.io.Serializable;
import java.util.List;

import keilen.local.entity.PostFloor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShowReviewUtil implements Serializable {
	private static final long serialVersionUID = 6668120991469657180L;
	private String floor;
	private String content;
	private String issuetime;
	private List<PostFloor> review;
}

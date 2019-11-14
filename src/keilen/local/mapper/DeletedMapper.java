package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import keilen.local.entity.Deleted;

@Mapper
public interface DeletedMapper {
	public void deletePostPersonal(Deleted deleted);
}

package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.UserPersonal;

@Mapper
public interface UserPersonalMapper extends CommonMapper<UserPersonal>{

	public boolean insertUserPersonal(UserPersonal userPersonal);

	public UserPersonal getUserPersonalByName(@Param("name") String name);

	public boolean updateUserPersonal(UserPersonal userPersonal);

	public boolean updateHeadById(@Param("head") String head, @Param("id") String id);
}

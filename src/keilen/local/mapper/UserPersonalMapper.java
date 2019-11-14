package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.UserPersonal;

@Mapper
public interface UserPersonalMapper {

	public boolean insertUserPersonal(UserPersonal userPersonal);

	public String getHeadByName(@Param("name") String name);

	public String getHeadById(@Param("id") String id);
	
	public String getNameById(@Param("id") String id);

	public UserPersonal getUserPersonalByName(@Param("name") String name);

	public boolean existName(@Param("name") String name);

	public boolean updateUserPersonal(UserPersonal userPersonal);

	public boolean updateHeadById(@Param("head") String head, @Param("id") String id);
}

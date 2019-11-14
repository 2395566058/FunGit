package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.User;

@Mapper
public interface UserMapper {
	public boolean existUsername(@Param("username") String username);

	public boolean existEmail(@Param("email") String email);

	public void insertUser(User user);

	public boolean loginByUsername(@Param("username") String username, @Param("password") String password);

	public boolean loginByEmail(@Param("email") String email, @Param("password") String password);

	public User getUserByUsername(@Param("username") String username);

	public User getUserByEmail(@Param("email") String email);

	public boolean updateNameById(@Param("name") String name, @Param("id") String id);

	public String getPasswordById(@Param("id") String id);

	public boolean updatePasswordById(@Param("password") String password, @Param("id") String id);

	public boolean updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

}

package keilen.local.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CommonMapper<T> {

	@Select(value = "SELECT * FROM ${table} WHERE id=#{id}")
	public T getOneById(//
			@Param("table") String table, //
			@Param("id") String id);//

	@Select(value = "SELECT * FROM ${table} WHERE ${argName}=#{argValue}")
	public List<T> getListByArg(//
			@Param("table") String table, //
			@Param("argName") String argName, //
			@Param("argValue") String argValue);//

	@Select(value = "SELECT ${column} FROM ${table} WHERE ${argName}=#{argValue}")
	public String getColumnByArg(//
			@Param("table") String table, //
			@Param("column") String column, //
			@Param("argName") String argName, //
			@Param("argValue") String argValue);

	@Select(value = "DELETE FROM ${table} WHERE id = #{id}")
	public boolean deleteOneById(//
			@Param("table") String table, //
			@Param("id") String id);//

	@Select(value = "SELECT COUNT(*) FROM ${table} where ${column}=#{args}")
	public boolean existArgsByCloumn(//
			@Param("table") String table, //
			@Param("args") String args, //
			@Param("column") String column);
}

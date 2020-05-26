package keilen.local.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import keilen.local.entity.ViewErmissions;

@Mapper
public interface ViewErmissionsMapper extends CommonMapper<ViewErmissions> {
	
	public boolean updateOne(@Param("id") String id, @Param("column") String column,@Param("columnValue") String columnValue);
	
	public boolean insertOne(@Param("id")String id);

}

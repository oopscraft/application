package net.oopscraft.application.core.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class YesNoBooleanTypeHandler extends BaseTypeHandler<Boolean> {
	
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute == true ? "Y" : "N";
	}

	public Boolean convertToEntityAttribute(String dbData) {
		return "Y".equals(dbData) ? true : false;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, convertToDatabaseColumn(parameter));
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return convertToEntityAttribute(rs.getString(columnName));
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return convertToEntityAttribute(rs.getString(columnIndex));
	}

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return convertToEntityAttribute(cs.getString(columnIndex));
	}

}

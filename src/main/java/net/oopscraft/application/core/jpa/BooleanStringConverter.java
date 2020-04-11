package net.oopscraft.application.core.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanStringConverter implements AttributeConverter<Boolean, String> {
	
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute == true ? "Y" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "Y".equals(dbData) ? true : false;
	}


}

package net.oopscraft.application.core;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ValueMap extends LinkedHashMap<String,Object> {
	
	private static final long serialVersionUID = 1L;
	
	public ValueMap(){
		super();
	}
	
	public ValueMap(Map<String,Object> map) {
		super();
		super.putAll(map);		
	}
		
	public void set(String name, Object value) {
		super.put(name, value);
	}
	
	public Object get(String name) {
		return super.get(name);
	}
	
	public void setString(String name, Object value) {
		if(value instanceof String) {
			this.set(name, value);
		}else{
			try {
				this.set(name, value.toString());
			}catch(Exception e){
				this.set(name, "");
			}
		}
	}

	public String getString(String name) {
		Object value = this.get(name);
		if(value instanceof String) {
			return (String)value;
		}else{
			try {
				return value.toString();
			}catch(Exception e){
				return "";
			}
		}
	}
	
	public void setNumber(String name, Object value) {
		if(value instanceof BigDecimal) {
			this.set(name, value);
		}else{
			try {
				this.set(name, new BigDecimal(value.toString()));
			}catch(Exception e){
				this.set(name, BigDecimal.ZERO);
			}
		}
	}
	
	public BigDecimal getNumber(String name) {
		Object value = this.get(name);
		if(value instanceof BigDecimal) {
			return (BigDecimal)value;
		}else{
			try {
				return new BigDecimal(value.toString());
			}catch(Exception e){
				return BigDecimal.ZERO;
			}
		}
	}
	
	public void setDate(String name, Object value) {
		if(value instanceof Date) {
			this.set(name, value);
		}else{
			try {
				this.set(name, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(value.toString()));
			}catch(Exception e){
				Calendar c = Calendar.getInstance();
				c.set(0, 0, 0, 0, 0, 0);
				this.set(name, c.getTime());
			}
		}
	}
	
	public Date getDate(String name) throws Exception {
		Object value = this.get(name);
		if(value instanceof Date) {
			return (Date) value;
		}else{
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(value.toString());
			}catch(Exception e){
				Calendar c = Calendar.getInstance();
				c.set(0, 0, 0, 0, 0, 0);
				return c.getTime();
			}
		}
	}
	
	public void setTimestamp(String name, Object value) {
		if(value instanceof Timestamp) {
			this.set(name, value);
		}else{
			try {
				this.set(name, Timestamp.valueOf(value.toString()));
			}catch(Exception e){
				this.set(name, Timestamp.valueOf("0001-01-01 00:00:00.0"));
			}
		}
	}
	
	public Timestamp getTimestamp(String name) {
		Object value = this.get(name);
		if(value instanceof Timestamp) {
			return (Timestamp)value;
		}else{
			try {
				return Timestamp.valueOf(value.toString());
			}catch(Exception e){
				return Timestamp.valueOf("0001-01-01 00:00:00.0");
			}
		}
	}
	
	
}

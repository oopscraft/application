package net.oopscraft.application.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectUtils {
	
	/**
	 * Gets all field names recursively in super class.
	 * @param type
	 * @return
	 */
	public static Field[] getFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()-1]);
    }
	
	/**
	 * Gets field value recursively in super class.
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object fieldValue = null;
	    Class<?> currentClass = obj.getClass();
	    do {
	       try {
	    	   try {
		           Field field = currentClass.getDeclaredField(fieldName);
		           fieldValue = field.get(obj);
	    	   }catch(Exception e) {
	    		   Method getterMethod = currentClass.getDeclaredMethod("get" + StringUtils.toPascalCase(fieldName));
	    		   fieldValue = getterMethod.invoke(obj);
	    	   }
	    	   break;
	       } catch(Exception e) { 
	    	   fieldValue = e.getMessage();
	       }
	    } while((currentClass = currentClass.getSuperclass()) != null);
	    return fieldValue;
	}

}

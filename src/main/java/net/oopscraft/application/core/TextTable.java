package net.oopscraft.application.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


public class TextTable {
	
	Object obj;
	Collection<?> collection;
	
	public TextTable(Object obj) {
		this.obj = obj;
	}
	
	public TextTable(Collection<?> collection) {
		this.collection = collection;
	}
	
	@Override
    public String toString() {
		if(obj != null) {
			return build(obj);
		}else if(collection != null) {
			return build(collection);
		}else {
			return null;
		}
    }

	/**
	 * build
	 * @param columnNames
	 * @param columnValues
	 * @return
	 */
	private static String build(String[] columnNames, String[] columnValues) {
		
		StringBuffer buffer = new StringBuffer();
		
		// Defines max / min length of value.
		int maxKeySize = 0;
		int maxValSize = 0;
		for(int i = 0, iSize = columnNames.length; i < iSize; i ++ ) {
			String columnName = columnNames[i];
			String columnValue = (columnValues[i] == null ? "null" : columnValues[i]);
			columnValue = columnValue.replaceAll("\t", " ");
			if(columnName.length() > maxKeySize) {
				maxKeySize = columnName.length();
			}
			// line separator
			String[] columnValueLines = columnValue.split("\n");
			if(columnValueLines.length > 0) {
				for(int ii = 0, iiSize = columnValueLines.length; ii < iiSize; ii ++ ) {
					if(columnValueLines[ii].length() > maxValSize) {
						maxValSize = columnValueLines[ii].length();
					}
				}
			}else{
				if(columnValue.length() > maxValSize) {
					maxValSize = columnValue.length();
				}
			}
		}
		
		// Prints Text Table
		for(int i = 0, iSize = columnNames.length; i < iSize; i ++ ) {
			if(i == 0) {
				buffer.append( String.format("\n+ %1$-" + maxKeySize + "s + %1$-" + (maxValSize + 20) + "s +", " ").replaceAll(" ", "-"));
				buffer.append( String.format("\n| %1$-" + maxKeySize + "s | %2$-" + (maxValSize + 20) + "s |", "KEY", "VALUE" ));
				buffer.append( String.format("\n+ %1$-" + maxKeySize + "s + %1$-" + (maxValSize + 20) + "s +", " ").replaceAll(" ", "-"));
			}
			String columnName = columnNames[i].replaceAll("\n", "");
			String columnValue = (columnValues[i] == null ? "null" : columnValues[i]);
			columnValue = columnValue.replaceAll("\t", " ");
			// line separator
			String[] columnValueLines = columnValue.split("\n");
			if(columnValueLines.length > 0) {
				for(int ii = 0, iiSize = columnValueLines.length; ii < iiSize; ii ++ ) {
					buffer.append( String.format("\n| %1$-" + maxKeySize + "s | %2$-" + (maxValSize + 20) + "s |", (ii == 0 ? columnName : ""), columnValueLines[ii]));
				}
			}else{
				buffer.append( String.format("\n| %1$-" + maxKeySize + "s | %2$-" + (maxValSize + 20) + "s |", columnName, columnValue));
			}
			buffer.append( String.format("\n+ %1$-" + maxKeySize + "s + %1$-" + (maxValSize + 20) + "s +", " ").replaceAll(" ", "-"));
		}
		
		// Returns
		return buffer.toString();
	}
	
	/**
	 * build
	 * @param headerNames
	 * @param cellValues
	 * @return
	 */
	private static String build(String[] headerNames, String[][] cellValues) {
	
		// DEFINES WIDTH
		int[] maxColumnLen = new int[headerNames.length];
		for(int i = 0, iSize = headerNames.length; i < iSize; i ++ ) {
			if(headerNames[i].length() > maxColumnLen[i])
				maxColumnLen[i] = headerNames[i].length();
		}
		for(int i = 0, iSize = cellValues.length; i < iSize; i ++ ) {
			for(int j = 0, iiSize = cellValues[i].length; j < iiSize; j ++ ){
				if((cellValues[i][j] == null ? 0 : cellValues[i][j].length()) > maxColumnLen[j])	
					maxColumnLen[j] = cellValues[i][j].length();
			}
		}
		
		// CREATE TEXT TABLE
		StringBuffer textTable = new StringBuffer();
		for(int i = 0, iSize = cellValues.length; i < iSize; i ++ ){
			
			// HEADER
			if(i == 0) {
				StringBuffer head01 = new StringBuffer();
				StringBuffer head02 = new StringBuffer();
				StringBuffer head03 = new StringBuffer();
				for(int j = 0, jSize = headerNames.length; j < jSize; j ++ ) {
					String columnName = headerNames[j];
					head01.append( String.format(" %1$-" + maxColumnLen[j] + "s +", " ").replaceAll(" ", "-") );
					head02.append( String.format(" %1$-" + maxColumnLen[j] + "s |", columnName) );
					head03.append( String.format(" %1$-" + maxColumnLen[j] + "s +", " ").replaceAll(" ", "-") );
				}
				textTable.append("\n+").append(head01.toString());
				textTable.append("\n|").append(head02.toString());
				textTable.append("\n+").append(head03.toString());
			}
			
			// RECORD
			StringBuffer cell01 = new StringBuffer();
			StringBuffer cell02 = new StringBuffer();
			for(int j = 0, iiSize = cellValues[i].length; j < iiSize; j ++ ){
				cell01.append( String.format(" %1$-" + maxColumnLen[j] + "s |", cellValues[i][j]) );
				cell02.append( String.format(" %1$-" + maxColumnLen[j] + "s +", " ").replaceAll(" ", "-") );
			}
	
			textTable.append("\n|").append(cell01.toString());
			textTable.append("\n+").append(cell02.toString());
		}
		
		return textTable.toString();
	}
	
	/**
	 * parseColumnNames
	 * @param obj
	 * @return
	 */
	private static String[] parseColumnNames(Object obj) {
		String[] columnNames = null;
		if(obj instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>)obj;
			columnNames = map.keySet().toArray(new String[map.keySet().size()]);
		}else {
			Class<?> objClass = obj.getClass();
			Vector<String> columnNamesVector = new Vector<String>();
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field : fields) {
				String fieldName = field.getName();
				try {
					field.get(obj);
					columnNamesVector.add(fieldName);
				}catch(Exception e) {
					try {
						Method getterMethod = objClass.getMethod("get" + NotationUtils.toPascalCase(fieldName));
						getterMethod.invoke(obj);
						columnNamesVector.add(fieldName);
					}catch(Exception ignore) {}
				}
			}
			columnNames = columnNamesVector.toArray(new String[columnNamesVector.size()]);
		}
		return columnNames;
	}
	
	/**
	 * parseColumnValues
	 * @param obj
	 * @param columnNames
	 * @return
	 */
	private static String[] parseColumnValues(Object obj, String[] columnNames) {
		String[] columnValues = new String[columnNames.length];
		if(obj instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>)obj;
			columnValues = new String[columnNames.length];
			for(int i = 0, iSize = columnNames.length; i < iSize; i ++ ) {
				columnValues[i] = String.valueOf(map.get(columnNames[i]));
			}
		}else {
			Class<?> objClass = obj.getClass();
			columnValues = new String[columnNames.length];
			for(int i = 0, iSize = columnNames.length; i < iSize; i ++ ) {
				String columnName = columnNames[i];
				Object columnValue = null;
				try {
					Field field = objClass.getField(columnName);
					columnValue = field.get(obj);
				}catch(Exception e) {
					try {
						Method getterMethod = objClass.getMethod("get" + NotationUtils.toPascalCase(columnName));
						columnValue = getterMethod.invoke(obj);
					}catch(Exception e1) {
						columnValue = e1.getMessage();
					}
				}
				columnValues[i] = columnValue == null ? null : columnValue.toString();
			}
		}
		return columnValues;
	}
	
	private static String build(Object obj) {
		StringBuffer buffer = new StringBuffer();
		
		// Checks empty
		if(obj == null) {
			buffer.append("\n-- EMPTY DATA --");
			return buffer.toString();
		}
		
		String[] columnNames = parseColumnNames(obj);
		String[] columnValues = parseColumnValues(obj,columnNames);
		buffer.append(build(columnNames, columnValues));
		return buffer.toString();
	}
	
	
	private static String build(Collection<?> collection) {
		StringBuffer buffer = new StringBuffer();
		
		// Appends data
		if(collection == null || collection.size() < 1) {
			buffer.append("\n-- EMPTY DATA --");
			return buffer.toString();
		}
		String[] columnNames = parseColumnNames(collection.iterator().next());
		String[][] columnValuesList = new String[collection.size()][];
		Iterator<?> iter = collection.iterator();
		int idx = -1;
		while(iter.hasNext()) {
			idx ++;
			columnValuesList[idx] = parseColumnValues(iter.next(), columnNames);
		}
		
		buffer.append(build(columnNames, columnValuesList));
		return buffer.toString();
	}
	
}

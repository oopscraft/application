package net.oopscraft.application.core.mybatis;

import org.apache.ibatis.session.RowBounds;

public class PageRowBounds extends RowBounds {
	
	int offset = 0;
	int limit = Integer.MAX_VALUE;
	int sqlOffset = 0;
	int sqlLimit = Integer.MAX_VALUE;
	boolean enableTotalCount = false;
	int totalCount = -1;
	
	public PageRowBounds(int offset, int limit, boolean enableTotalCount) {
		this.offset = offset;
		this.limit = limit;
		this.enableTotalCount = enableTotalCount;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setSqlOffset(int sqlOffset) {
		this.sqlOffset = sqlOffset;
	}
	
	public int getSqlOffset() {
		return sqlOffset;
	}
	
	public void setSqlLimit(int sqlLimit) {
		this.sqlLimit = sqlLimit;
	}
	
	public int getSqlLimit() {
		return sqlLimit;
	}

	public boolean isEnableTotalCount() {
		return enableTotalCount;
	}
	
	public void enableTotalCount(boolean enableTotalCount) {
		this.enableTotalCount = enableTotalCount;
	}
	
	public boolean enableTotalCount() {
		return this.enableTotalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public long getTotalCount() {
		return this.totalCount;
	}

}

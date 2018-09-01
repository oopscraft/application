package net.oopscraft.application.core.mybatis;

import org.apache.ibatis.session.RowBounds;

public class Pageable extends RowBounds {
	
	int rows = Integer.MAX_VALUE;
	int page = 1;
	int offset = 0;
	int limit = Integer.MAX_VALUE;
	int sqlOffset = 0;
	int sqlLimit = Integer.MAX_VALUE;
	boolean enableTotalCount = false;
	int totalCount = -1;
	
	public Pageable(int rows, int page) {
		this.rows = rows;
		this.page = page;
		this.offset = (this.rows*this.page) - this.rows;
		this.limit = rows;
		this.sqlOffset = (this.rows*this.page) - this.rows;
		this.sqlLimit = rows;
	}
	
	public int getPage() {
		return this.page;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	public int getSqlOffset() {
		return (this.rows*this.page) - this.rows;
	}
	
	public int getSqlLimit() {
		return this.rows;
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
	
	/**
	 * Return content range value.
	 * rows, page 기준으로 계산한다.(실제 offset, limit는 SQL로 조회되는 경우 interceptor에서 변경될수 있음)
	 * @return
	 */
	public String toContentRange() {
		StringBuffer contentRange = new StringBuffer();
		
		// start
		int start = (this.rows*this.page) - this.rows + 1;
		contentRange.append(start);
		
		// end
		contentRange.append("-");
		int end = start -1 + this.rows;
		if(end < this.getTotalCount()) {
			contentRange.append(end);
		}else {
			contentRange.append(this.getTotalCount());
		}
		
		// size
		contentRange.append("/");
		if(this.enableTotalCount() == true) {
			contentRange.append(this.getTotalCount());
		}else {
			contentRange.append("*");
		}
		return contentRange.toString();
	}

}

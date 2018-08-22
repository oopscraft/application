package net.oopscraft.application.core;

import org.apache.ibatis.session.RowBounds;

public class Pageable extends RowBounds {
	
	int rows = Integer.MAX_VALUE;
	int page = 1;
	int totalRows = -1;
	boolean enableTotalRows = false;
	
	public Pageable(int rows, int page) {
		this.rows = rows;
		this.page = page;
	}
	
	public int getLimit() {
		return this.rows;
	}
	
	public int getOffset() {
		int offset = (this.rows*this.page) - this.rows;
		return offset;
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void enableTotalRows(boolean enableTotalRows) {
		this.enableTotalRows = enableTotalRows;
	}
	
	public boolean enableTotalRows() {
		return this.enableTotalRows;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

}

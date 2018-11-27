package net.oopscraft.application.core;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageInfo {

	int page = 1;
	int rows = Integer.MAX_VALUE;
	boolean enableTotalCount = false;
	long totalCount = -1;

	public PageInfo(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}

	public PageInfo(int page, int rows, boolean enableTotalCount) {
		this(page, rows);
		this.enableTotalCount = enableTotalCount;
	}

	public int getPage() {
		return page;
	}

	public int getRows() {
		return rows;
	}

	public int getOffset() {
		return (rows * page) - this.rows;
	}

	public int getLimit() {
		return rows;
	}

	public boolean isEnableTotalCount() {
		return enableTotalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public Pageable toPageable() {
		return new PageRequest(page - 1, rows);
	}

	public RowBounds toRowBounds() {
		return new RowBounds(getOffset(), getLimit());
	}

	/**
	 * Gets Content-Range value
	 * 
	 * @param response
	 */
	public String getContentRange() {
		StringBuffer contentRange = new StringBuffer();
		contentRange.append("items");
		contentRange.append(" ");
		contentRange.append(getOffset() + 1).append("-").append(getOffset() + getLimit());
		contentRange.append("/");
		contentRange.append(isEnableTotalCount() ? getTotalCount() : "*");
		return contentRange.toString();
	}

}

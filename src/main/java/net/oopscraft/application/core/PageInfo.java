package net.oopscraft.application.core;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageInfo {

	int page = 1;
	int size = Integer.MAX_VALUE;
	boolean enableTotalCount = false;
	long totalCount = -1;

	public PageInfo(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public PageInfo(int page, int size, boolean enableTotalCount) {
		this(page, size);
		this.enableTotalCount = enableTotalCount;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public int getOffset() {
		return (size*page) - this.size;
	}

	public int getLimit() {
		return size;
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
		return new PageRequest(page - 1, size);
	}
	
	public RowBounds toRowBounds() {
		return new RowBounds(getOffset(), getLimit());
	}

}

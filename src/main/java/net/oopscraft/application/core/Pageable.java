public class Pageable extends RowBounds {
	
	int rows = Integer.MAX_VALUE;
	int page = 1;
	
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

}

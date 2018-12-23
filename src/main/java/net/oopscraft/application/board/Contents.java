package net.oopscraft.application.board;

import java.io.Serializable;

public class Contents {

	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String boardId, long no) {
			this.boardId = boardId;
			this.no = no;
		}
		String boardId;
		long no;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getBoardId().equals(pk.getBoardId())
				&& this.getNo() == pk.getNo()
				) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}	
		
		@Override
		public int hashCode() {
			return (boardId + Long.toString(no)).hashCode();
		}
		
		public String getBoardId() {
			return boardId;
		}
		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}
		public long getNo() {
			return no;
		}
		public void setNo(long no) {
			this.no = no;
		}
	}
	
}

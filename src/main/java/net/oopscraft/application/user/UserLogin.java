package net.oopscraft.application.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_USER_LOGN_HIST")
@IdClass(UserLogin.Pk.class)
public class UserLogin {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String userId, Date datetime) {
			this.userId = userId;
			this.datetime = datetime;
		}
		String userId;
		Date datetime;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getUserId().equals(pk.getUserId())
				&& this.getDatetime().equals(pk.getDatetime())
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
			return (userId + Long.toString(datetime.getTime())).hashCode();
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Date getDatetime() {
			return datetime;
		}
		public void setDatetime(Date datetime) {
			this.datetime = datetime;
		}
	}
	
	@Id
	@Column(name = "USER_ID")
	String userId;

	@Id
	@Column(name = "LOGN_DTTM")
	Date datetime;
	
	@Column(name = "LOGN_SUCS_YN")
	String successYn;
	
	@Column(name = "LOGN_FAIL_RESN")
	String failReason;
	
	@Column(name = "LOGN_IP")
	String ip;

	@Column(name = "LOGN_AGNT")
	String agent;
	
	@Column(name = "LOGN_REFR")
	String referer;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getSuccessYn() {
		return successYn;
	}

	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

}

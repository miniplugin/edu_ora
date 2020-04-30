package org.edu.vo;

import java.util.Date;

/*
 * ...128p.
	create table TBL_MEMBER
	(
		 user_id	varchar(50)	not null
	    ,user_pw	varchar(255) not null
	    ,user_name	varchar(50)	not null
	    ,email		varchar(100)
	    ,point		int(11) NOT NULL DEFAULT 0
	    ,enabled	boolean NOT NULL DEFAULT 0
	    ,levels		varchar(50) NOT NULL DEFAULT 'ROLE_USER'
		,reg_date 	TIMESTAMP NOT NULL DEFAULT now()
		,update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP    
	    ,primary key(user_id)
	);
 */
public class MemberVO {

	private String user_id;
	private String user_pw;
	private String user_name;
	private String email;
	private Integer point;
	private Boolean enabled;
	private String levels;
	private Date reg_date;
	private Date update_date;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getReg_date() {
		return reg_date;
	}
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	@Override
	public String toString() {
		return "XMemberVO [user_id=" + user_id 
					  + ", user_pw=" + user_pw 
					  + ", user_name=" + user_name 
					  + ", email=" + email 
					  + ", point=" + point
					  + ", enabled=" + enabled
					  + ", levels=" + levels
					  + ", reg_date=" + reg_date 
					  + ", update_date=" + update_date + "]";
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
		
	
	
	
}



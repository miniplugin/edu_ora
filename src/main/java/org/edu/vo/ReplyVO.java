package org.edu.vo;

import java.util.Date;
/***
 * create table TBL_REPLY
(
	rno	int not null auto_increment
	,bno int not null default 0
    ,replytext	varchar(1000) not null
    ,replyer	varchar(50) not null
    ,regdate	timestamp not null	default	now()
    ,update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ,primary key(rno)
);
alter table tbl_reply add constraint fk_board
foreign key (bno) references tbl_board(bno);
update tbl_board set reply_count = (select count(rno) from tbl_reply where bno=tbl_board.bno) where bno > 0;
 * @author Administrator
 *
 */
public class ReplyVO {

	private Integer rno;
	private Integer bno;
	private String replytext;
	private String replyer;

	private Date regdate;
	private Date update_date;

	public Integer getRno() {
		return rno;
	}

	public void setRno(Integer rno) {
		this.rno = rno;
	}

	public Integer getBno() {
		return bno;
	}

	public void setBno(Integer bno) {
		this.bno = bno;
	}

	public String getReplytext() {
		return replytext;
	}

	public void setReplytext(String replytext) {
		this.replytext = replytext;
	}

	public String getReplyer() {
		return replyer;
	}

	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	@Override
	public String toString() {
		return "ReplyVO [rno=" + rno + ", bno=" + bno 
					           + ", replytext=" + replytext 
					           + ", replyer=" + replyer
					           + ", regdate=" + regdate 
					           + ", update_date=" + update_date + "]";
	}

}
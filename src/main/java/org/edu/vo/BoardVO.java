package org.edu.vo;

import java.util.Arrays;
import java.util.Date;
/***
 * create table TBL_BOARD
(
	bno	int not null auto_increment
    ,title	varchar(200) not null
    ,content text null
    ,writer	varchar(50) not null
    ,regdate	timestamp not null	default	now()
    ,update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ,view_count	int default 0
    ,primary key(bno)
);
댓글카운트 트랜잭션 기능으로 추가
alter table tbl_board drop column replycnt;
alter table tbl_board add column reply_count int default 0;
페이지넘버를 표시하기 위해 충분한 양의 데이터 넣기
insert into tbl_board (title,content,writer) (select title,content,writer from tbl_board);
첨부파일 기능으로 추가
CREATE table tbl_attach (
    full_name varchar(150) not null
    ,bno int not null
    ,regdate timestamp DEFAULT now()
    ,PRIMARY KEY(full_name)
    );
ALTER table tbl_attach add CONSTRAINT fk_board_attach
FOREIGN KEY (bno) REFERENCES tbl_board(bno);
 * @author Administrator
 *
 */
public class BoardVO {

	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date update_date;
	private int view_count;
	private int reply_count; //...added since 502p.
	
	private String[] files;
	
	public Integer getBno() {
		return bno;
	}
	public void setBno(Integer bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	@Override
	public String toString() {
		return "BoardVO [bno=" + bno + ", title=" + title 
				+ ", content=" + content + ", writer=" + writer 
				+ ", regdate=" + regdate + ", update_date=" + update_date
				+ ", view_count=" + view_count + ", files="
				+ Arrays.toString(files) + "]";
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public String[] getFiles() {
		return files;
	}
	public void setFiles(String[] files) {
		this.files = files;
	}
	
}

package com.example.demo.templateCreate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="codemst")  /* テーブル名を定義する */
@IdClass(CodeMstKey.class)
public class CodeMstDto {

	@Id
	private int kbn1;

	@Id
	private int kbn2;

	@Id
	private int id;

	private String name;

	private String comment;

	public int getKbn1() {
		return kbn1;
	}

	public void setKbn1(int kbn1) {
		this.kbn1 = kbn1;
	}

	public int getKbn2() {
		return kbn2;
	}

	public void setKbn2(int kbn2) {
		this.kbn2 = kbn2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


}

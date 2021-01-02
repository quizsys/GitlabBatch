package com.example.demo;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="summary")  /* テーブル名を定義する */
public class SummaryDto {

	@Id
	private LocalDate date;

	private int opened;

	private int todo;

	private int doing;

	private int done;

	private int closed;


	public SummaryDto() {}


	public SummaryDto(LocalDate date, int opened, int todo, int doing, int done, int closed) {
		this.date = date;
		this.opened = opened;
		this.todo = todo;
		this.doing = doing;
		this.done = done;
		this.closed = closed;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public int getOpened() {
		return opened;
	}


	public void setOpened(int opened) {
		this.opened = opened;
	}


	public int getTodo() {
		return todo;
	}


	public void setTodo(int todo) {
		this.todo = todo;
	}


	public int getDoing() {
		return doing;
	}


	public void setDoing(int doing) {
		this.doing = doing;
	}


	public int getDone() {
		return done;
	}


	public void setDone(int done) {
		this.done = done;
	}


	public int getClosed() {
		return closed;
	}


	public void setClosed(int closed) {
		this.closed = closed;
	}

}

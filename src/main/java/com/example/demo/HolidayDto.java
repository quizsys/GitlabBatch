package com.example.demo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="holiday")  /* テーブル名を定義する */
public class HolidayDto {

	@Id
	private LocalDate date;

	private String name;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}




}

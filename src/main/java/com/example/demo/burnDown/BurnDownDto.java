package com.example.demo.burnDown;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="burndown")  /* テーブル名を定義する */
@IdClass(BurnDownKey.class)
public class BurnDownDto {

	@Id
	private String milestone;

	@Id
	private LocalDate date;

	@Id
	private String label;

	private int allIssueCount;

	private int compIssueCount;

	private int totalTimeSpent;

	private int timeEstimate;

	private int totalTimeSpentMergeRequest;

	private int compTimeEstimate;

	private int uncompTimeSpent;

	public BurnDownDto() {}


	public BurnDownDto(String milestone, LocalDate date, String label) {
		this.milestone = milestone;
		this.date = date;
		this.label = label;

		this.allIssueCount = 0;
		this.compIssueCount = 0;
		this.totalTimeSpent = 0;
		this.timeEstimate = 0;
		this.totalTimeSpentMergeRequest = 0;
		this.compTimeEstimate = 0;
		this.uncompTimeSpent = 0;
	}

	@Override
	public String toString() {
		return "BurnDownDto [milestone=" + milestone + ", date=" + date + ", label=" + label + ", allIssueCount="
				+ allIssueCount + ", compIssueCount=" + compIssueCount + ", totalTimeSpent=" + totalTimeSpent
				+ ", timeEstimate=" + timeEstimate + ", totalTimeSpentMergeRequest=" + totalTimeSpentMergeRequest
				+ ", compTimeEstimate=" + compTimeEstimate + ", uncompTimeSpent=" + uncompTimeSpent + "]";
	}


	public String getMilestone() {
		return milestone;
	}


	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public int getAllIssueCount() {
		return allIssueCount;
	}


	public void setAllIssueCount(int allIssueCount) {
		this.allIssueCount = allIssueCount;
	}


	public int getCompIssueCount() {
		return compIssueCount;
	}


	public void setCompIssueCount(int compIssueCount) {
		this.compIssueCount = compIssueCount;
	}


	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}


	public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}


	public int getTimeEstimate() {
		return timeEstimate;
	}


	public void setTimeEstimate(int timeEstimate) {
		this.timeEstimate = timeEstimate;
	}


	public int getTotalTimeSpentMergeRequest() {
		return totalTimeSpentMergeRequest;
	}


	public void setTotalTimeSpentMergeRequest(int totalTimeSpentMergeRequest) {
		this.totalTimeSpentMergeRequest = totalTimeSpentMergeRequest;
	}


	public int getCompTimeEstimate() {
		return compTimeEstimate;
	}


	public void setCompTimeEstimate(int compTimeEstimate) {
		this.compTimeEstimate = compTimeEstimate;
	}


	public int getUncompTimeSpent() {
		return uncompTimeSpent;
	}


	public void setUncompTimeSpent(int uncompTimeSpent) {
		this.uncompTimeSpent = uncompTimeSpent;
	}



}

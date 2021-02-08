package com.example.demo.templateCreate;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="templatecreate")  /* テーブル名を定義する */
public class TemplateCreateDto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int projectId;

	private String templateName;

	private String issueTitle;

	private String labels;

	private int createTerms;

	private int createTermsDetail;

	private int issueDate;

	private int issueDateDetail;

	private LocalDate nextCreateDate;

	private LocalDate beforeCreateDate;

	private boolean beforeSuccessFlg;

	private double estimateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public int getCreateTerms() {
		return createTerms;
	}

	public void setCreateTerms(int createTerms) {
		this.createTerms = createTerms;
	}

	public int getCreateTermsDetail() {
		return createTermsDetail;
	}

	public void setCreateTermsDetail(int createTermsDetail) {
		this.createTermsDetail = createTermsDetail;
	}

	public int getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(int issueDate) {
		this.issueDate = issueDate;
	}

	public int getIssueDateDetail() {
		return issueDateDetail;
	}

	public void setIssueDateDetail(int issueDateDetail) {
		this.issueDateDetail = issueDateDetail;
	}

	public LocalDate getNextCreateDate() {
		return nextCreateDate;
	}

	public void setNextCreateDate(LocalDate nextCreateDate) {
		this.nextCreateDate = nextCreateDate;
	}

	public LocalDate getBeforeCreateDate() {
		return beforeCreateDate;
	}

	public void setBeforeCreateDate(LocalDate beforeCreateDate) {
		this.beforeCreateDate = beforeCreateDate;
	}

	public boolean isBeforeSuccessFlg() {
		return beforeSuccessFlg;
	}

	public void setBeforeSuccessFlg(boolean beforeSuccessFlg) {
		this.beforeSuccessFlg = beforeSuccessFlg;
	}

	public double getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(double estimateTime) {
		this.estimateTime = estimateTime;
	}



}

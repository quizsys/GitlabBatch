package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalenderUtil {

	public CalenderUtil(ArrayList<String> holidayList) {
		this.holidayList = holidayList;
	}

	/**
	 *  CREATE_TERMS = 1. 週次で作成
	 */
	private final int CREATE_TERMS_WEEK = 1;

	/**
	 * CREATE_TERMS = 2. 月次で作成
	 */
	private final int CREATE_TERMS_MONTH = 2;

	/**
	 *  ISSUE_DATE = 1. 今週
	 */
	private final int ISSUE_DATE_THIS_WEEEK = 1;
	/**
	 *  ISSUE_DATE = 2. 来週
	 */
	private final int ISSUE_DATE_NEXT_WEEEK = 2;
	/**
	 *  ISSUE_DATE = 3. 今月
	 */
	private final int ISSUE_DATE_THIS_MONTH = 3;
	/**
	 *  ISSUE_DATE = 4. 来月
	 */
	private final int ISSUE_DATE_NEXT_MONTH = 4;

	/**
	 * 祝日のリスト
	 */
	private ArrayList<String> holidayList;



	/**
	 * 次回の実行日付を返却する
	 * @param baseDate
	 * @param createTerms
	 * @param CreateTermsDetail : 1:月曜日...7:日曜日
	 * @return
	 */
	public LocalDate getNextCreateDate(LocalDate baseDate, int createTerms, int CreateTermsDetail) {

		LocalDate ret = null;

		if(createTerms == CREATE_TERMS_WEEK) {

			//次週の日曜日
			LocalDate nextSunday = baseDate.plusWeeks(1).minusDays(baseDate.getDayOfWeek().getValue());

			//対象日付を取得
			ret = nextSunday.plusDays(CreateTermsDetail);

		} else if(createTerms == CREATE_TERMS_MONTH) {

			//次月の0日
			LocalDate nextZeroDay = baseDate.plusMonths(1).minusDays(baseDate.getDayOfMonth());

			//対象日付を取得
			ret = nextZeroDay.plusDays(CreateTermsDetail);
		}

//		System.out.println("次回実行日：" + ret);
		return ret;

	}


	/**
	 * ISSUEの実行日を計算する
	 * @param baseDate
	 * @param issueDate
	 * @param issueDateDetail
	 * @return
	 */
	public LocalDate getIssueDate(LocalDate baseDate, int issueDate, int issueDateDetail) {

		LocalDate ret = null;

		// 週次の場合
		if(issueDate == ISSUE_DATE_THIS_WEEEK || issueDate == ISSUE_DATE_NEXT_WEEEK) {

			// 追加する週数
			int plusWeek = issueDate -1;
			// 対象日付を取得
			ret = baseDate.plusWeeks(plusWeek)
					      .minusDays(baseDate.getDayOfWeek().getValue())
					      .plusDays(issueDateDetail);

		// 月次の場合
		} else if(issueDate == ISSUE_DATE_THIS_MONTH || issueDate == ISSUE_DATE_NEXT_MONTH) {

			// 追加する月数
			int plusMonth = issueDate -3;

			//当月の0日
			LocalDate thisZeroDay = baseDate.minusDays(baseDate.getDayOfMonth());

			// 32未満の場合、日付ベース
			if(issueDateDetail < 32) {

				ret = thisZeroDay.plusDays(issueDateDetail).plusMonths(plusMonth);

			// 32 以上の場合、営業日ベース
			} else {

				//基準月の1日
				LocalDate thisFirstDay = thisZeroDay.plusDays(1).plusMonths(plusMonth);
				ret = thisZeroDay.plusMonths(plusMonth).plusDays(calcBussinessDay(thisFirstDay, issueDateDetail));

			}

		}

//		System.out.println("引数："+ issueDate + "," + issueDateDetail + ", ISSUE実行日：" + ret);
		return ret;

	}


	/**
	 * 営業日数から日付を計算
	 * @param thisFirstDay : 対象月の最初の日（1日）
	 * @param day          : 営業日数
	 * @return
	 */
	public long calcBussinessDay(LocalDate thisFirstDay, int day) {

		// 目標の営業日数
		int countBussinessDate = day -32;

		// 返却値（対象月の日付）
		int retDate = 0;

		// 基準日
		LocalDate baseDate = thisFirstDay.minusDays(1);
//		System.out.println("thisFirstDay: " + thisFirstDay);
//		System.out.println("baseDate: " + baseDate);

		// 1か月繰り返す
		for(int i = 1; i<= thisFirstDay.lengthOfMonth() ; i++) {

			// チェック用の日付
			LocalDate checkDate = baseDate.plusDays(i);
//			System.out.println(checkDate + ", 曜日: " + checkDate.getDayOfWeek().getValue());

			// 土日チェック
			if(checkDate.getDayOfWeek().getValue() ==  DayOfWeek.SATURDAY.getValue()
					|| checkDate.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()) {
//				System.out.println("土日" + checkDate.toString());
				continue;

			// 祝日チェック
			} else if(holidayList.contains(checkDate.toString())) {
//				System.out.println("祝日" + checkDate.toString());

				continue;

			}

			// 営業日の残数を減らす
			countBussinessDate --;
			//目標の営業日数に合致したら抜ける
			if(countBussinessDate == 0) {
				retDate = i;
				break;
			}
		}
//		System.out.println(retDate);
		return retDate;
	}


	/**
	 * issueのタイトル内の日付文字列を変換する
	 * @param issueTitle
	 * @param issueDate
	 * @return
	 */
	public String convertIssueTitle(String issueTitle, LocalDate issueDate) {

		String YYYY = Integer.toString(issueDate.getYear());
		String MM   = Integer.toString(issueDate.getMonthValue());
		String DD   = Integer.toString(issueDate.getDayOfMonth());
		String UU   = Integer.toString((int)Math.ceil(issueDate.getDayOfMonth() / 7));


		return issueTitle.replace("YYYY", YYYY).replace("MM", MM).replace("DD", DD).replace("UU", UU);

	}



}

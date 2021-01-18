package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitlabBatchApplication implements CommandLineRunner {

	private static final Logger LOGGER = LogManager.getLogger(GitlabBatchApplication.class);



	public static void main(String[] args) {
		SpringApplication.run(GitlabBatchApplication.class, args);
	}

	@Autowired
	SummaryDao summaryDao;

	@Autowired
	BurnDownDao burnDownDao;


	@Autowired
	TemplateCreateDao templateCreateDao;

	@Autowired
	HolidayDao holidayDao;

	@Override
	public void run(String... args) throws Exception {


		LOGGER.info("==================================================");
		LOGGER.info("START: バッチ処理を開始します");

		LOGGER.info("START: ISSUEの統計情報の取得");

    	GitlabSendRequest gitlabSendRequest = new GitlabSendRequest();

    	SummaryDto summaryDto = gitlabSendRequest.getIssueStatistics();
    	SummaryDto retSave = summaryDao.save(summaryDto);
    	LOGGER.info("保存日付: " + retSave.getDate());

		LOGGER.info("END: ISSUEの統計情報の取得");



		LOGGER.info("START: バーンダウン情報の取得");

		ArrayList<BurnDownDto> list = gitlabSendRequest.getBurnDown();
		LOGGER.info("登録内容：" + list);
		if(list.size() > 0) {
	    	List<BurnDownDto> ret = burnDownDao.saveAll(list);
			LOGGER.info("登録件数：" + ret.size());
		}

		LOGGER.info("END: バーンダウン情報の取得");




		LOGGER.info("START: ISSUEのスケジュール作成開始");

		// 当日日付を取得
		LocalDate today = LocalDate.now();

		// 2か月分の祝日リストを取得
		ArrayList<String> holidayList = getHolidayList(today);
		CalenderUtil calenderUtil = new CalenderUtil(holidayList);

		//　実行スケジュールを取得
		List<TemplateCreateDto> exeList = templateCreateDao.findByNextCreateDate(today);

		// 実行結果保存用の配列
		ArrayList saveList = new ArrayList<TemplateCreateDto>();

		//リクエストを送信
		for(TemplateCreateDto dto : exeList) {

			LOGGER.info("ISSUE作成、テンプレート： " + dto.getTemplateName());

			// ISSUEの実施日を計算
			LocalDate issueDate = calenderUtil.getIssueDate(today, dto.getIssueDate(), dto.getIssueDateDetail());

			// ISSUEのタイトルを作成
			String issueTitle = calenderUtil.convertIssueTitle(dto.getIssueTitle(), issueDate);

			// 次回実施日を計算
			LocalDate nextDate = calenderUtil.getNextCreateDate(today, dto.getCreateTerms(), dto.getCreateTermsDetail());

			//リクエスト処理
			LOGGER.info("ISSUE作成、タイトル： " + issueTitle);

			// ISSUE作成リクエストを送信
			boolean successFlg = gitlabSendRequest.sendCreateIssueRequest(dto.getProjectId(), dto.getTemplateName(), issueTitle, dto.getLabels());

			// 実行結果を記録
			dto.setBeforeCreateDate(today);
			dto.setBeforeSuccessFlg(successFlg);

			// 次回実行日時を設定
			dto.setNextCreateDate(nextDate);
			saveList.add(dto);
		}


		if(saveList.size() > 0) {
			//実行結果をDBに格納
			List<TemplateCreateDto> retList = templateCreateDao.saveAll(saveList);
			LOGGER.info("実行件数： " + retList.size());

		} else {
			// 0件だったとき
			LOGGER.info("実行対象のスケジュールはありませんでした");
		}

		LOGGER.info("END: ISSUEのスケジュール作成終了");

		LOGGER.info("END: バッチ処理が完了しました");


	}

	//
	/**
	 * 今月と来月の祝日リストを作成
	 * @param today：今日
	 * @return
	 */
	private ArrayList<String> getHolidayList(LocalDate today) {

		LocalDate since = today.minusDays(today.getDayOfMonth() -1);
		LocalDate until = since.plusMonths(2);
		List<HolidayDto> dbHolidayList = holidayDao.findByDateBetween(since, until);
		ArrayList<String> holidayList = new ArrayList<>();

		for(HolidayDto dto :dbHolidayList) {
			holidayList.add(dto.getDate().toString());
		}
//		System.out.println(holidayList);
		return holidayList;
	}

}

package com.example.demo;

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
	SummaryDao dao;

	@Autowired
	BurnDownDao burnDownDao;

	@Override
	public void run(String... args) throws Exception {


		LOGGER.info("==================================================");
		LOGGER.info("START: バッチ処理を開始します");

//		LOGGER.info("START: ISSUEの統計情報の取得");
//
    	GitlabSendRequest gitlabSendRequest = new GitlabSendRequest();
//
//    	SummaryDto dto = gitlabSendRequest.getIssueStatistics();
//    	SummaryDto retSave = dao.save(dto);
//    	LOGGER.info("保存日付: " + retSave.getDate());
//
//		LOGGER.info("END: ISSUEの統計情報の取得");


		LOGGER.info("START: バーンダウン情報の取得");
//
//		ArrayList<BurnDownDto> list = gitlabSendRequest.getBurnDown();
//		if(list.size() > 0) {
//	    	List<BurnDownDto> ret = burnDownDao.saveAll(list);
//			LOGGER.info("登録件数：" + ret.size());
//		}

		LOGGER.info("END: バーンダウン情報の取得");

		LOGGER.info("END: バッチ処理が完了しました");


	}

}

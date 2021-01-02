package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitlabBatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GitlabBatchApplication.class, args);
	}

	@Autowired
	SummaryDao dao;

	@Override
	public void run(String... args) throws Exception {


    	System.out.println("START: バッチ処理を開始します");

    	SummaryDto dto = GitlabSendRequest.get();
    	SummaryDto ret = dao.save(dto);
		System.out.println("保存日付: " + ret.getDate());

    	System.out.println("END: バッチ処理が完了しました");


	}

}

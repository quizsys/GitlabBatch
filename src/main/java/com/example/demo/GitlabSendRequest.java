package com.example.demo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

public class GitlabSendRequest {


//	public static String HOST = "proxy.hoge.jp";
//	public static int PORT = 0000;
//	public static String USER = "xxxxx";
//	public static String PASSWORD = "xxxxx";
	public static Charset charset = StandardCharsets.UTF_8;
	public static String strGetUrl = "https://x.x.x.x/api/v4/groups/4/issues?private_token=xxxx";


	public static SummaryDto get() throws Exception{

    	String todoStr = sendHeadRequest(strGetUrl + "&state=opened&labels=To%20Do");
    	String doingStr = sendHeadRequest(strGetUrl + "&state=opened&labels=Doing");
    	String doneStr = sendHeadRequest(strGetUrl + "&state=opened&labels=Done");
    	String closedStr = sendHeadRequest(strGetUrl + "&state=closed");
    	String openedStr = sendHeadRequest(strGetUrl + "&state=opened");

		LocalDate today = LocalDate.now();

    	int todo = Integer.parseInt(todoStr);
    	int doing = Integer.parseInt(doingStr);
    	int done = Integer.parseInt(doneStr);
    	int closed = Integer.parseInt(closedStr);
    	int opened = Integer.parseInt(openedStr) - todo - doing - done;

    	System.out.println(
			"todo:" + todoStr +
			", doing:" + doing +
			", done:" + done +
			", closed:" + closed +
			", opened:" + opened
		);

		return new SummaryDto(today, opened, todo, doing, done, closed);
	}


	public static String sendHeadRequest(String url) throws Exception {
		String ret = "";
//		HttpHost proxy = new HttpHost(HOST, PORT);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
//		credsProvider.setCredentials(
//		        new AuthScope(proxy),
//		        new UsernamePasswordCredentials(USER, PASSWORD));
		RequestConfig config = RequestConfig.custom()
//		        .setProxy(proxy)
				.build();
		HttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider)
				.setDefaultRequestConfig(config)
				.build();

		HttpHead head = new HttpHead(url);
		HttpResponse res = httpclient.execute(head);
		int status = res.getStatusLine().getStatusCode();
		System.out.println(res.getStatusLine());
		if (status == HttpStatus.SC_OK){
			String name = "X-Total";
		    for (Header header : res.getHeaders(name)) {
		       ret = header.getValue();
		    }
		}
		return ret;
	}
}

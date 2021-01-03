package com.example.demo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GitlabSendRequest {


//	public static String HOST = "proxy.hoge.jp";
//	public static int PORT = 0000;
//	public static String USER = "xxxxx";
//	public static String PASSWORD = "xxxxx";
	public static Charset charset = StandardCharsets.UTF_8;
	public static String strGetUrl = "https://x.x.x.x/api/v4/groups/4/issues_statistics?private_token=xxxx";


	public static SummaryDto get() throws Exception{

    	String todoStr  = sendHeadRequest(strGetUrl + "&state=opened&labels=To%20Do");
    	String doingStr = sendHeadRequest(strGetUrl + "&state=opened&labels=Doing");
    	String doneStr  = sendHeadRequest(strGetUrl + "&state=opened&labels=Done");
    	String openAndCloseStr = sendHeadRequest(strGetUrl);

		LocalDate today = LocalDate.now();
		Util util = new Util();

    	int todo = util.returnOpenedFromStr(todoStr);
    	int doing = util.returnOpenedFromStr(doingStr);
    	int done = util.returnOpenedFromStr(doneStr);

    	int openAndClosedArray[] = util.returnOpenedAndClosedFromStr(openAndCloseStr);
    	int opened = openAndClosedArray[0] - todo - doing - done;
    	int closed = openAndClosedArray[1];

    	System.out.println(
			"todo:" + todo +
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

		HttpGet head = new HttpGet(url);
		HttpResponse res = httpclient.execute(head);
		int status = res.getStatusLine().getStatusCode();
		System.out.println(res.getStatusLine());
		if (status == HttpStatus.SC_OK){
			ret =EntityUtils.toString(res.getEntity(),charset);
		}
		return ret;
	}
}

package com.example.demo;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.demo.burnDown.BurnDownDto;
import com.example.demo.summary.SummaryDto;

public class GitlabSendRequest {

	private static final Logger LOGGER = LogManager.getLogger(GitlabBatchApplication.class);

	public static String HOST = "proxy.hoge.jp";
	public static int PORT = 0000;
	public static String USER = "xxxxx";
	public static String PASSWORD = "xxxxx";
	private final Charset charset = StandardCharsets.UTF_8;
	private final String GIT_URL = "https://x.x.x.x/api/v4";
	private final String TOKEN = "xxxx";
	private final String GROUP_ID = "4";
	private final int LOOP_COUNT = 10; //ループ回数、無限ループしないように上限を設定


	/**
	 * ISSUEの統計情報を取得
	 * @return
	 * @throws Exception
	 */
	public SummaryDto getIssueStatistics() throws Exception{

	    String strGetUrl = GIT_URL + "/groups/" + GROUP_ID + "/issues_statistics?private_token=" + TOKEN;
    	String todoStr  = sendGetRequest(strGetUrl + "&state=opened&labels=To%20Do");
    	String doingStr = sendGetRequest(strGetUrl + "&state=opened&labels=Doing");
    	String doneStr  = sendGetRequest(strGetUrl + "&state=opened&labels=Done");
    	String openAndCloseStr = sendGetRequest(strGetUrl);

		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1); //翌日午前午前1時の実行なので、前日日付を取得する
		Util util = new Util();

    	int todo = util.returnOpenedFromStr(todoStr);
    	int doing = util.returnOpenedFromStr(doingStr);
    	int done = util.returnOpenedFromStr(doneStr);

    	int openAndClosedArray[] = util.returnOpenedAndClosedFromStr(openAndCloseStr);
    	int opened = openAndClosedArray[0] - todo - doing - done;
    	int closed = openAndClosedArray[1];

    	LOGGER.info(
			"保存件数: " +
			"todo:" + todo +
			", doing:" + doing +
			", done:" + done +
			", closed:" + closed +
			", opened:" + opened
		);

		return new SummaryDto(yesterday, opened, todo, doing, done, closed);
	}



	/**
	 * バーンダウンの情報を取得
	 * @return
	 * @throws Exception
	 */
	public ArrayList<BurnDownDto> getBurnDown() throws Exception{

		String strGetMilestoneUrl = GIT_URL + "/groups/" + GROUP_ID + "/milestones?private_token=" + TOKEN;
		String strGetIssueUrl = GIT_URL + "/groups/" + GROUP_ID + "/issues?private_token=" + TOKEN;

		Util util = new Util();

		//マイルストーンの取得処理
		String milestoneJson = sendGetRequest(strGetMilestoneUrl);
		String milestone = util.jsonToMilestoneDate(milestoneJson);
		LOGGER.info("マイルストーン： " + milestone);

		//もし期間内にマイルストーンがなければ処理終了
		if("".equals(milestone)) {
			LOGGER.warn("期間内にマイルストーンがありませんでした");
			return new ArrayList<>();
		}

		//マイルストーンの内のissueの取得処理
		ArrayList<String> jsonList = new ArrayList<String>();

		for(int i = 1; i< LOOP_COUNT; i++) {

			String ret[]  = sendHeadRequest(strGetIssueUrl + "&milestone=" + milestone + "&per_page=100&page=" + i);
	    	jsonList.add(ret[0]);
	    	LOGGER.info("nextPage = " + ret[1]);

	    	try {
	    		Integer.parseInt(ret[1]);
	    	} catch(Exception e) {
	    		break;
	    	}
		}

		return util.jsonToDto(jsonList, milestone);
	}


	/**
	 * ISSUE作成のリクエストを送信
	 * @return
	 * @throws Exception
	 */
	public boolean sendCreateIssueRequest(int projectId, String templateFileName, String issueTitle, String labels, double estimateTime) throws Exception {

		String templatePath = ".gitlab/issue_templates/" + templateFileName;
		String templatePathEncord = URLEncoder.encode(templatePath, "UTF-8");

		String strGetTemplateUrl = GIT_URL + "/projects/" + projectId + "/repository/files/" + templatePathEncord + "/raw"
				+ "?private_token=" + TOKEN + "&ref=master";


//		System.out.println(strGetTemplateUrl);

		// テンプレート取得リクエストを送信
		String templateContent = sendGetRequest(strGetTemplateUrl);
//		System.out.println(templateContent);

		// テンプレートの取得に失敗した場合、処理終了
		if(templateContent.equals("")) {
			LOGGER.warn("テンプレートの取得に失敗しました。プロジェクトID: " + projectId + ", 取得ファイル名：" + templateFileName);
			return false;
		}

		// ISSUE作成のPOSTリクエスト
		String strPostIssueUrl = GIT_URL + "/projects/" + projectId + "/issues";

		// リクエストを作成
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("private_token", TOKEN));
		nvps.add(new BasicNameValuePair("title", issueTitle));
		nvps.add(new BasicNameValuePair("description", templateContent));
		nvps.add(new BasicNameValuePair("labels", labels));

//		System.out.println("リクエストURL:" + strPostIssueUrl);
		LOGGER.info("POST送信開始（ISSUE作成）");

		// ISSUE作成のリクエストを送信
		String resJson = sendPostRequest(strPostIssueUrl, nvps);

		Util util = new Util();

		// 登録したissueのiidを取得
		int iid = util.jsonToIssueIid(resJson);

		// estimateの時間を設定しない場合、処理終了
		if(estimateTime == 0) {
			LOGGER.info("estimateの時間設定はありませんでした");
			return true;
		}

		// estimate設定用のPOSTリクエスト
		String strPostIssueEstimateUrl = GIT_URL + "/projects/" + projectId + "/issues/" + iid + "/time_estimate?duration=" + estimateTime + "h";
//		System.out.println("リクエストURL:" + strPostIssueEstimateUrl);


		LOGGER.info("POST送信開始（estimate設定）");

		// estimate設定のリクエストを送信
		return sendPostRequestSuccessFlg(strPostIssueEstimateUrl, nvps);


	}




	/**
	 * リクエストを送信する（ボディとヘッダーのうちの一部を返却）
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String[] sendHeadRequest(String url) throws Exception {
		String ret[] = new String[2];
		ret[0] = ""; //body
		ret[1] = ""; // X-Next-Page

		HttpClient httpclient = getHttpClient();

		HttpGet get = new HttpGet(url);
		HttpResponse res = httpclient.execute(get);
		int status = res.getStatusLine().getStatusCode();
		LOGGER.info(res.getStatusLine());
		if (status == HttpStatus.SC_OK){
			ret[0] = EntityUtils.toString(res.getEntity(),charset);
			Header[] headers = res.getHeaders("X-Next-Page");
			for(Header head :headers) {
				ret[1] = head.getValue();
				break;
			}
		}
		return ret;
	}



	/**
	 * GETリクエストを送り、レスポンスの文字列を返す
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String sendGetRequest(String url) throws Exception {
		String ret = "";

		HttpClient httpclient = getHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse res = httpclient.execute(get);
		int status = res.getStatusLine().getStatusCode();
		LOGGER.info(res.getStatusLine());
		if (status == HttpStatus.SC_OK){
			ret =EntityUtils.toString(res.getEntity(),charset);
		}
		return ret;
	}


	/**
	 * POSTリクエストを送り、レスポンスの文字列を返す
	 * @param url
	 * @param nvps
	 * @return
	 * @throws Exception
	 */
	private String sendPostRequest(String url, ArrayList<NameValuePair> nvps) throws Exception {

		String ret = "";
		HttpClient httpclient = getHttpClient();
		HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nvps, charset));

		HttpResponse res = httpclient.execute(post);
		int status = res.getStatusLine().getStatusCode();
		LOGGER.info(res.getStatusLine());

		if (status == HttpStatus.SC_CREATED){
			ret =EntityUtils.toString(res.getEntity(),charset);
		}
		return ret;
	}


	/**
	 * POSTリクエストを送り、正常に作成されたかを返却する。
	 * @param url
	 * @param nvps
	 * @return
	 * @throws Exception
	 */
	private boolean sendPostRequestSuccessFlg(String url, ArrayList<NameValuePair> nvps) throws Exception {

		HttpClient httpclient = getHttpClient();
		HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nvps, charset));


		HttpResponse res = httpclient.execute(post);
		int status = res.getStatusLine().getStatusCode();
		LOGGER.info(res.getStatusLine());

		return status == HttpStatus.SC_OK;
	}



	/**
	 * 通信用のHttpClientを作成し、返却する
	 * @return
	 * @throws Exception
	 */
	private HttpClient getHttpClient() throws Exception {

		HttpHost proxy = new HttpHost(HOST, PORT);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(proxy),
				new UsernamePasswordCredentials(USER, PASSWORD));
		RequestConfig config = RequestConfig.custom()
//				.setProxy(proxy)                   //プロキシ使う場合はコメント外す
				.build();
		HttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider)
				.setDefaultRequestConfig(config)
				.build();

		return httpclient;

	}
}

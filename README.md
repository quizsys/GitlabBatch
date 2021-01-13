<!DOCTYPE html>
<html>
<head>
  <title>Gitlab tools</title>
  <meta charset="UTF-8">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/core-js/2.4.1/core.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.0/css/bootstrap.min.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.js"></script>
  <script src="config.js"></script>
  <script src="common.js"></script>
  <script src="main.js"></script>
  <script src="main-chart.js"></script>
</head>
<body>


  <h1>ISSUEを集計</h1>
  <select id="milestone"></select>
  <button class="btn btn-primary" id="get-info-btn" onclick="getInfo()">送信</button>
  <div id="update-date-time"></div>

  <br>
  <br>

	<table class="table table-striped">
		<thead>
		  <tr>
		  	<th>名前</th>
		  	<th>ISSUEの数</th>
		  	<th>estimate(h)</th>
		  	<th>spend(h)</th>
		  	<th>spendMR(h)</th>
		  </tr>
		</thead>
		<tbody id="result">
		</tbody>
	</table>


  <br>

  <h4>ラベル別集計</h4>
    <input id="label-name" placeholder="ラベル名でフィルター" onchange="changeLabelName()"></input>
    <input type="checkbox" id="graph-display-flg" onchange="changeLabelName()" />グラフ表示
	<table id="label-table" class="table table-striped">
		<thead>
		  <tr>
		  	<th>ラベル名</th>
		  	<th>ISSUEの数</th>
		  	<th>estimate(h)</th>
		  	<th>spend(h)</th>
		  	<th>spendMR(h)</th>
		  </tr>
		</thead>
		<tbody id="result-by-tags">
		</tbody>
	</table>
  <canvas id="label-chart" style="height: 50px"></canvas>

  <!-- <br> -->
  <h4>一覧をcsvダウンロード</h4>
  <a id="download" class="btn btn-info" href="#" download="test.csv" onclick="csvDownload()">ダウンロード</a>



  <br>
  <br>
  <hr>

  <h1>ISSUEを作成</h1>
  <h4>継続ISSUEを作成</h4>
  <select id="issue"></select>
  <button class="btn btn-success" onclick="getIssueContinue()">作成</button><br />
  <input id="search-issue-name" placeholder="名前でフィルター" onchange="changeSearchIssueName()"></input>
  <div id="createIssueResultContinue" style="color:red"></div>

	<br>
	<br>

  <h4>テンプレートから作成</h4>
  <table class="table" border="1">
    <tbody>
      <tr>
        <td class="table-secondary">①プロジェクトを選択</td>
        <td>
          <select id="select-project" onchange="getTemplateList()"></select>
        </td>
      </tr>
      <tr>
        <td class="table-secondary">②テンプレートを選択</td>
        <td>
          <select id="select-template"></select><br />
          <input type="checkbox" id="global-template-flg" onchange="getTemplateList()" />グローバルテンプレートを使用
        </td>
      </tr>
      <tr>
        <td class="table-secondary">③マイルストーンを選択</td>
        <td>
          <select id="select-milestone"></select>
        </td>
      </tr>
      <tr>
        <td class="table-secondary">④ラベルを選択<br>（Ctrl押しながらで複数選択可）</td>
        <td>
          <select id="select-label" multiple></select>
        </td>
      </tr>
      <tr>
        <td class="table-secondary">⑤ISSUEの名前を入力</td>
        <td>
          <input type="text" id="issue-name" placeholder="ISSUEの名前を入力してください" style="width:50vw"/>
        </td>
      </tr>
    </tbody>
  </table>
  <button class="btn btn-success" onclick="getTemplate()">作成</button>
  <div id="createIssueResult" style="color:red"></div>

  <br>
  <br>
  <hr>
  <h1>チャートを表示</h1>

  <h4>バーンダウンチャートを表示</h4>

  <select id="select-label-chart"></select>
  <button class="btn btn-primary" onclick="getInfoBurnDownChart()">表示</button><br />
  <canvas id="burnDownChart"></canvas>



  <ul>
    <li><a href="diagram/" target="_blank">累積フローダイアグラムを表示</a></li>
  </ul>

</body>

<script>
  window.onload = function() {
    getProjectList()
    getMilestoneList()
  }
</script>

</html>

# バッチプロジェクトの作成手順

### 注意点
**この手順ではプロジェクト名を`SpringSampleBatch`、ログの出力先を`/springsample/log/springsample-batch.log`としています。**  
**適時、自分のプロジェクト名に読み替えて、実施してください。**


## プロジェクトの作成

* 大まかな手順は 前回と同じ。  

* パッケージエクスプローラーの何もないところで、右クリック > 新規 > その他 > Spring Boot > Spring スタータープロジェクト を選択し、次へを押下。  

* 下記のように変更して、次へ。
```
　・名前：プロジェクト名に変更
　・型：Gradle
　・パッケージング：jar
　・Javaバージョン：8
```

* プロジェクトの依存関係で、下記を設定し、「完了」を押下
```
　・開発ツール
　　・Spring Boot DevTools
　・SQL
　　・Spring Data JPA
　　・MySQL Driver
```

## log4j（ログを出力する仕組み）の設定

* build.gradleに下記を追加
```
	testCompile 'junit:junit:4.12'
	compile group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.5'
	compile group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.5'
```
※追加する場所は、`dependencies {`の括弧の中。  
詳しくはこちら→[参考サイト](https://qiita.com/kazurof/items/abbd42f11bfc125f3190#gradle-%E3%83%93%E3%83%AB%E3%83%89%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB)

* Log4jの 設定ファイルを追加
パッケージエクスプローラーから「src/main/resources」を選択し、右クリック > 新規 > ファイル を選択する。
ファイル名に「log4j2.xml」と入力し、「完了」を押下  
ファイル（log4j2.xml）に下記のようにログの設定を記載する。
```
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="OFF">
  <appenders>
    <Console name="console" target="SYSTEM_OUT">
      <PatternLayout pattern="%d %-5p [%t] - %m (%C.java:%L)%n"/>
    </Console>
  		<RollingFile name="file" fileName="/springsample/log/springsample-batch.log"
			filePattern="/springsample/log/springsample-batch.%d{yyyyMMdd}.log">
			<PatternLayout pattern="%d %-5p [%t] - %m (%C.java:%L)%n"/>
			<Policies>
				<CronTriggeringPolicy schedule="0 0 0 * * ?" />
			</Policies>
			<DefaultRolloverStrategy max="10" />
		</RollingFile>
  </appenders>

  <loggers>
    <Logger name="org.example" level="info" additivity="false">
      <appender-ref level="info" ref="console"/>
    </Logger>
    <root level="info">
      <appender-ref ref="console"/>
      <appender-ref ref="file"/>
    </root>
  </loggers>
</configuration>
```

「/springsample/log/springsample-batch.log」と「/springsample/log/springsample-batch.%d{yyyyMMdd}.log」がログのファイル名のため、出力先のパスに変える。  
「/springsample/log/springsample-batch.log」がログのファイル名、  
「/springsample/log/springsample-batch.%d{yyyyMMdd}.log」がローテーション後（古いログが退避された姿）のファイル名  
詳しくはこちら→[参考サイト1](https://qiita.com/kazurof/items/abbd42f11bfc125f3190#log4j-2-%E8%A8%AD%E5%AE%9A%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB)
[参考サイト2](https://pukiwiki.codereign.org/index.php?SoftwareEngineering%2FJava%2FLogging%2FApacheLog4j2%2FAppenders)


## DaoとDto、application.propertyを作成
* 他プロジェクトで作成したものを、コピー&ペーストして終わり（使いまわしして省エネしましょう！）  
下記のようなファイル構成になればOK
```
SpringSampleBatch
├─build.gradle                                               ★ビルドの内容を記載（編集）
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─demo
    │  │              ├─BulletinBoardDao.java                ★DAO（コピペ）
    │  │              ├─BulletinBoardDto.java                ★DTO（コピペ）
    │  │              └─SpringSampleBatchApplication.java    ★実行ファイル（編集）
    │  │
    │  └─resources
    │      ├─application.properties                          ★DB接続定義ファイル（コピペ）
    │      └─log4j2.xml                                      ★ログ出力定義ファイル（新規作成）
    │
    └─test
```


## SpringSampleBatchApplicationのクラスを修正
コマンドラインから実行できるように`CommandLineRunner`を継承する


* CommandLineRunnerを継承
クラス名の後ろに`implements CommandLineRunner`を追加して継承する。
例）
```
public class SpringSampleBatchApplication implements CommandLineRunner {
```

* Daoを実装する
```
    @Autowired
    private BulletinBoardDao bulletinBoardDao;
```


* ロガー（log4j）を実装する
```
    private static final Logger LOGGER = LogManager.getLogger(SpringSampleBatchApplication.class);
```

* runメソッドを作成する（このメソッド内に実行したい処理を書いていく）
```
    @Override
    public void run(String... strings) throws Exception {
        
        LOGGER.info("START: バッチ処理を開始します");
        
        //ここに処理を書く（daoのメソッド呼び出しなど）
        
        LOGGER.info("END: バッチ処理が完了しました");
    }
```

上記4点が終わった後に、下記のようになっていればOK
```
@SpringBootApplication
public class SpringSampleBatchApplication implements CommandLineRunner {

    private static final Logger LOGGER = LogManager.getLogger(SpringSampleBatchApplication.class); // ロガーの実装

    @Autowired  //Daoの実装
    private BulletinBoardDao bulletinBoardDao;

	public static void main(String[] args) {
		SpringApplication.run(SpringSampleBatchApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {

        LOGGER.info("START: バッチ処理を開始します");
        
        //ここに処理を書く（daoのメソッド呼び出しなど）
        bulletinBoardDao.deleteBySolveFlg() //←例です。適時、書き換えてください
        
        LOGGER.info("END: バッチ処理が完了しました");
    }
}

```



# Gitへの登録

* プロジェクトを右クリック→Windowsエクスプローラー  
　プロジェクトのフォルダが表示される

* プロジェクトを右クリック→削除  
　「ディスク上から、、、」にチェックをいれない（★大事★）でOK
　
* Gitのフォルダに、プロジェクトフォルダをそのまま移動してくる。  
　→Gitにコミット→プッシュ

* eclipseで  
　何もないところで、右クリック >インポート > Gradle > 既存のGradleプロジェクト で次へ  
　参照を押下し、先ほどGitに登録したプロジェクトフォルダを選択し、完了を押下。  
　エラーが出ずに、eclipse上に元のプロジェクトフォルダができていればOK  



# 実行手順
* warの時と同様に「実行」 > 「Spring Bootアプリケーション」でOK
　
* 実行後に下記を確認する。  
　①eclipseのコンソールに`START: バッチ処理を開始します`と`END: バッチ処理が完了しました`と出力されていること  
　②Cドライブ直下の`/springsample/log/springsample-batch.log`に上記同様のログが出力されていること。（自分で設定したログのファイル名に書き換える）  
詳しくはこちら→[参考サイト](https://qiita.com/misskabu/items/9f0402554d511bf4b35d)  



# jarのビルド
* プロジェクト名を右クリック > 実行 > 実行の構成 > Gradle Taskを選択肢、右クリック > 新規構成（W） を選択。  
「追加」を押下し、Gradleタスクの入力欄の「task」を「build」に書き換える。  
ワークスペースを選択し、「SpringSampleBatch」のプロジェクトを選択し、右下の「実行」ボタンを押下する。


* initializationErrorが出る場合は、「SpringSampleBatchApplicationTests.java」の中身をすべてコメントアウトする  

* 実行後に、プロジェクトを右クリック→Windowsエクスプローラー  を選択し、プロジェクトのフォルダが表示し、build/libsの下にjarファイルが作成されていればOK。

# jarファイルの実行方法

jarファイルのあるフォルダ内で、コマンドラインから下記のようにコマンドを実行する。
```
java -jar SpringSampleBatch.jar  
```
ログが出ていればOK
```
20yy-MM-dd 09:04:32,125 INFO  [restartedMain] - START: バッチ処理を開始します (com.example.demo.SpringSampleBatchApplication.java:32)
20yy-MM-dd 09:04:32,125 INFO  [restartedMain] - END: バッチ処理が完了しました (com.example.demo.SpringSampleBatchApplication.java:55)
```


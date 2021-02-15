// 最終変更日時：2020年7月19日 20：23

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteDataSource;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Storage extends Application {

	int a;	  // データベースのメソッドで使用
	double b; // データベースのメソッドで使用
	int c;    // データベースのメソッドで使用
	String s; // データベースのメソッドで使用
	String table = "a";
	SQLiteDataSource ds = null; // データベースのメソッドで使用
	LineNumberReader nr = null; // データベースのメソッドで使用

	 //抽出されるファイル名
	String  get_file = "C:/Users/~/Downloads/data.csv";

	  //接続するデータベース名
	String  db_url = "jdbc:sqlite:C:/Users/~/Temperature/SQLite/testDB6.db";

	File file = new File(get_file);


	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	String msg = "";
	Label lb1 = new Label("進捗状況");
	Label lb2 = new Label(msg);


	public static void main(String[] args) {
		launch(args);

	}
	public void start(Stage stage) throws Exception {;
		Button bt1 = new Button("実行");
		Button bt2 = new Button("確認");

		bt1.setFont(Font.font("serif", 20));
		bt2.setFont(Font.font("serif", 20));

		lb1.setFont(Font.font("serif", 20));
		lb2.setFont(Font.font("serif", 20));

		VBox vb = new VBox();
		vb.getChildren().addAll(lb1, lb2);
		vb.setAlignment(Pos.CENTER);

		HBox hb1 = new HBox();
		HBox hb2 = new HBox();
		HBox hb3 = new HBox();

		hb1.getChildren().add(bt1);
		hb2.getChildren().add(bt2);
		hb3.getChildren().add(vb);
		hb1.setAlignment(Pos.CENTER);
		hb2.setAlignment(Pos.CENTER);
		hb3.setAlignment(Pos.CENTER);

		BorderPane bp = new BorderPane();
		bp.setTop(hb1);
		bp.setCenter(hb3);
		bp.setBottom(hb2);
		Scene sc = new Scene(bp, 600, 400);
		stage.setScene(sc);
		stage.setTitle("気温情報保管アプリ");
		stage.show();


		bt1.setOnAction(new ExecutionHandler());
		bt2.setOnAction(new ConfirmationEventHandler());
	}

	// 「実行」ボタン ハンドラー
		public class ExecutionHandler implements EventHandler<ActionEvent> {

			public void handle(ActionEvent event) {
				DB_connect();
				DB_csv(DB_table());
			}

		}

		// 「確認」ボタン　ハンドラー
		public class ConfirmationEventHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent event) {
			}

		}

	// データベースへアクセスするメソッド
	private void DB_connect() {
		try {
			ds = new SQLiteDataSource();
			ds.setUrl(db_url);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	// 新規にテーブルを作成するメソッド
	private String DB_table() {
		try{
			nr = new LineNumberReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();

			while((s=nr.readLine())!=null) {
				if(nr.getLineNumber() == 3) {

					String[] cul = s.split(",",0);
					table = cul[1]; // CSVデータ3行目に記載の「地点名」取り出し完了。
					break;
				}
			}
			nr.close();

			String query =
					"CREATE TABLE " + table + "(No int,年月日 varchar(20),時刻 varchar(10), 気温 double, グラフ  varchar(20))";
			stmt.executeUpdate(query);

			System.out.println("db create table ");
		}
		catch(IOException | SQLException e) {
			System.out.println("db failes for creating table");
			msg += "db failes for creating table" + LINE_SEPARATOR;
			lb2.setText(msg);
		}
		return table;
	}

	// 既存のテーブルにCSVデータを追加するメソッド
	private void DB_csv(String t) {
		try {
			nr = new LineNumberReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();

			String str,str2,graph;
			while((s=nr.readLine())!=null) {
				c = nr.getLineNumber(); // 現在の行番号（5行目からのデータが必要なため、次の処理を記述。）
				if(5<=c) {
					str = (c-4) + "," + s; //行番号から余分な行を引いてNoを求めてデータに追記しています。
					String[] cul = str.split(",",0);
					str2 = cul[0] + "," + cul[1]+","; // 「気温」列に空白がある場合、「null」を代入。
					if(str.equals(str2)) {			  // x軸(null)に対してy軸(0)として反映。
						str += "null";
						cul = str.split(",",0);
					}

					String[] cul_b = cul[1].split(" +",0); // 「年月日時」を空白を基準に「年月日」と「時刻」に分割。

					graph = DB_graph(cul[2]);  // グラフを行に追加。

					String query =
							"INSERT INTO " + t + " VALUES(" +
							cul[0]+ ",'" + cul_b[0] + "','"+ cul_b[1] + "',"+ cul[2] +",'" + graph +"')";
					stmt.executeUpdate(query);
				}

			}
			nr.close();
			file.delete(); // 抽出されたファイルを削除。

			System.out.println("db insert success");
			System.out.println(table + " テーブルに全部で "+ (c-4) + "行入りました。");
			msg += table + " テーブルに全部で "+ (c-4) + "行入りました。" + LINE_SEPARATOR;
			lb2.setText(msg);
		}
		catch(IOException | SQLException e) {
			System.out.println("db failes about inserting");
			msg += "db failes" + LINE_SEPARATOR;
			lb2.setText(msg);
		}
	}

	//　行に「グラフ」を追加するメソッド
	// グラフの意味：「～℃までの暑さ」または「～℃までの寒さ」
	private String DB_graph(String t) {
		String graph = "｜｜｜｜｜｜｜｜｜＊｜｜｜｜｜｜｜｜｜";

		if(!(t.equals("null"))) {
			double temp = Double.parseDouble(t);

			if(temp==0)
				graph =  "｜｜｜｜｜｜｜｜｜○｜｜｜｜｜｜｜｜｜";

			else if(-45<=temp && temp<-40)
				graph =  "○｜｜｜｜｜｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-40<=temp && temp<-35)
				graph =  "｜○｜｜｜｜｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-35<=temp && temp<-30)
				graph =  "｜｜○｜｜｜｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-30<=temp && temp<-25)
				graph =  "｜｜｜○｜｜｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-25<=temp && temp<-20)
				graph =  "｜｜｜｜○｜｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-20<=temp && temp<-15)
				graph =  "｜｜｜｜｜○｜｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-15<=temp && temp<-10)
				graph =  "｜｜｜｜｜｜○｜｜＊｜｜｜｜｜｜｜｜｜";
			else if(-10<=temp && temp<-5)
				graph =  "｜｜｜｜｜｜｜○｜＊｜｜｜｜｜｜｜｜｜";
			else if(-5<=temp && temp<0)
				graph =  "｜｜｜｜｜｜｜｜○＊｜｜｜｜｜｜｜｜｜";

			else if(0<temp && temp<=5)
				graph =  "｜｜｜｜｜｜｜｜｜＊○｜｜｜｜｜｜｜｜";
			else if(5<temp && temp<=10)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜○｜｜｜｜｜｜｜";
			else if(10<temp && temp<=15)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜○｜｜｜｜｜｜";
			else if(15<temp && temp<=20)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜○｜｜｜｜｜";
			else if(20<temp && temp<=25)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜｜○｜｜｜｜";
			else if(25<temp && temp<=30)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜｜｜○｜｜｜";
			else if(30<temp && temp<=35)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜｜｜｜○｜｜";
			else if(35<temp && temp<=40)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜｜｜｜｜○｜";
			else if(40<temp && temp<=45)
				graph =  "｜｜｜｜｜｜｜｜｜＊｜｜｜｜｜｜｜｜○";
		}
		else 	graph =  "｜｜｜｜｜｜｜｜｜○｜｜｜｜｜｜｜｜｜";

		return graph;
	}
}

package mybeans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteDataSource;

public class ClBean implements Serializable{

	private String ymd;
	private String prefecture, area;
	private ArrayList<String> colname;
	private ArrayList<ArrayList> data;;
	private String url;
	private String query;
	private SQLiteDataSource ds;

	public ClBean() {

		ymd        = null;
		prefecture = null;
		area       = null;
		url        = null;
		query      = null;
		ds         = null;
	}

	// 日付のメソッド
	public void setYmd(String y,String m, String d) {
		ymd = y + "/" + m +"/" + d;
	}
	public String getYmd() {
		return ymd;
	}

	// 都道府県のデータを設定するメソッド
	public void setPrefecture(String p) {
		prefecture = p;
	}
	// 都道府県のデータを取得するメソッド
	public String getPrefecture() {
		return prefecture;
	}

	// 地点名のデータを作成するメソッド
	public void makeArea(String p4, String p1, String p2, String p3) {
		if(prefecture.equals("愛知県")) 		area = p4;
		else if(prefecture.equals("岐阜県")) area = p1;
		else if(prefecture.equals("三重県")) area = p2;
		else if(prefecture.equals("静岡県")) area = p3;
	}
	// 地点名のデータを取得するメソッド
	public String getArea() {
		return area;
	}

	// 接続するデータベース名を設定するメソッド
	public void setUrl(String u) {
		url = u;
	}
	// データベースに接続するメソッド
	public void makeUrl(){

		try {
			// データベースへの接続
			ds = new SQLiteDataSource();
			ds.setUrl(url);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	// Queryデータを設定するメソッド
	public void setQuery(String p, String t) {
		query = "SELECT * FROM " +  p + " WHERE 年月日='" + t  + "'";
	}

	/*
	// Queryデータを取得するメソッド
	public String getQuery() {
		return query;
	}
	*/

	// Queryを作成するメソッド
	public void makeQuery() {
		try{
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			// 問い合わせ
			ResultSet rs = stmt.executeQuery(query);

			// 列数の取得
			ResultSetMetaData rm = rs.getMetaData();
			int cnum = rm.getColumnCount();
			colname = new ArrayList<String>(cnum);

			// 列名の取得
			for(int i=1;i<=cnum; i++) {
				colname.add(rm.getColumnName(i).toString());
			}

			//行の取得
			data = new ArrayList<ArrayList>();
			while(rs.next()) {
				ArrayList<String> rowdata
					= new ArrayList<String>();
				for(int i=1; i<(cnum+1); i++) { // cnumの数を補正。
					rowdata.add(rs.getObject(i).toString());
				}
				data.add(rowdata);
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch(SQLException e) {
			System.out.println("Select Fail");
			e.printStackTrace();
			System.exit(0);
		}
	}

	// dataを取得するメソッド
	public ArrayList getData() {
		return data;
	}
	// ArrayListを取得するメソッド
	public ArrayList getColname() {
		return colname;
	}
}

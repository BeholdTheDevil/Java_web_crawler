import java.io.*;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class crawler {
	public static db data = new db();

	public static void main(String[] args) throws SQLException, IOException {
		data.runSql2("TRUNCATE Record;");
		
		String query = "SELECT komplett_artnum FROM Components;";
		String query_two = "SELECT COUNT(*) FROM Components";
		String itemid_string = "";
		String address = "";
		String[] progress = {"\r [          ]",		//0
							 "\r [#         ]",		//1
						   	 "\r [##        ]",		//2
							 "\r [###       ]",		//3
							 "\r [####      ]",		//4
							 "\r [#####     ]",		//5
							 "\r [######    ]",		//6
							 "\r [#######   ]",		//7
							 "\r [########  ]",		//8
							 "\r [######### ]",		//9
							 "\r [##########]\n"};	//10
		int i = 0;

		try {
			ResultSet result = data.runSql(query);
			ResultSet res = data.runSql(query_two);
			int size = 0;
			int incre = 0;
			while(res.next()) {
				size = res.getInt(1);
			}
		 	incre = size/10;
	
			while(result.next()) {
				itemid_string = result.getString("komplett_artnum");
				address = "http://www.komplett.se/products/" + itemid_string;
				processPage(address);
				if(i % incre == 0) {
					if(size < 10) {
						System.out.print(progress[i]);	
					} else {
						System.out.print(progress[i/incre]);
					}
				}
				++i;
			}
			System.out.print(progress[10]);
		} catch (NullPointerException e) {
			System.out.println(" Invalid site");
		}
		System.out.println(" Process finished ");
		System.out.println(" Check database for output ");
	}

	public static void processPage(String URL) throws SQLException, IOException {
		String sql_one = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = data.runSql(sql_one);
		try {
			if(rs.next()) {

			} else {
				data.conn.setAutoCommit(false);
				sql_one = "INSERT INTO `Crawler`.`Record` (`URL`, `price`) VALUES (?, ?)";
				PreparedStatement stmt = data.conn.prepareStatement(sql_one, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, URL);

				Document doc = Jsoup.connect(URL).get();

				String price = doc.select("span.product-price-now").first().text();

				stmt.setString(2, price);
				stmt.execute();
				data.conn.commit();
			}	
		} catch(SQLException e) {
			
		}
	}
}
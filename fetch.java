import java.io.*;
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

public class fetch {
	public static db data = new db();

	public static void main(String[] args) throws SQLException, IOException {
		data.runSql2("TRUNCATE Components;");
		boolean validInput = false;
		String[] komplett_index_num = {"10392/bildskarm", "10149/chassibarebone", "10412/grafikkort", "10088/haarddisk", "10204/kontrollerkort", "10462/kylningvattenkylning", "11211/ljudkort", "11209/minne", "10111/moderkort", "10057/nataggregat", "10273/natverk", "11204/processor", "10335/programvara"};
		String[] komplett_index = {"screen", "case", "gpu", "storage", "controllercard", "cooling", "soundcard", "ram", "motherboard", "psu", "network", "cpu", "programs"};
		String site = "https://www.komplett.se/category/";
		String hits = "";
		if(args.length >= 1) {
			if(args.length == 2) {
				if(isParsable(args[1]) == true) {
					hits = args[1];
				} else {
					System.out.println(" Invalid hits-number");
				}
			}
			for(int i = 0; i < komplett_index.length; ++i) {
				if(args[0].equals(komplett_index[i])) {
					getArtNums(site + komplett_index_num[i] + "?hits=" + hits);
					validInput = true;
				}
			}
		}
		if(validInput != true) {
			System.out.println(" Invalid Input! Please use one of following options:");
			for(int j = 0; j < komplett_index.length; ++j) {
				System.out.println(" " + komplett_index[j]);
			}
		}

	}

	public static void getArtNums(String URL) throws SQLException, IOException{
		int i = 0;
		String sql_insert = "INSERT INTO `Crawler`.`Components` (`komplett_artnum`) VALUES (?);";
		try {
			PreparedStatement stmt_insert = data.conn.prepareStatement(sql_insert, Statement.RETURN_GENERATED_KEYS);
			Document doc = Jsoup.connect(URL).timeout(0).get();
			Elements elements = doc.select("a.product-link.image-container");
			for(Element element : elements) {
				Elements link = doc.select("a.product-link.image-container[href]").eq(i);
				String artNum = link.attr("href");

				stmt_insert.setString(1, artNum.substring(9,16));
				stmt_insert.execute();
				++i;
			}
		} catch(SQLException e) {
			System.out.println(" Invalid sql query");
		}
	}

	public static boolean isParsable(String input) {
		boolean parsable = true;
		try {
			Integer.parseInt(input);
		} catch(NumberFormatException e) {
			parsable = false;
		}
		return parsable;
	}
}
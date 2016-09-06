import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db {
	public Connection conn = null;

	public db() {
		Console console = System.console();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/Crawler?autoReconnect=true&useSSL=false";
			char pass[] = console.readPassword(" Enter pass: ");

			conn = DriverManager.getConnection(url, "root",  new String(pass));
			System.out.println(" Connection established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}

	public boolean runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}

	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}
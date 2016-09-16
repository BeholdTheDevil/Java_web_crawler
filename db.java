import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class db {
	public Connection conn = null;
	public MysqlDataSource ds = null;

	public db(String inputPass) {
		
		Console console = System.console();
		ds = new MysqlDataSource();
		ds.setURL("jdbc:mysql://localhost:3306/Crawler?autoReconnectForPools=true&useSSL=false");
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/Crawler?autoReconnectForPools=true&useSSL=false";
	/*		if(inputPass.length() == 0) {
				inputPass = new String(console.readPassword(" Enter password: "));
			}*/
			//conn = DriverManager.getConnection(url, "root",  inputPass);
			conn = ds.getConnection("root", inputPass);
		//	System.out.println(" Connection established");
		} catch (SQLException passexc) {
	//		System.out.println(" Invalid Password");
		} catch (NullPointerException nullexc) {
			throw nullexc;
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
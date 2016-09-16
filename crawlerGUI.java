import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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


public class crawlerGUI extends JFrame{
	private static JFrame mainframe;
	private static JTextArea headerLabel;
	private static JLabel statusLabel;
	private static JButton btnStart;
	private static JPanel controlPanel;
	private static JLabel resultLabel;
	private static JLabel msgLabel;
	private static JPasswordField textPassword;
	private static JPanel padding, padding_two;

	public static void main(String[] args) throws SQLException, IOException{															
																		//Setting up the mainframe
		mainframe = new JFrame("CrawlerGUI");  							//Initialize the frame with the title "FrameDemo"
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainframe.setExtendedState(JFrame.MAXIMIZED_BOTH); 				//Extendes the frame to take up the whole screen
		mainframe.setSize(800, 200);
		mainframe.setLocationRelativeTo(null);
		mainframe.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Container panel = mainframe.getContentPane();
		String query = "SELECT komplett_artnum FROM Components;";
		String query_two = "SELECT COUNT(*) FROM Components";
		String itemid_string = "";
		String address = "";

																		//Content below

/*		padding = new JPanel();
		padding.setBackground(Color.RED);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
//		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(padding); */

		textPassword = new JPasswordField();
		textPassword.setPreferredSize(new Dimension(125, 25));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.insets = new Insets(10,10,10,10);
		panel.add(textPassword, c);

		btnStart = new JButton("Start");								//Start button
		btnStart.setPreferredSize(new Dimension(50, 25));
		btnStart.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					btnStartActionPerformed(e);
				} catch (SQLException sqlexc) {
					sqlexc.printStackTrace();
				} catch (IOException ioexc) {
					ioexc.printStackTrace();
				}
			}
		});
		c.insets = new Insets(10, 100, 10, 10);
		c.weightx = 0.5;
		c.gridx = 1;
		panel.add(btnStart, c);

		headerLabel = new JTextArea(30, 60);
		headerLabel.setPreferredSize(new Dimension(570, 130));
		headerLabel.setEditable(false);
		headerLabel.append(" Hello");
		headerLabel.append(" there!\n");
		c.insets = new Insets(10,10,10,10);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		panel.add(headerLabel, c);


    	
     	
    	








																		//Content above 
   	    mainframe.setContentPane(panel);
   	    mainframe.pack();
		mainframe.setVisible(true);										//Sets the frame visibility to true
	}

	public static void btnStartActionPerformed(ActionEvent e) throws SQLException, IOException {
		String password = new String (textPassword.getPassword());
		if(password.length() > 0) {
			try {
				db data = new db(password);

				data.runSql2("TRUNCATE Record;");
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		}
	}

	public static void processPage(String URL, String password) throws SQLException, IOException {
		db data = new db(password);
		String sql_one = "SELECT * FROM Record WHERE URL = '"+URL+"'";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
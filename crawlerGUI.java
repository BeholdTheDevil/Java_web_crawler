import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class crawlerGUI extends JFrame{
	private static JFrame mainframe;
	private static JTextArea outputTextArea;
	private static JLabel statusLabel;
	private static JButton btnStart, btnUpdate;
	private static JPasswordField textPassword;
	private static GridBagConstraints c;
	private static Container panel;
	private static JProgressBar progressBar;
	private static JFormattedTextField numberBox;
	private static JComboBox<String> selectBox;

	public static void main(String[] args) throws SQLException, IOException{																
		initMainframe();												//Initialize Mainframe
		mainframe.setContentPane(panel);	
		panel.setBackground(Color.darkGray);

		initPasswordField();											//Password field init
		initProgressBar();												//Progressbar init
		initNumberBox();												//Numberbox init
		initBtnStart();													//Start button init
		initBtnUpdate();												//Update button init
		initOutputTextArea();											//TextArea init
 
   	    mainframe.pack();	
   	    mainframe.setLocationRelativeTo(null);		
		mainframe.setVisible(true);										//Sets the frame visibility to true
	}


	public static void printline(String printString) {
		outputTextArea.append(printString + "\n");
	}

	public static void initOutputTextArea() {
		outputTextArea = new JTextArea(10, 20);
		JScrollPane sp = new JScrollPane(outputTextArea);
		sp.setPreferredSize(new Dimension(650, 500));
		sp.setBackground(new Color(230, 230, 230));
		outputTextArea.setEditable(false);
		outputTextArea.append(" Welcome to the Java Web Crawler Graphical Interface Version 0.1\n");
		c.insets = new Insets(20,20,10,20);
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		panel.add(sp, c);
		c.ipadx = 0;
	}


	public static void initBtnUpdate() {
		btnUpdate = new JButton("Update");
		btnUpdate.setPreferredSize(new Dimension(50, 25));
		btnUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					btnUpdateActionPerformed(e);
				} catch (SQLException sqlexc) {
					sqlexc.printStackTrace();
				} catch (IOException ioexc) {
					ioexc.printStackTrace();
				}
			}
		});
		c.insets = new Insets(10, 10, 20, 20);
		c.gridx = 2;
		c.gridy = 2;
		panel.add(btnUpdate, c);
	}

	public static void initBtnStart() {
		btnStart = new JButton("Start");
		btnStart.setPreferredSize(new Dimension(50, 25));
		btnStart.addActionListener(new ActionListener() {
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
		c.insets = new Insets(10, 10, 0, 20);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 30;
		panel.add(btnStart, c);
	}

	public static void initMainframe() {
		mainframe = new JFrame("JWCGUI0.1");  							//Initialize the frame with the title "JWCGUI" + "Version"
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(800, 200);
		mainframe.setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		panel = mainframe.getContentPane();
	}

	public static void initNumberBox() {
		String[] choices = {"Screen", 
						    "Case", 
						    "GPU", 
						    "Storage(SSD/HDD)", 
		 				    "Controllercard", 
		 				    "Cooling", 
	 					    "Soundcard", 
	 					    "Ram", 
						    "Motherboard", 
						    "PSU", 
						    "Network", 
						    "CPU", 
						    "Software"};

		numberBox = new JFormattedTextField();
		numberBox.setPreferredSize(new Dimension(25, 25));
		numberBox.setBackground(new Color(230,230,230));
		numberBox.setEditable(true);
		numberBox.setMaximumSize(new Dimension(25, 25));

		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 20, 10);
		c.gridx = 1;
		c.gridy = 2;
		panel.add(numberBox, c);

		selectBox = new JComboBox<String>(choices);
		c.insets = new Insets(10, 20, 0, 10);
		c.gridy = 1;
  		panel.add(selectBox, c);
	}

	public static void initPasswordField() {
		textPassword = new JPasswordField(30);
		char temp = textPassword.getEchoChar();
		textPassword.setEchoChar((char)0);
		textPassword.setText("Password...");
		textPassword.setPreferredSize(new Dimension(125, 25));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.insets = new Insets(10 , 20, 20, 0);
		textPassword.setBackground(new Color(230, 230, 230));
		textPassword.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				textPassword.setEchoChar(temp);
				textPassword.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(textPassword.getPassword().length == 0) {
					textPassword.setEchoChar((char)0);
					textPassword.setText("Password...");
				}
			}
		});
		panel.add(textPassword, c);
	}

	public static void initProgressBar() throws SQLException {
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(125, 25));
		c.insets = new Insets(10, 20, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(progressBar, c);
	}

	public static void btnStartActionPerformed(ActionEvent e) throws SQLException, IOException {
		progressBar.setValue(0);
		String password = new String (textPassword.getPassword());
		String query = "SELECT komplett_artnum FROM Components;";			//SQL-Queries
		String itemid_string;
		String address;
		String query_two = "SELECT COUNT(*) FROM Components";
		if(password.length() > 0 && !password.equals("Password...")) {
			db data = new db(password);
			printline("Database connection established");
			btnUpdate.setEnabled(false);
			btnStart.setEnabled(false);
			textPassword.setEditable(false);
			try {
				data.runSql2("TRUNCATE Record;");
				ResultSet result = data.runSql(query);
				ResultSet res = data.runSql(query_two);
				int size = 0;
				int i = 0;
				while(res.next()) {
					size = res.getInt(1);
				}
				progressBar.setMaximum(size);
				while(result.next()) {
					itemid_string = result.getString("komplett_artnum");
					address = "http://www.komplett.se/products/" + itemid_string;
					processPage(address, password, data);
					progressBar.setValue(i);
					++i;
				}
			} catch (SQLException sqlexc) {

			}
			printline("Process finished\nCheck database for output");
		} else {
			printline(" Invalid password");
		}
	}

	public static void btnUpdateActionPerformed(ActionEvent e) throws SQLException, IOException {
		progressBar.setValue(0);
		String password = new String (textPassword.getPassword());
		if(password.length() > 0 && !password.equals("Password...")) {
			boolean validInput = false;
			String site = "https://www.komplett.se/category/";
			String hits = "";
			String[] komplett_index_num = {"10392/bildskarm", 
										   "10149/chassibarebone",
										   "10412/grafikkort", 
										   "10088/haarddisk", 
										   "10204/kontrollerkort", 
										   "10462/kylningvattenkylning", 
										   "11211/ljudkort", 
										   "11209/minne", 
										   "10111/moderkort", 
										   "10057/nataggregat", 
										   "10273/natverk", 
										   "11204/processor", 
										   "10335/programvara"};

			try {
				if(tryParseInt(numberBox.getText()) || numberBox.getText().length() == 0) {
					db data = new db(password);
					printline("Database connection established");
					printline("Article numbers collected: \n");
					data.runSql2("TRUNCATE Components;");
					if(numberBox.getText().length() > 0) {
						hits = numberBox.getText();
					}
					printline(hits);
					int selectedIndex = selectBox.getSelectedIndex();
					getArtNums(site + komplett_index_num[selectedIndex] + "?hits=" + hits, data);
					} else {
						printline("Invalid number entry");
						numberBox.setText("");
					}
				} catch (SQLException exc) {
				exc.printStackTrace();
				}
			} else {
			printline(" Invalid password");
		}
	}

	public static boolean tryParseInt(String value) {  
     	try {  
         	Integer.parseInt(value);  
        	return true;  
      	} catch (NumberFormatException e) {  
        	return false;  
    	}
	}

	public static void getArtNums(String URL, db data) throws SQLException, IOException{
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
				printline(artNum.substring(9,16));
				stmt_insert.execute();
				progressBar.setValue(i);
				++i;
			}
		} catch(SQLException e) {
			printline("Invalid SQL query");
		}
	}

	public static void processPage(String URL, String password, db data) throws SQLException, IOException {
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
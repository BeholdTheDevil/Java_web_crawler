import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.ArrayIndexOutOfBoundsException;


public class crawlerGUI extends JFrame{
	private static JFrame mainframe;
	private static JTextPane outputTextArea;
	private static JLabel statusLabel;
	private static JButton btnStart, btnUpdate;
	private static JPasswordField textPassword;
	private static GridBagConstraints c;
	private static Container panel;
	private static JProgressBar progressBar;
	private static JFormattedTextField numberBox;
	private static JComboBox<String> selectBox;
	private static JRadioButton checkboxtwo, checkboxone;
	private static StyledDocument styledoc;
	private static Style style;
	public static String version = "0.4";

	public static void main(String[] args) throws SQLException, IOException{
		initUI();																
	}

	public static void initUI() throws SQLException, IOException{
		initMainframe();												//Initialize Mainframe
		mainframe.setContentPane(panel);	
		panel.setBackground(Color.darkGray);

		initPasswordField();											//Password field init
		initProgressBar();												//Progressbar init
		initNumberBox();												//Numberbox init
		initBtnStart();													//Start button init
		initBtnUpdate();												//Update button init
		initCheckBoxOne();												//Checkbox one init
		initCheckBoxTwo();												//Checkbox two init
		initOutputTextArea();											//TextArea init
 
   	    mainframe.pack();	
   	    mainframe.setLocationRelativeTo(null);		
		mainframe.setVisible(true);										//Sets the frame visibility to true
	}

	public static void printline(String printString, int color) {
		int[][] colors= {{0,0,0},
						 {0,255,0},
						 {255,0,0}};
		style = styledoc.addStyle("FlexStyle", null);
		StyleConstants.setForeground(style, new Color(colors[color][0],colors[color][1], colors[color][2]));
		try {
			styledoc.insertString(styledoc.getLength(), printString + "\n", style);
		} catch (final BadLocationException ble) {

		}
	}

	public static void initOutputTextArea() {
		outputTextArea = new JTextPane();
		JScrollPane sp = new JScrollPane(outputTextArea);
		sp.setPreferredSize(new Dimension(650, 500));
		sp.setBackground(new Color(230, 230, 230));
		sp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		outputTextArea.setEditable(false);
		outputTextArea.setText(" Welcome to the Java Web Crawler Graphical Interface Version " + (version) + "\n");
		styledoc = outputTextArea.getStyledDocument();
		c.insets = new Insets(20,20,10,20);
		c.gridwidth = 5;
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
		c.insets = new Insets(10, 10, 20, 10);
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
		c.insets = new Insets(10, 10, 0, 10);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 40;
		panel.add(btnStart, c);
	}

	public static void initCheckBoxTwo() {
		checkboxtwo = new JRadioButton();
   		checkboxtwo.setSelected(false);
		checkboxtwo.setBackground(Color.darkGray);
		checkboxtwo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(checkboxtwo.isSelected()) {
					checkboxtwo.setSelected(true);
				} else {
					checkboxtwo.setSelected(false);
				}
			}
		});
		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(10, 10, 20, 20);
		c.ipadx = 0;
		panel.add(checkboxtwo, c);
	}

	public static void initCheckBoxOne() {
		checkboxone = new JRadioButton();
   		checkboxone.setSelected(false);
   		checkboxone.setBackground(Color.darkGray);
		checkboxone.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(checkboxone.isSelected()) {
					checkboxone.setSelected(true);
				} else {
					checkboxone.setSelected(false);
				}
			}
		});
		c.gridx = 3;
		c.gridy = 1;
		c.insets = new Insets(10, 10, 0, 20);
		c.ipadx = 0; 
		panel.add(checkboxone, c);
	}

	public static void initMainframe() {
		mainframe = new JFrame("JWCGUI " + version);  							//Initialize the frame with the title "JWCGUI" + "Version"
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
	 					    "RAM", 
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

	public static void btnStartActionPerformed(ActionEvent e) throws SQLException, IOException, NullPointerException {
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		mainframe.setCursor(cursor);
		progressBar.setValue(0);
		String password = new String (textPassword.getPassword());
		String query = "SELECT komplett_artnum FROM Components;";			//SQL-Queries
		String itemid_string;
		String address;
		ResultSet result = null;
		ResultSet res = null;
		String query_two = "SELECT COUNT(*) FROM Components";
		boolean error = false;
		PreparedStatement stmt_insert = null;
		if(password.length() > 0 && !password.equals("Password...")) {
			outputTextArea.setText(null);
			printline(" Welcome to the Java Web Crawler Graphical Interface Version " + version + "\n", 0);
			btnUpdate.setEnabled(false);
			btnStart.setEnabled(false);
			textPassword.setEditable(false);
			try {
				db data = new db(password);
				stmt_insert = data.conn.prepareStatement(query_two, Statement.RETURN_GENERATED_KEYS);
				printline("Database connection established", 1);
				if(checkboxone.isSelected()) {
					data.runSql2("TRUNCATE Record;");
				}
				result = data.runSql(query);
				res = data.runSql(query_two);
				res.next();
				int size = res.getInt(1);
				int i = 0;
				progressBar.setMaximum(size);
				printline("Prices: ", 0);
				while(result.next()) {
					try {
						itemid_string = result.getString("komplett_artnum");
						address = "http://www.komplett.se/products/" + itemid_string;
						printline((i+1) + " " + processPage(address, password, data), 0);
					} catch (java.net.UnknownHostException unknownHost){
						printline((i+1) + " Invalid URL or no connection", 2);
					}
					data.conn.commit();
					++i;
					progressBar.setValue(i);
				}
				data.conn.commit();
			} catch (SQLException sqlexc) {

			} catch(NullPointerException npe) {
				printline("Connection to database could not be established", 2);
				System.out.println(npe);
				error = true;
			} finally {
				if(result != null) {
					result.close();
				}
				if(res != null) {
					res.close();
				}
				if(stmt_insert != null) {
					stmt_insert.close();
				}
			}
			if(!error) {
				printline("Process finished\nCheck database for output", 1);
			}
		} else {
			printline(" Invalid password", 2);
		}
		btnUpdate.setEnabled(true);
		btnStart.setEnabled(true);
		textPassword.setEditable(true);
		cursor = Cursor.getDefaultCursor();
		mainframe.setCursor(cursor);
	}

	public static void btnUpdateActionPerformed(ActionEvent e) throws SQLException, IOException {
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		mainframe.setCursor(cursor);
		progressBar.setValue(0);
		String password = new String (textPassword.getPassword());
		if(password.length() > 0 && !password.equals("Password...")) {
			outputTextArea.setText(null);
			printline(" Welcome to the Java Web Crawler Graphical Interface Version " + version + "\n", 0);
			boolean validInput = false;
			String site = "https://www.komplett.se/category/";
			String hits = "";
			String[] komplett_index_num = {"10392/datorutrustning/bildskarm", 
										   "10149/datorutrustning/chassi/barebone",
										   "10412/datorutrustning/grafikkort", 
										   "10088/datorutrustning/haarddisk", 
										   "10204/datorutrustning/kontrollerkort", 
										   "10462/datorutrustning/kylning/vattenkylning", 
										   "11211/datorutrustning/ljudkort", 
										   "11209/datorutrustning/minne", 
										   "10111/datorutrustning/moderkort", 
										   "10057/datorutrustning/nataggregat", 
										   "10273/datorutrustning/natverk", 
										   "11204/datorutrustning/processor", 
										   "10335/datorutrustning/programvara"};
			btnUpdate.setEnabled(false);
			btnStart.setEnabled(false);
			textPassword.setEditable(false);
			try {
				if(tryParseInt(numberBox.getText()) || numberBox.getText().length() == 0) {
					db data = new db(password);
					printline("Database connection established", 1);
					if(checkboxtwo.isSelected()) {
						data.runSql2("TRUNCATE Components;");
					}
					if(numberBox.getText().length() > 0) {
						hits = numberBox.getText();
					} else {
						progressBar.setMaximum(24);
					}
					int selectedIndex = selectBox.getSelectedIndex();
					getArtNums(site + komplett_index_num[selectedIndex] + "?hits=" + hits, data, Integer.parseInt(hits));
					} else {
						printline("Invalid number entry", 2);
						numberBox.setText("");
					}
				} catch (SQLException exc) {
				exc.printStackTrace();
				} catch (NumberFormatException nfe) {

				}
				printline("Process finished\nCheck database for output", 1);
			} else {
			printline(" Invalid password", 2);
		}
		cursor = Cursor.getDefaultCursor();
		mainframe.setCursor(cursor);
		btnUpdate.setEnabled(true);
		btnStart.setEnabled(true);
		textPassword.setEditable(true);
	}

	public static boolean tryParseInt(String value) {  
     	try {  
         	Integer.parseInt(value);  
        	return true;  
      	} catch (NumberFormatException e) {  
        	return false;  
    	}
	}

	public static void getArtNums(String URL, db data, int hits) throws SQLException, IOException{
		int i = 0;
		int j = 0;
		String sql_insert = "INSERT INTO `Crawler`.`Components` (`komplett_artnum`, `type`) VALUES (?, ?);";
		Document doc = null;
		Elements elements = null;
		PreparedStatement stmt_insert = null;

		printline("Article numbers collected: \n", 0);
		try {
			stmt_insert = data.conn.prepareStatement(sql_insert, Statement.RETURN_GENERATED_KEYS);
			doc = Jsoup.connect(URL).timeout(0).get();
			elements = doc.select("a.product-link.image-container");
			for(Element element : elements) {
				++j;
			}
			if(hits > j) {
				progressBar.setMaximum(j);
				printline("Requested number of objects does not exist, setting to maximum amount", 2);
			} else {
				progressBar.setMaximum(hits);
			}
			for(Element element : elements) {
				Elements link = doc.select("a.product-link.image-container[href]").eq(i);
				String artNum = link.attr("href");
				stmt_insert.setString(1, artNum.substring(9,16));
				stmt_insert.setString(2, (String)selectBox.getSelectedItem());
				printline((i+1) + " " + artNum.substring(9,16), 0);
				stmt_insert.execute();
				++i;
				progressBar.setValue(i);
			}
		} catch(SQLException e) {
			printline("Invalid SQL query", 2);
		} catch(java.net.UnknownHostException unknownHost) {
			printline("Connection to website could not be established", 2);
		}finally {
			if(stmt_insert != null) {
				stmt_insert.close();
			}
		}
	}

	public static String processPage(String URL, String password, db data) throws SQLException, IOException {
		String sql_one = "SELECT * FROM Record WHERE URL = '"+URL+"'";
		ResultSet rs = data.runSql(sql_one);
		Document doc = null;
		PreparedStatement stmt = null;
		sql_one = "INSERT INTO `Crawler`.`Record` (`URL`, `price`, `name`) VALUES (?, ?, ?)";
		data.conn.setAutoCommit(false);
		try {
			if(!rs.next()) {
				stmt = data.conn.prepareStatement(sql_one, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, URL);

				doc = Jsoup.connect(URL).get();

				String price = doc.select("span.product-price-now").first().text();
				String name = doc.select("h1.product-main-info-webtext1").first().text();

				stmt.setString(2, price);
				stmt.setString(3, name);
				stmt.execute();
				return price;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
		}
		return null;
	}
}
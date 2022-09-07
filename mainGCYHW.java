import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

import java.time.LocalDateTime;

public class mainGCYHW {

	public static OracleDataSource ods;
	public static Connection conn;
	public static Statement stmt;
		
	// Login window
	public static JFrame frame_login = new JFrame();
	public static JButton login = new JButton("Log in");
	//public static JTextField username = new JTextField(15);
	//public static JPasswordField password = new JPasswordField(15);
	 
	public static JLabel login_info = new JLabel();
	 
	//public static JTextField deptName = new JTextField(15);
	//public static JButton query = new JButton("Run Query");
	
	// Card swipe window
	public static JFrame frame_cardswipe = new JFrame();
	public static JTextField cardswipe_info = new JTextField(15);
	public static JButton input_button_cardswipe = new JButton("Add ID");
	public static JButton cardswipe_next = new JButton("Next");
	public static JLabel cardswipe = new JLabel();
	 
	//public static LocalDateTime obj_datetime;
	 
	// SQL window
	public static JFrame frame_oraclesql = new JFrame();
	public static JLabel filter_by = new JLabel("Filter access history by:       ");
	public static JLabel filter_id = new JLabel("ID:");
	public static JTextField id_filter_text = new JTextField(15);
	public static JLabel filter_date = new JLabel("Date (year):");
	public static JTextField id_filter_date = new JTextField(15);
	public static JLabel filter_range = new JLabel("Range (years ago):      ");
	public static JTextField id_filter_range = new JTextField(15);
	public static JLabel swipes_oraclesql = new JLabel("User Access History:");
	public static JButton show_swipes_oraclesql = new JButton("Display");
	public static JButton back_button_oraclesql = new JButton("Previous");
	 
	public static JLabel access_history;
	 
	public static boolean already_loaded = false;
	 
	//public static String filter_student_id = "";
	//public static String filter_access_date = "";
	//public static int filter_date_range = -1;
 
	// Code to set up the initial login window
	public static void init_login_window()  {
		 
		login.addActionListener(new ActionListener() { 
			
		public void actionPerformed(ActionEvent event) {
			try {
			loginAction(event);
			} catch (Exception E) {};
			}
		});
			 
		frame_login.setLayout(new FlowLayout());
		frame_login.setTitle("Connect to PSUHB");
		frame_login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_login.setSize(400,600);
		   
		//frame_login.add(new JLabel("Username: "));
		//frame_login.add(username);
		//frame_login.add(new JLabel("Password: "));
		//frame_login.add(password);
		frame_login.add(login);
		   
		frame_login.add(login_info);
		   
		frame_login.setResizable(true);
		frame_login.setVisible(true);
		frame_login.setBackground(Color.WHITE);
		 
	   
	}
 
	// Add student id and date to database
	public static void add_ID(String id) throws Exception  {
	
		System.out.println("add id");
		
		//Statement stmt = conn.createStatement();
		
		System.out.println("past create statement");
		
		//String id = cardswipe_info.getText();
		String date_now = LocalDateTime.now().toString();
		String date_year = date_now.substring(0, 4);
		
		stmt.executeUpdate("INSERT INTO PSUstudents(student_id, access_date) " +
		"VALUES ('" + id + "', '" + date_year + "') ");
		
		System.out.println("past execute update");
	}
 
	// Code to set up the card swipe window
	public static void init_cardswipe_window()  {
		 
		input_button_cardswipe.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent event) {
			try {
			cardswipe(event);
			} catch (Exception E) {};
			}
		});
			 
		cardswipe_next.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent event) {
			try {
			cardswipe_next(event);
			} catch (Exception E) {};
			}
		});
			 
		frame_cardswipe.setLayout(new FlowLayout());
		frame_cardswipe.setTitle("Swipe ID Card");
		frame_cardswipe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_cardswipe.setSize(400,600);
		   
		frame_cardswipe.add(new JLabel("Swipe Your Card: "));
		frame_cardswipe.add(input_button_cardswipe);
		frame_cardswipe.add(cardswipe_info);
		frame_cardswipe.add(cardswipe_next);
		frame_cardswipe.add(cardswipe);
		   
		frame_cardswipe.setResizable(true);
		frame_cardswipe.setVisible(true);
		frame_cardswipe.setBackground(Color.WHITE);
		 
	}
 
	// Code to set up the Oracle SQl window
	// GUI allows you to 
	public static void init_oraclesql_window()  {
		 
		frame_oraclesql.dispose();
		frame_oraclesql = new JFrame();
		
		back_button_oraclesql.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent event) {
			try {
			backto_cardswipe(event);
			} catch (Exception E) {};
			}
		});
		
		show_swipes_oraclesql.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent event) {
				try {
				queryAction(event);
				} catch (Exception E) {};
				}
			});
			 
		frame_oraclesql.setLayout(new FlowLayout());
		frame_oraclesql.setTitle("PSHB Oracle SQL Database: View SUN Lab access history");
		frame_oraclesql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_oraclesql.setSize(700,900);	// !!! Later will want to add a scrolling component,
			 								// if swing allows that
		frame_oraclesql.add(filter_by);
		frame_oraclesql.add(filter_id);
		frame_oraclesql.add(id_filter_text);
		frame_oraclesql.add(filter_date);
		frame_oraclesql.add(id_filter_date);
		frame_oraclesql.add(filter_range);
		frame_oraclesql.add(id_filter_range);
		frame_oraclesql.add(swipes_oraclesql);
		frame_oraclesql.add(show_swipes_oraclesql);
		frame_oraclesql.add(back_button_oraclesql);
		   
		frame_oraclesql.setResizable(true);
		frame_oraclesql.setVisible(true);
		frame_oraclesql.setBackground(Color.WHITE);
			 
		already_loaded = true;
	}
 
	// Button code to log into a Sun Lab computer 
	public static void loginAction(ActionEvent event) throws Exception {
		//System.out.println("blah blah blah");
		
		//ods.setURL("jdbc:oracle:thin:Local@localhost:1521/xe");
		
		ods = new OracleDataSource();
		ods.setDatabaseName("Local");
		ods.setURL("jdbc:oracle:thin:SYSTEM/Klz3_gm59*@localhost:1521:xe");

		Connection conn = ods.getConnection();
		
		//ods.setDatabaseName("Local");
		System.out.println("database: " + ods.getDatabaseName());
		
		if (conn.isValid(15))  {
			
			System.out.println("Connected");
			login_info.setText("Connection success!");
			
			stmt = conn.createStatement();
			
			frame_login.setVisible(false);
				   
			init_cardswipe_window();
			
		}
		else  {
			
			System.out.println("Failed to connect!");
			login_info.setText("Connection failure!");
		}
	   
	}

	// Add a user ID and time stamp to the database
	public static void cardswipe(ActionEvent event) throws Exception {
		   
		// Adds info to database
		add_ID(cardswipe_info.getText());
	}
 
	// Go from card swipe window to Oracle SQL window
	public static void cardswipe_next(ActionEvent event) throws Exception {
		   
		frame_cardswipe.setVisible(false);
			   
		if (already_loaded == false)  init_oraclesql_window();
		else  frame_oraclesql.setVisible(true);
	}
 
	//Go from card swipe window to Oracle SQL window
	public static void  backto_cardswipe(ActionEvent event) throws Exception {
		   
		frame_oraclesql.setVisible(false);
			   
		frame_cardswipe.setVisible(true);
	
	}

	// SQL query action
	// Show ids and access dates but filter
	public static void queryAction(ActionEvent event) throws Exception {
	
		System.out.println("query");
		
		ResultSet rset;
		
		System.out.println("past create statement");
		
		boolean id_filtered = false;
		boolean date_filtered = false;
		
		String filtered_by_id = "";
		
		if (!id_filter_text.getText().equalsIgnoreCase("")) {
			filtered_by_id = " where student_id = '" + id_filter_text.getText() + "'";
			
			id_filtered = true;
		}
		
		String filtered_by_date = "";
		
		if (!id_filter_date.getText().equalsIgnoreCase(""))  {
			if (id_filtered) {
				filtered_by_date = " and access_date = '" + id_filter_date.getText() + "'";
			}
			else  {
				filtered_by_date = " where access_date = '" + id_filter_date.getText() + "'";
			}
			
			date_filtered = true;
			
		}
		
		String filtered_by_range = "";
		if (!id_filter_range.getText().equalsIgnoreCase(""))  {
			int range = 2022 - Integer.parseInt(id_filter_range.getText());
			
			if (Integer.parseInt(id_filter_range.getText()) <= 5 
					&& Integer.parseInt(id_filter_range.getText()) >= 0)  {
				
				if (id_filtered || date_filtered) {
					filtered_by_range = " and access_date >= '" + range + "'";
				}
				else  {
					filtered_by_range = " where access_date >= '" + range + "'";
				}
			}
		
		}
		rset = stmt.executeQuery("select * from PSUstudents" + filtered_by_id + filtered_by_date
				+ filtered_by_range);
		
		System.out.println("past execute query");
		System.out.println(id_filter_text.getText());
		
		// Clear the Jlabels
		init_oraclesql_window();
		   
		while (rset.next() ) {
		    
			String col_a = rset.getString(1);
			String col_b = rset.getString(2);
			
			System.out.println(col_a + " " +
			                  col_b + " ");
			
			frame_oraclesql.add(new JLabel("       " + col_a + " " + col_b + "       "));
		}
		
		frame_oraclesql.setVisible(true);
	
	}

	public static void main(String[] args) throws Exception {
		
		//System.out.println(LocalDateTime.now());
		
		init_login_window();
	    
	}
 
} 

package yt;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class App extends JFrame {

	private JPanel contentPane;
	
	//comboBoxes
	private JComboBox comboBox_from = new JComboBox();
	private JComboBox comboBox_to = new JComboBox();
	private JComboBox comboBox_time = new JComboBox();
	private JTextArea textArea_price = new JTextArea();
	
	//textAreas
	private JTextArea textArea_name = new JTextArea();
	private JTextArea textArea_date = new JTextArea();
	private JTextArea textArea_message = new JTextArea();
	
	//buttons
	private JButton btnBuyTicket = new JButton("Buy Ticket");
	private JButton btnGetPrice = new JButton("Get Price");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					
					//Station from is loaded 
					frame.fillComboFrom();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//get station name from db
	public void fillComboFrom() {
		String sql = "select * from station";
		Connection conn = null;
		try {
			//get connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8080/ticket_master?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			//statement
			Statement myStmt = conn.createStatement();
			//execute query
			ResultSet rs = myStmt.executeQuery(sql);
			//process the result
			while (rs.next()) {
				comboBox_from.addItem(rs.getString("station_name"));
			}
		}catch (Exception e) {
			e.getStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//the content of the comboBox_to will change based on the item selected in comboBox_from
	public void fillComboTo() {
		Object from = comboBox_from.getSelectedItem();
		from.toString();
		String sql = "SELECT * FROM schedule JOIN station on schedule.station_from = station.station_name where station_from = \""+ from + "\""+ "group by station_to" ;
		Connection conn = null;
		try {
			//get connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8080/ticket_master?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			//statement
			Statement myStmt = conn.createStatement();
			//execute query
			ResultSet rs = myStmt.executeQuery(sql);
			//process the result
			while (rs.next()) {
				comboBox_to.addItem(rs.getString("station_to"));
			}
		}catch (Exception e) {
			e.getStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//get scheduled time form db
	public void search() {
		Object from = comboBox_from.getSelectedItem();
		Object to = comboBox_to.getSelectedItem();
		from.toString();
		to.toString();
		String sql = "SELECT * FROM schedule JOIN station on schedule.station_from = station.station_name where station_from = \"" + from + "\""+ "AND station_to =\""+ to +"\"";
		Connection conn = null;
		try {
			//get connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8080/ticket_master?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			//statement
			Statement myStmt = conn.createStatement();
			//execute query
			ResultSet rs = myStmt.executeQuery(sql);
			//process the result
			while (rs.next()) {
				comboBox_time.addItem(rs.getString("scheduled_time"));
			}
		}catch (Exception e) {
			e.getStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//get price from db
	public void getPrice() {
		Object from = comboBox_from.getSelectedItem();
		Object to = comboBox_to.getSelectedItem();
		Object time = comboBox_time.getSelectedItem();
		from.toString();
		to.toString();
		time.toString();
		
		String sql = "SELECT * FROM schedule JOIN station on schedule.station_from = station.station_name where station_from = \"" + from + "\"" + "AND station_to = \"" + to + "\"";
		Connection conn = null;
		try {
			//get connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8080/ticket_master?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			//statement
			Statement myStmt = conn.createStatement();
			//execute query
			ResultSet rs = myStmt.executeQuery(sql);
			//process the result
			while (rs.next()) {
				textArea_price.setText(rs.getString("ticket_price"));
			}
		}catch (Exception e) {
			e.getStackTrace();
			textArea_message.setText(e.getMessage());
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//INSERT query is performed after click
	public void buyTicket() {
		Object from = comboBox_from.getSelectedItem();
		Object to = comboBox_to.getSelectedItem();
		Object time = comboBox_time.getSelectedItem();
		from.toString();
		to.toString();
		time.toString();
		String name = textArea_name.getText();
		String date = textArea_date.getText();
		String price = textArea_price.getText();
		
		Random rand = new Random();
		int tid = rand.nextInt(9999);
		
		
			String sql = "INSERT INTO transcation (tid, cname, station_from, station_to, time, date, price) "
					+ "VALUES ("+ tid +", \"" +name + "\", \""+from + "\", \""+to + "\", \"" + time + "\", \"" + date + "\", " + price +")";
			Connection conn = null;
			try {
				//get connection
				conn = DriverManager.getConnection("jdbc:mysql://localhost:8080/ticket_master?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
				//statement
				Statement myStmt = conn.createStatement();
				//execute query
				myStmt.executeUpdate(sql);
				
				}catch (Exception e) {
				e.getStackTrace();
				}finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	//clear the form
	public void cancel() {
		textArea_name.setText("");
		textArea_price.setText("");
		textArea_date.setText("");
		textArea_message.setText("");
		
		comboBox_time.removeAllItems();
		
		
		
	}
	/**
	 * Create the frame.
	 */
	public App() {
		setTitle("Ticket App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{110, 110, 110, 110, 0};
		gbl_contentPane.rowHeights = new int[]{52, 52, 52, 52, 52, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblCustomerName = new JLabel("Customer Name");
		GridBagConstraints gbc_lblCustomerName = new GridBagConstraints();
		gbc_lblCustomerName.fill = GridBagConstraints.BOTH;
		gbc_lblCustomerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomerName.gridx = 0;
		gbc_lblCustomerName.gridy = 0;
		contentPane.add(lblCustomerName, gbc_lblCustomerName);
		
		
		GridBagConstraints gbc_textArea_name = new GridBagConstraints();
		gbc_textArea_name.fill = GridBagConstraints.BOTH;
		gbc_textArea_name.insets = new Insets(0, 0, 5, 5);
		gbc_textArea_name.gridx = 1;
		gbc_textArea_name.gridy = 0;
		
		//when lost focus, check 3 textArea to enable or disable the buy ticket button
		textArea_name.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(!textArea_name.getText().equals("") && !textArea_date.getText().equals("") && !textArea_price.getText().equals("")) {
					btnBuyTicket.setEnabled(true);
				}else {
					btnBuyTicket.setEnabled(false);
				}
			}
		});
		contentPane.add(textArea_name, gbc_textArea_name);
		
		JLabel lblTicketPrice = new JLabel("Ticket Price");
		GridBagConstraints gbc_lblTicketPrice = new GridBagConstraints();
		gbc_lblTicketPrice.fill = GridBagConstraints.BOTH;
		gbc_lblTicketPrice.insets = new Insets(0, 0, 5, 5);
		gbc_lblTicketPrice.gridx = 2;
		gbc_lblTicketPrice.gridy = 0;
		contentPane.add(lblTicketPrice, gbc_lblTicketPrice);
		
		
		textArea_price.setEditable(false);
		GridBagConstraints gbc_textArea_price = new GridBagConstraints();
		gbc_textArea_price.fill = GridBagConstraints.BOTH;
		gbc_textArea_price.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_price.gridx = 3;
		gbc_textArea_price.gridy = 0;
		contentPane.add(textArea_price, gbc_textArea_price);
		
		JLabel lblStationfrom = new JLabel("Station (From)");
		GridBagConstraints gbc_lblStationfrom = new GridBagConstraints();
		gbc_lblStationfrom.fill = GridBagConstraints.BOTH;
		gbc_lblStationfrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblStationfrom.gridx = 0;
		gbc_lblStationfrom.gridy = 1;
		contentPane.add(lblStationfrom, gbc_lblStationfrom);
		
		
		GridBagConstraints gbc_comboBox_from = new GridBagConstraints();
		gbc_comboBox_from.fill = GridBagConstraints.BOTH;
		gbc_comboBox_from.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_from.gridx = 1;
		gbc_comboBox_from.gridy = 1;
		
		//when item changed in comboBox_from, refill comboBox_to
		comboBox_from.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				comboBox_to.removeAllItems();
				fillComboTo();
			}
		});
		contentPane.add(comboBox_from, gbc_comboBox_from);
		
		JLabel lblScheduledTime = new JLabel("Scheduled Time");
		GridBagConstraints gbc_lblScheduledTime = new GridBagConstraints();
		gbc_lblScheduledTime.fill = GridBagConstraints.BOTH;
		gbc_lblScheduledTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblScheduledTime.gridx = 2;
		gbc_lblScheduledTime.gridy = 1;
		contentPane.add(lblScheduledTime, gbc_lblScheduledTime);
		
		
		GridBagConstraints gbc_comboBox_time = new GridBagConstraints();
		gbc_comboBox_time.fill = GridBagConstraints.BOTH;
		gbc_comboBox_time.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_time.gridx = 3;
		gbc_comboBox_time.gridy = 1;
		
		//when item changed in comboBox_time, enable get price button
		comboBox_time.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				btnGetPrice.setEnabled(true);
			}
		});
		contentPane.add(comboBox_time, gbc_comboBox_time);
		
		JLabel lblStationto = new JLabel("Station (To)");
		GridBagConstraints gbc_lblStationto = new GridBagConstraints();
		gbc_lblStationto.anchor = GridBagConstraints.EAST;
		gbc_lblStationto.fill = GridBagConstraints.VERTICAL;
		gbc_lblStationto.insets = new Insets(0, 0, 5, 5);
		gbc_lblStationto.gridx = 0;
		gbc_lblStationto.gridy = 2;
		contentPane.add(lblStationto, gbc_lblStationto);
		
		
		GridBagConstraints gbc_comboBox_to = new GridBagConstraints();
		gbc_comboBox_to.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_to.fill = GridBagConstraints.BOTH;
		gbc_comboBox_to.gridx = 1;
		gbc_comboBox_to.gridy = 2;
		
		contentPane.add(comboBox_to, gbc_comboBox_to);
		
		JLabel lblTravelDate = new JLabel("Travel Date");
		GridBagConstraints gbc_lblTravelDate = new GridBagConstraints();
		gbc_lblTravelDate.fill = GridBagConstraints.BOTH;
		gbc_lblTravelDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblTravelDate.gridx = 2;
		gbc_lblTravelDate.gridy = 2;
		contentPane.add(lblTravelDate, gbc_lblTravelDate);
		
		
		GridBagConstraints gbc_textArea_date = new GridBagConstraints();
		gbc_textArea_date.fill = GridBagConstraints.BOTH;
		gbc_textArea_date.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_date.gridx = 3;
		gbc_textArea_date.gridy = 2;
		
		//when lost focus,  check 3 textArea to enable or disable the buy ticket button
		textArea_date.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(!textArea_name.getText().equals("") && !textArea_date.getText().equals("") && !textArea_price.getText().equals("")) {
					btnBuyTicket.setEnabled(true);
				}else {
					btnBuyTicket.setEnabled(false);
				}
			}
		});
		contentPane.add(textArea_date, gbc_textArea_date);
		
		JLabel lblSystemMessage = new JLabel("System Message");
		GridBagConstraints gbc_lblSystemMessage = new GridBagConstraints();
		gbc_lblSystemMessage.fill = GridBagConstraints.BOTH;
		gbc_lblSystemMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblSystemMessage.gridx = 0;
		gbc_lblSystemMessage.gridy = 3;
		contentPane.add(lblSystemMessage, gbc_lblSystemMessage);
		
		
		textArea_message.setEditable(false);
		GridBagConstraints gbc_textArea_message = new GridBagConstraints();
		gbc_textArea_message.gridwidth = 3;
		gbc_textArea_message.fill = GridBagConstraints.BOTH;
		gbc_textArea_message.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_message.gridx = 1;
		gbc_textArea_message.gridy = 3;
		contentPane.add(textArea_message, gbc_textArea_message);
		
		JButton btnSearch = new JButton("Search");
		
		//search button on click
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox_time.removeAllItems();
				search();
			}
		});
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.fill = GridBagConstraints.BOTH;
		gbc_btnSearch.insets = new Insets(0, 0, 0, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 4;
		contentPane.add(btnSearch, gbc_btnSearch);
		
		
		btnGetPrice.setEnabled(false);
		
		//get price button on click
		btnGetPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getPrice();
				textArea_message.setText("The price is shown above.");
			}
		});
		GridBagConstraints gbc_btnGetPrice = new GridBagConstraints();
		gbc_btnGetPrice.insets = new Insets(0, 0, 0, 5);
		gbc_btnGetPrice.fill = GridBagConstraints.BOTH;
		gbc_btnGetPrice.gridx = 1;
		gbc_btnGetPrice.gridy = 4;
		contentPane.add(btnGetPrice, gbc_btnGetPrice);
		btnBuyTicket.setEnabled(false);
		
		
		//buy ticket button on click
		btnBuyTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buyTicket();
				textArea_message.setText("Your ticket has been booked!");
			}
		});
		GridBagConstraints gbc_btnBuyTicket = new GridBagConstraints();
		gbc_btnBuyTicket.insets = new Insets(0, 0, 0, 5);
		gbc_btnBuyTicket.fill = GridBagConstraints.BOTH;
		gbc_btnBuyTicket.gridx = 2;
		gbc_btnBuyTicket.gridy = 4;
		contentPane.add(btnBuyTicket, gbc_btnBuyTicket);
		
		JButton btnCancel = new JButton("Cancel");
		
		//cancel button on click
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.fill = GridBagConstraints.BOTH;
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 4;
		contentPane.add(btnCancel, gbc_btnCancel);
	}

}

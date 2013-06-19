package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class Frame extends JFrame {
	private static JTable table;
	private static JLabel lblName;
	private static JLabel lblOnline;
	private static JScrollPane sc;
	private static HashMap<String, String> idents = new HashMap<String, String>();

	private static int serverAmt = 0, playerAmt = 0;

	public FrameConfiguration configFrame;
	public FrameConnection connectFrame;
	
	public static Configuration lc;

	public Frame(String title, Image icon) {
		lc = new Configuration(new File(System.getProperty("user.home"), "rs_config.ini"));
		if(!lc.exists) {
			String x = JOptionPane.showInputDialog(this, "Could not find Ace of Spades Path.\n Please insert the path to Ace of Spades:",
					"C:\\Ace of Spades\\");
			lc.put("aos-dir", x);
			lc.save();
		}
		configFrame = new FrameConfiguration(icon);
		connectFrame = new FrameConnection(icon);
		setTitle(title);
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(768, 522));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JMenuBar menuBar = new JMenuBar();
		springLayout.putConstraint(SpringLayout.NORTH, menuBar, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, menuBar, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, menuBar, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(menuBar);

		JMenu mnMenu = new JMenu("File");
		menuBar.add(mnMenu);

		JMenuItem mntmLocalhostConnect = new JMenuItem("Localhost");
		mntmLocalhostConnect.setAction(new AbstractAction("Localhost") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.connectToLocalhost();
			}
		});
		mnMenu.add(mntmLocalhostConnect);

		JMenuItem mntmAddFavorite = new JMenuItem("Add Favorite");
		mnMenu.add(mntmAddFavorite);

		JMenuItem mntmOpenAddress = new JMenuItem("Open Address");
		mntmOpenAddress.setAction(new AbstractAction("Open Address") {
			public void actionPerformed(ActionEvent e) {
				connectFrame.setVisible(true);
			}
		});
		mnMenu.add(mntmOpenAddress);

		JSeparator separator = new JSeparator();
		mnMenu.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAction(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnMenu.add(mntmExit);

		lblName = new JLabel("<html>Name: <FONT COLOR=BLUE>Deuce</font></html>");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				configFrame.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, lblName, 1, SpringLayout.SOUTH, menuBar);
		springLayout.putConstraint(SpringLayout.EAST, lblName, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(lblName);

		table = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
		};
		table.setBorder(null);
		//JScrollBar scrollin = new JScrollBar();
		//table.add(scrollin);
		table.setFillsViewportHeight(true);
		springLayout.putConstraint(SpringLayout.SOUTH, lblName, -4, SpringLayout.NORTH, table);
		springLayout.putConstraint(SpringLayout.NORTH, table, 40, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, table, -102, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, table, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, table, 10, SpringLayout.WEST, getContentPane());
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Server Name", "Players", "Max", "Mode", "Map Name", "Country", "Ping"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sc = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		springLayout.putConstraint(SpringLayout.NORTH, sc, 40, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, sc, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, sc, 0, SpringLayout.EAST, lblName);
		sc.setBounds(10, 40, 500, 500);
		//getContentPane().add(table);
		getContentPane().add(sc);

		JCheckBox chckbxFavoritesOnly = new JCheckBox("Favorites Only");
		chckbxFavoritesOnly.setEnabled(false);
		springLayout.putConstraint(SpringLayout.SOUTH, sc, -6, SpringLayout.NORTH, chckbxFavoritesOnly);
		springLayout.putConstraint(SpringLayout.NORTH, chckbxFavoritesOnly, 6, SpringLayout.SOUTH, table);
		springLayout.putConstraint(SpringLayout.WEST, chckbxFavoritesOnly, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(chckbxFavoritesOnly);

		JCheckBox chckbxEmptyServers = new JCheckBox("Empty Servers");
		chckbxEmptyServers.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, chckbxEmptyServers, 0, SpringLayout.SOUTH, chckbxFavoritesOnly);
		springLayout.putConstraint(SpringLayout.WEST, chckbxEmptyServers, 0, SpringLayout.WEST, table);
		getContentPane().add(chckbxEmptyServers);

		JCheckBox chckbxFullServers = new JCheckBox("Full Servers");
		chckbxFullServers.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, chckbxFullServers, 0, SpringLayout.SOUTH, chckbxEmptyServers);
		springLayout.putConstraint(SpringLayout.WEST, chckbxFullServers, 0, SpringLayout.WEST, table);
		getContentPane().add(chckbxFullServers);

		lblOnline = new JLabel("Online: "+serverAmt);
		springLayout.putConstraint(SpringLayout.EAST, lblOnline, -150, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblName, 14, SpringLayout.EAST, lblOnline);
		getContentPane().add(lblOnline);
		springLayout.putConstraint(SpringLayout.NORTH, lblOnline, 1, SpringLayout.SOUTH, menuBar);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmWebsite = new JMenuItem("Main Forum");
		mntmWebsite.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL(
							"http://www.buildandshoot.com/viewtopic.php?f=13&t=3201&p=24789#p24789").toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmWebsite);
		springLayout.putConstraint(SpringLayout.SOUTH, lblOnline, -4, SpringLayout.NORTH, table);

		JButton btnConfigure = new JButton("Configure");
		springLayout.putConstraint(SpringLayout.NORTH, btnConfigure, 33, SpringLayout.SOUTH, sc);
		springLayout.putConstraint(SpringLayout.EAST, btnConfigure, -10, SpringLayout.EAST, getContentPane());
		btnConfigure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnConfigure.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				configFrame.setVisible(true);
			}
		});
		getContentPane().add(btnConfigure);

		JButton btnHostServer = new JButton("Host Server");
		springLayout.putConstraint(SpringLayout.NORTH, btnHostServer, 4, SpringLayout.SOUTH, btnConfigure);
		springLayout.putConstraint(SpringLayout.WEST, btnHostServer, -99, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnHostServer, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnConfigure, 0, SpringLayout.WEST, btnHostServer);
		getContentPane().add(btnHostServer);

		JButton btnConnect = new JButton("Connect");
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 6, SpringLayout.SOUTH, sc);
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, -99, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, getContentPane());
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String url = (String) table.getValueAt(table.getSelectedRow(), 0);
				Main.connectToServer(idents.get(url));
			}
		});
		getContentPane().add(btnConnect);
		
		configFrame.loadConfig();
	}

	public static void updateName(String name) {
		lblName.setText("<html>Name: <FONT COLOR=BLUE>"+name+"</font></html>");
	}

	public static void updateTable() throws Exception {
		idents.clear();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int rown = model.getRowCount(); 
		serverAmt = 0;
		playerAmt = 0;
		for(int i = rown - 1; i >= 0; i--)
		{
		   model.removeRow(i);
		}

		String json = "";
		try {
			URL url = new URL(Constants.MASTER_SERVER);
			BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
			String rd;
			StringBuilder sb = new StringBuilder();
			while((rd = read.readLine()) != null) {
				sb.append(rd+"\r\n");
			}
			read.close();
			json = new String(sb);
		} catch(IOException e) {
			e.printStackTrace();
		}

		JsonRootNode node = new JdomParser().parse(json);
		java.util.List<JsonNode> objs = node.getArrayNode();
		Object[][] rows = new Object[objs.size()][7];
		for(int i = 0; i < objs.size(); i++) {
			JsonNode child = objs.get(i);

			rows[i][0] = child.getStringValue("name");
			playerAmt += Integer.parseInt(child.getNumberValue("players_current"));
			rows[i][1] = child.getNumberValue("players_current");
			rows[i][2] = child.getNumberValue("players_max");
			rows[i][3] = child.getStringValue("game_mode");
			rows[i][4] = child.getStringValue("map");

			rows[i][5] = new ImageIcon(Util.getCountryFlag(child.getStringValue("country").toLowerCase()));

			//rows[i][5] = child.getStringValue("country").toUpperCase();
			rows[i][6] = child.getNumberValue("latency");

			String x = child.getStringValue("name");
			idents.put(x, child.getStringValue("identifier"));

			model.addRow(rows[i]);
			serverAmt++;
			lblOnline.setText("<html>Online: <font color=BLUE>"+playerAmt+"</font></html>");
			table.setPreferredScrollableViewportSize(table.getPreferredSize());
		}
		lblOnline.setText("Online: "+playerAmt);

	}
}

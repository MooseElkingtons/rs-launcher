package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.*;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

public class Frame extends JFrame {
	
	public static JTable table;
	private static JLabel lblName;
	private static JLabel lblOnline;
	private static JScrollPane sc;
	private static JMenuItem mntmRunOvl;
	private static JMenuItem mntmStop;
	private JMenuItem mntmCustomFiles = new JMenuItem("Custom Files");
	public static HashMap<String, String> idents = new HashMap<String, String>();
	private static TableRowSorter<TableModel> sorter;
	private String popupIdent = "";
	private int popupRow = -1;
	
	private static Runnable ovl;

	private static int serverAmt = 0, playerAmt = 0;
	public static boolean isFavoritesShowing = false;
	
	public FrameConfiguration configFrame;
	public FrameConnection connectFrame;
	public FrameBlacklist blacklistFrame;
	public FrameCustom customFrame;

	public static InstanceManager instanceManager;
	public static java.util.List<String> favorites = new ArrayList<String>();
	public static Blacklist blacklist = new Blacklist();
	
	public Frame(String title, final Image icon) {
		instanceManager = new InstanceManager();
		blacklist.load();
		loadFavorites();
		
		Main.cfg = new Configuration(new File(instanceManager.getInstanceFile(),
				"config.ini"), "=");
		loadOsConfig();
		configFrame = new FrameConfiguration(icon);
		connectFrame = new FrameConnection(icon);
		blacklistFrame = new FrameBlacklist(icon);
		customFrame = new FrameCustom(icon);
		setTitle(title);
		if(icon != null)
			setIconImage(icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(768, 500));
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
		mntmAddFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				askForNewFavorite(false, "");
			}
		});
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
				switch(column) {
					case 1:
					case 2:
					case 6:
						return Integer.class;
					default:
						return getValueAt(0, column).getClass();
				}
            }
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int button = e.getButton();
				if(button == MouseEvent.BUTTON2) {
					Point p = e.getPoint();
					int row = table.rowAtPoint(p);
					
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ENTER) {
					int row = table.getSelectedRow() - 1;
					String name = table.getValueAt(row, 0).toString();
					Main.connectToServer(idents.get(sanitize(name)));
				}
			}
		});
		
		table.setBorder(null);
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
		sorter = new TableRowSorter<TableModel>(table.getModel());
		
		Comparator ic = new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Integer)o1).compareTo((Integer) o2);
			}
			
			public boolean equals(Object o2) {
				return this.equals(o2);
			}
		};
		
		sorter.setComparator(1, ic);
		sorter.setComparator(2, ic);
		sorter.setComparator(6, ic);
		
		table.setRowSorter(sorter);
		table.getTableHeader().setReorderingAllowed(false);
		sc = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		springLayout.putConstraint(SpringLayout.NORTH, sc, 40, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, sc, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, sc, 0, SpringLayout.EAST, lblName);
		sc.setBounds(10, 40, 500, 500);
		//getContentPane().add(table);
		getContentPane().add(sc);

		final JCheckBox chckbxFavoritesOnly = new JCheckBox("Favorites Only");
		chckbxFavoritesOnly.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					updateTable(!isFavoritesShowing);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				isFavoritesShowing = !isFavoritesShowing;
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, sc, -6, SpringLayout.NORTH, chckbxFavoritesOnly);
		springLayout.putConstraint(SpringLayout.NORTH, chckbxFavoritesOnly, 6, SpringLayout.SOUTH, table);
		springLayout.putConstraint(SpringLayout.WEST, chckbxFavoritesOnly, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(chckbxFavoritesOnly);

		lblOnline = new JLabel("Online: "+serverAmt);
		springLayout.putConstraint(SpringLayout.EAST, lblOnline, -150, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblName, 14, SpringLayout.EAST, lblOnline);
		getContentPane().add(lblOnline);
		springLayout.putConstraint(SpringLayout.NORTH, lblOnline, 1, SpringLayout.SOUTH, menuBar);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenu mnOvl = new JMenu("OVL");
		mnTools.add(mnOvl);
		
		mntmRunOvl = new JMenuItem("Run");
		mntmRunOvl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!mntmRunOvl.isEnabled())
					return;
				if(!Util.ovlExists()) {
					try {
						Util.downloadFile(new URL(
								"https://dl.dropboxusercontent.com/u/60275959/ovl075.exe"),
								"ovl075.exe");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				int id = askPlayerId(false);
				if(id == -1)
					return; // Cancelled
				mntmRunOvl.setEnabled(false);
				mntmStop.setEnabled(true);
				ovl = new OVL(id);
				Main.ovlThread = new Thread(ovl);
				Main.ovlThread.start();
			}
		});
		mnOvl.add(mntmRunOvl);
		
		mntmStop = new JMenuItem("Stop");
		mntmStop.setEnabled(false);
		mntmStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!mntmStop.isEnabled())
					return;
				mntmStop.setEnabled(false);
				mntmRunOvl.setEnabled(true);
				System.out.println("Destroying OVL075 Process");
				OVL.process.destroy();
			}
		});
		mnOvl.add(mntmStop);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmBlacklist = new JMenuItem("Blacklist (Filter)");
		mntmBlacklist.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				blacklistFrame.setVisible(true);
			}
		});
		
		mnOptions.add(mntmBlacklist);
		
		final JCheckBoxMenuItem chckbxUpdate = new JCheckBoxMenuItem("Auto-Update");
		chckbxUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Main.rsCfg.put("auto_update",
							chckbxUpdate.isSelected() ? "1" : "0");
					Main.rsCfg.save();
			}
		});
		if(Integer.parseInt((String) Main.rsCfg.get("auto_update")) == 1)
			chckbxUpdate.setSelected(true);
		mnOptions.add(chckbxUpdate);
		
		final JCheckBoxMenuItem chckbxOpenGl = new JCheckBoxMenuItem("OpenSpades");
		chckbxOpenGl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.rsCfg.put("open_spades",
						chckbxOpenGl.isSelected() ? "1" : "0");
				Main.rsCfg.save();
			}
		});
		try {
			if(Integer.parseInt((String) Main.rsCfg.get("open_spades")) == 1)
				chckbxOpenGl.setSelected(true);
		} catch(NumberFormatException e) {
			Main.rsCfg.put("open_spades", "0");
			Main.rsCfg.save();
		}
		
		mntmCustomFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(customFrame == null)
					customFrame = new FrameCustom(icon);
				if(!customFrame.isVisible()) {
					customFrame.setVisible(true);
				}
			}
		});
		mnOptions.add(mntmCustomFiles);
		
		JSeparator separator_1 = new JSeparator();
		mnOptions.add(separator_1);
		mnOptions.add(chckbxOpenGl);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmWebsite = new JMenuItem("Main Forum");
		mntmWebsite.setAction(new AbstractAction("Main Forum") {
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
		
		JSeparator separatorHelp = new JSeparator();
		mnHelp.add(separatorHelp);
		
		JMenuItem mntmTroubleshooting = new JMenuItem("Troubleshooting");
		mntmTroubleshooting.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Util.browseURI("http://www.buildandshoot.com/viewtopic.php?" +
							"f=13&t=3201&p=24789#troubleshooting");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmTroubleshooting);
		springLayout.putConstraint(SpringLayout.SOUTH, lblOnline, -4, SpringLayout.NORTH, table);
		
		final JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);
		
		JMenuItem popupConnect = new JMenuItem("Connect");
		popupConnect.setIcon(Util.getIcon("control_play"));
		popupConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Main.connectToServer(popupIdent);
			}
		});
		popupMenu.add(popupConnect);
		
		final JMenuItem popupFavorite = new JMenuItem("Favorite");
		popupFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				boolean isFav = favorites.contains(popupIdent);
				if(!isFav)
					addFavorite(popupIdent, popupRow);
				else
					remFavorite(popupIdent, popupRow);
			}
		});
		popupMenu.add(popupFavorite);
		
		JMenuItem popupCopyAddress = new JMenuItem("Copy Address");
		popupCopyAddress.setIcon(Util.getIcon("paste_plain"));
		popupCopyAddress.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				StringSelection selection = new StringSelection(popupIdent);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    clipboard.setContents(selection, selection);
			}
		});
		popupMenu.add(popupCopyAddress);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					Point p = e.getPoint();
					int row = table.rowAtPoint(p);
					if(row < 0) {
						popupMenu.setVisible(false);
						return;
					}
					String name = table.getValueAt(row, 0).toString();
					popupIdent = idents.get(sanitize(name));
					popupRow = row;
					if(!favorites.contains(popupIdent)) {
						popupFavorite.setText("Add Favorite");
						popupFavorite.setIcon(Util.getIcon("heart_add"));
					} else {
						popupFavorite.setText("Remove Favorite");
						popupFavorite.setIcon(Util.getIcon("heart_delete"));
					}
				}
			}
		});
		
		JButton btnConfigure = new JButton("Configure");
		springLayout.putConstraint(SpringLayout.EAST, btnConfigure, -10, SpringLayout.EAST, getContentPane());
		btnConfigure.setHorizontalAlignment(SwingConstants.LEFT);
		btnConfigure.setIcon((Icon) Util.getIcon("cog"));
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

		JButton btnConnect = new JButton("Connect");
		springLayout.putConstraint(SpringLayout.NORTH, btnConfigure, 4, SpringLayout.SOUTH, btnConnect);
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 11, SpringLayout.WEST, lblName);
		btnConnect.setHorizontalAlignment(SwingConstants.LEFT);
		btnConnect.setIcon((Icon) Util.getIcon("control_play"));
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 6, SpringLayout.SOUTH, sc);
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, getContentPane());
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String url = (String) table.getValueAt(table.getSelectedRow(), 0);
				Main.connectToServer(idents.get(sanitize(url)));
			}
		});
		getContentPane().add(btnConnect);
		
		JButton btnResetList = new JButton("Refresh List");
		springLayout.putConstraint(SpringLayout.NORTH, btnResetList, 6, SpringLayout.SOUTH, btnConfigure);
		springLayout.putConstraint(SpringLayout.EAST, btnResetList, -10, SpringLayout.EAST, getContentPane());
		btnResetList.setHorizontalAlignment(SwingConstants.LEFT);
		btnResetList.setIcon((Icon) Util.getIcon("arrow_refresh"));
		btnResetList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					Frame.updateTable(isFavoritesShowing);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getContentPane().add(btnResetList);
		
		JButton btnSearch = new JButton("Search");
		springLayout.putConstraint(SpringLayout.NORTH, btnSearch, 6, SpringLayout.SOUTH, sc);
		springLayout.putConstraint(SpringLayout.WEST, btnSearch, -125, SpringLayout.WEST, btnConnect);
		springLayout.putConstraint(SpringLayout.EAST, btnSearch, -10, SpringLayout.WEST, btnConnect);
		btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
		btnSearch.setIcon((Icon) Util.getIcon("magnifier"));
		btnSearch.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				String search = JOptionPane.showInputDialog("Please insert keywords to search for.");
				if(!searchFor(search))
					searchFor("");
			}
		});
		getContentPane().add(btnSearch);
		
		JButton btnAddFavorite = new JButton("Add Favorite");
		springLayout.putConstraint(SpringLayout.NORTH, btnAddFavorite, 4, SpringLayout.SOUTH, btnSearch);
		springLayout.putConstraint(SpringLayout.WEST, btnAddFavorite, 0, SpringLayout.WEST, btnSearch);
		springLayout.putConstraint(SpringLayout.EAST, btnAddFavorite, -135, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnConfigure, 10, SpringLayout.EAST, btnAddFavorite);
		btnAddFavorite.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddFavorite.setIcon((Icon) Util.getIcon("heart_add"));
		btnAddFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row > -1) {
					String name = (String) table.getValueAt(row, 0);
					addFavorite(idents.get(sanitize(name)), row);
				}
			}
		});
		getContentPane().add(btnAddFavorite);
		
		JButton btnRemoveFavorite = new JButton("Del Favorite");
		springLayout.putConstraint(SpringLayout.NORTH, btnRemoveFavorite, 6, SpringLayout.SOUTH, btnAddFavorite);
		springLayout.putConstraint(SpringLayout.WEST, btnRemoveFavorite, 0, SpringLayout.WEST, btnSearch);
		springLayout.putConstraint(SpringLayout.EAST, btnRemoveFavorite, -135, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnResetList, 10, SpringLayout.EAST, btnRemoveFavorite);
		btnRemoveFavorite.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveFavorite.setIcon((Icon) Util.getIcon("heart_delete"));
		btnRemoveFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row > -1) {
					String name = (String) table.getValueAt(table.getSelectedRow(), 0);
					remFavorite(idents.get(sanitize(name)), row);
				}
			}
		});
		getContentPane().add(btnRemoveFavorite);
		
		final JComboBox comboBox = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 71, SpringLayout.SOUTH, sc);
		springLayout.putConstraint(SpringLayout.SOUTH, comboBox, -10, SpringLayout.SOUTH, getContentPane());
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				instanceManager.setInstance(comboBox.getSelectedIndex() == 0 
						? 75 : 76);
				try {
					updateTable(isFavoritesShowing);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0.75", "0.76"}));
		comboBox.setSelectedIndex(0);
		getContentPane().add(comboBox);
		
		JLabel lblVersion = new JLabel("Version:");
		springLayout.putConstraint(SpringLayout.EAST, lblVersion, 55, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, comboBox, 55, SpringLayout.EAST, lblVersion);
		springLayout.putConstraint(SpringLayout.NORTH, lblVersion, 0, SpringLayout.NORTH, comboBox);
		springLayout.putConstraint(SpringLayout.WEST, lblVersion, 15, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblVersion, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, lblVersion);
		getContentPane().add(lblVersion);

		configFrame.loadConfig();
	}
	
	public static void updateName(String name) {
		lblName.setText("<html>Name: <FONT COLOR=BLUE>"+name+"</font></html>");
	}

	public static void updateTable(boolean onlyFavorites) throws Exception {
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
			URL url = new URL(Constants.MASTER_SERVER + instanceManager.getInstance());
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
		final Object[][] rows = new Object[objs.size()][7];
		model.setRowCount(0);
		lblOnline.setText("<html>Online: <font color=BLUE>"+playerAmt+"</font></html>");
		for(int i = 0; i < objs.size(); i++) {
			JsonNode child = objs.get(i);
			
			String name = child.getStringValue("name");
			final String identifier = child.getStringValue("identifier");
			idents.put(name, identifier);
			
			if(favorites.contains(identifier))
				rows[i][0] = String.format("<html><strong>%s</strong></html>", name);
			else
				rows[i][0] = name;
			playerAmt += Integer.parseInt(child.getNumberValue("players_current"));
			rows[i][1] = new Integer(child.getNumberValue("players_current"));
			rows[i][2] = new Integer(child.getNumberValue("players_max"));
			rows[i][3] = child.getStringValue("game_mode");
			rows[i][4] = child.getStringValue("map");

			ImageIcon ii = Util.getIcon("flags/"
					+child.getStringValue("country").toLowerCase());
			if(ii == null)
				ii = Util.getIcon("flags/unknown");
			rows[i][5] = ii;

			//rows[i][5] = child.getStringValue("country").toUpperCase();
			rows[i][6] = new Integer(child.getNumberValue("latency"));
			
			if(onlyFavorites && favorites.contains(identifier))
				model.addRow(rows[i]);
			else if(!onlyFavorites) {
				if(!blacklist.contains(name)
						&& !blacklist.contains((String) rows[i][3]) 
						&& !blacklist.contains((String) rows[i][4])) {
					model.addRow(rows[i]);
				}
			}
			serverAmt++;
			lblOnline.setText("<html>Online: <font color=BLUE>"+playerAmt+"</font></html>");
			table.setPreferredScrollableViewportSize(table.getPreferredSize());
		}
		lblOnline.setText("Online: "+playerAmt);
	}


	public boolean searchFor(String s) {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		RowFilter<TableModel, Object> filter = null;
		try {
			filter = RowFilter.regexFilter(s);
		} catch(Exception e) {
			return false;
		}
		sorter.setRowFilter(filter);
		table.setRowSorter(sorter);
		return table.getRowCount() > 0; // Nothing found!
	}
	
	public int askPlayerId(boolean tried) {
		try {
			String msg = "Please insert the player ID to spectate.";
			if(tried)
				msg = "<html>Please insert a <font color=RED>VALID</font> Player ID.</html>";
			String response = JOptionPane.showInputDialog(Main.frame, msg);
			if(response == null) // Cancelled
				return -1; // cancel response

			int pres = Integer.parseInt(response);
			if(pres > 32 || pres < 0)
				return askPlayerId(true); // Out of player ID range
			return pres; // Valid
		} catch(Exception e) {
			return askPlayerId(true); // Invalid
		}
	}
	
	public void loadFavorites() {
		try {
			File file = new File(Constants.ROOT_DIR, "favorites.list");
			if(!file.exists()) {
				file.createNewFile();
				return;
			}
			BufferedReader read = new BufferedReader(new FileReader(file));
			String rd = "";
			while((rd = read.readLine()) != null) {
				if(rd.contains("aos://")) {
					favorites.add(rd);
				}
			}
			read.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFavorites() {
		try {
			File file = new File(Constants.ROOT_DIR, "favorites.list");
			BufferedWriter write = new BufferedWriter(new FileWriter(file, false));
			for(String s : favorites) {
				write.write(s);
				write.newLine();
			}
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFavorite(String url, int row) {
		if(favorites.contains(url)) {
			System.out.println(String.format("%s already exists in Favorites.", url));
			return;
		}
		favorites.add(url);
		if(row > -1) {
			String name = table.getValueAt(row, 0).toString();
			table.setValueAt(
					String.format("<html><strong>%s</strong></html>", name), row, 0);
		}
		try {
			File file = new File(Constants.ROOT_DIR, "favorites.list");
			BufferedWriter write = new BufferedWriter(new FileWriter(file, true));
			write.write(url);
			write.newLine();
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println(String.format("Added %s to Favorites.", url));
		}
	}
	
	public void remFavorite(String url, int row) {
		if(!favorites.contains(url)) {
			System.out.println(String.format("%s does not exist in Favorites.", url));
			return;
		}
		if(row > -1) {
			String name = table.getValueAt(row, 0).toString();
			table.setValueAt(sanitize(name), row, 0);
		}
		favorites.remove(url);
		try {
			File file = new File(Constants.ROOT_DIR, "favorites.list");
			BufferedWriter write = new BufferedWriter(new FileWriter(file, false));
			for(String s : favorites) {
				if(!s.equalsIgnoreCase(url)) {
					write.write(s);
					write.newLine();
				}
			}
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println(String.format("Removed %s from Favorites.", url));
		}
	}
	
	public void askForNewFavorite(boolean tried, String prev) {
		String x = JOptionPane.showInputDialog(tried ? "<html>Please insert " +
				"a <font color=RED>VALID</font> Ace of Spades URL.</html>"
											: "Please insert an Ace of Spades URL.", prev);
		if(x == null)
			return;
		if(!x.toLowerCase().contains("aos://")) {
			askForNewFavorite(true, x);
			return;
		} else {
			addFavorite(x, -1);
		}
	}
	
	public void lockCustoms() {
		mntmCustomFiles.setEnabled(false);
	}
	
	public void unlockCustoms() {
		mntmCustomFiles.setEnabled(true);
	}
	
	public String sanitize(String string) {
		return string.replace("<html>", "").replace("</html>", "")
					.replace("<strong>", "")
					.replace("</strong>", "");
	}
	
	private void loadOsConfig() {
		try {
			Main.osCfg = new Configuration(
					new File(System.getenv("appdata"),
							"yvt.jp/OpenSpades.prefs"), ":");
			if(!Main.osCfg.file.exists()) {
				new File(System.getenv("appdata"), "yvt.jp").mkdirs();
				Main.osCfg.file.createNewFile();
				System.out.println("Couldn't find OpenSpades Configuration.");
				Thread.sleep(1000);
				Main.osCfg.put("r_videoWidth", Main.cfg.get("width").toString());
				Main.osCfg.put("r_videoHeight", Main.cfg.get("height").toString());
				Main.osCfg.put("r_fullscreen", Main.cfg.get("windowed") == "0" ? "1" : "0");
				Main.osCfg.put("r_multisamples", "2");
				Main.osCfg.put("r_fxaa", "0");
				Main.osCfg.put("r_bloom", "0");
				Main.osCfg.put("r_lens", "0");
				Main.osCfg.put("r_lensFlare", "0");
				Main.osCfg.put("r_cameraBlur", "0");
				Main.osCfg.put("r_softParticles", "1");
				Main.osCfg.put("r_radiosity", "1");
				Main.osCfg.put("r_modelShadows", "0");
				Main.osCfg.put("r_dlights", "1");
				Main.osCfg.put("r_fogShadow", "0");
				Main.osCfg.put("r_water", "0");
				Main.osCfg.put("s_maxPolyphonics", "96");
				Main.osCfg.put("s_eax", "1");
				Main.osCfg.put("cg_blood", "1");
				Main.osCfg.put("cg_lastQuickConnectHost", " ");
				Main.osCfg.put("cg_playerName", Main.cfg.get("name").toString());
				Main.osCfg.put("cg_protocolVersion", "3");
				Main.osCfg.put("cg_serverSort", "16385");
				Main.osCfg.save();
				
				Configuration altCfg = new Configuration(
						new File(Constants.ROOT_DIR, "openspades.pref"), ":");
				if(!altCfg.file.exists())
					altCfg.file.createNewFile();
				altCfg.putAll(Main.osCfg.getAll());
				altCfg.save();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	
}

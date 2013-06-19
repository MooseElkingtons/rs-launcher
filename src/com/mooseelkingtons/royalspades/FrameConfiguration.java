package com.mooseelkingtons.royalspades;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;

public class FrameConfiguration extends JFrame {
	private final JList list = new JList();
	private JTextField textFieldName;
	private JTextField textFieldWidth;
	private JTextField textFieldHeight;
	private JSlider sliderVolume;
	private JSlider sliderSensitivity;
	private JSlider sliderTeamAred;
	private JSlider sliderTeamAGreen;
	private JSlider sliderTeamABlue;
	private JSlider sliderTeamBRed;
	private JSlider sliderTeamBGreen;
	private JSlider sliderTeamBBlue;
	private JTextField textFieldServerName;
	private JTextField textField;
	private JTextField txtGreen;
	private JTextField txtBlue;
	private JTextField textFieldMaxPlayers;
	private JCheckBox chckbxLogging;
	private JCheckBox chckbxFullscreen;
	private JCheckBox chckbxInvertedY;
	private JCheckBox chckbxDisplayNews;
	private JTextField cfgField;
	
	private final JPanel clientConfig;
	
	public FrameConfiguration(Image icon) {
		super("Royal Spades Configuration");
		setIconImage(icon);
		setSize(new Dimension(545, 404));
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, list, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, list, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, list, 0, SpringLayout.SOUTH, getContentPane());
		getContentPane().setLayout(springLayout);
		
		JButton btnClose = new JButton("Close");
		springLayout.putConstraint(SpringLayout.SOUTH, btnClose, -13, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnClose, -6, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnClose);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		
		JButton btnSave = new JButton("Save");
		springLayout.putConstraint(SpringLayout.SOUTH, btnSave, -13, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -71, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnSave);
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveConfig();
				setVisible(false);
			}
		});
		
		final JPanel serverConfig = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, 6, SpringLayout.SOUTH, serverConfig);
		springLayout.putConstraint(SpringLayout.NORTH, btnClose, 6, SpringLayout.SOUTH, serverConfig);
		springLayout.putConstraint(SpringLayout.WEST, serverConfig, 154, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, serverConfig, 10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, list, -6, SpringLayout.WEST, serverConfig);
		springLayout.putConstraint(SpringLayout.NORTH, serverConfig, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, serverConfig, -42, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(serverConfig);
		SpringLayout sl_serverConfig = new SpringLayout();
		serverConfig.setLayout(sl_serverConfig);
		
		ListSelectionModel lsm = list.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				int min = lsm.getMinSelectionIndex(), max = lsm.getMaxSelectionIndex();
				int selected = 0;
				for(int i = min; i <= max; i++) {
					if(lsm.isSelectedIndex(i)) {
						selected = i;
						break;
					}
				}
				switch(selected) {
					case 0:
						serverConfig.setVisible(false);
						clientConfig.setVisible(true);
						break;
					
					case 1:
						serverConfig.setVisible(true);
						clientConfig.setVisible(false);
						break;

						
					default:
						serverConfig.setVisible(false);
						clientConfig.setVisible(true);
				}
			}
			
		});
		
		clientConfig = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, clientConfig, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, clientConfig, 6, SpringLayout.EAST, list);
		springLayout.putConstraint(SpringLayout.SOUTH, clientConfig, -42, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, clientConfig, 10, SpringLayout.EAST, getContentPane());
		getContentPane().add(clientConfig);
		SpringLayout sl_clientConfig = new SpringLayout();
		sl_clientConfig.putConstraint(SpringLayout.NORTH, serverConfig, 0, SpringLayout.NORTH, clientConfig);
		sl_clientConfig.putConstraint(SpringLayout.WEST, serverConfig, 0, SpringLayout.WEST, clientConfig);
		clientConfig.setLayout(sl_clientConfig);
		
				
				JLabel lblName = new JLabel("Name: ");
				lblName.setToolTipText("The username which will show up in-game.");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblName, 13, SpringLayout.NORTH, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblName, -331, SpringLayout.EAST, clientConfig);
				clientConfig.add(lblName);
				
				textFieldName = new JTextField();
				sl_clientConfig.putConstraint(SpringLayout.NORTH, textFieldName, 10, SpringLayout.NORTH, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.WEST, textFieldName, 45, SpringLayout.EAST, lblName);
				clientConfig.add(textFieldName);
				textFieldName.setColumns(10);
				
				JLabel lblFrameSize = new JLabel("Frame Attributes:");
				clientConfig.add(lblFrameSize);
				
				JLabel lblWidth = new JLabel("Width:");
				clientConfig.add(lblWidth);
				
				textFieldWidth = new JTextField();
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, lblFrameSize, -14, SpringLayout.NORTH, textFieldWidth);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblFrameSize, 0, SpringLayout.EAST, textFieldWidth);
				sl_clientConfig.putConstraint(SpringLayout.EAST, textFieldWidth, -210, SpringLayout.EAST, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblWidth, 3, SpringLayout.NORTH, textFieldWidth);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblWidth, -6, SpringLayout.WEST, textFieldWidth);
				clientConfig.add(textFieldWidth);
				textFieldWidth.setColumns(10);
				
				textFieldHeight = new JTextField();
				sl_clientConfig.putConstraint(SpringLayout.EAST, textFieldHeight, -210, SpringLayout.EAST, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, textFieldWidth, -6, SpringLayout.NORTH, textFieldHeight);
				textFieldHeight.setColumns(10);
				clientConfig.add(textFieldHeight);
				
				JLabel lblHeight = new JLabel("Height:");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblHeight, 3, SpringLayout.NORTH, textFieldHeight);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblHeight, -6, SpringLayout.WEST, textFieldHeight);
				clientConfig.add(lblHeight);
				
				chckbxFullscreen = new JCheckBox("Fullscreen");
				clientConfig.add(chckbxFullscreen);
				
				JLabel lblGameAttributes = new JLabel("Game Attributes:");
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, textFieldHeight, -6, SpringLayout.NORTH, lblGameAttributes);
				sl_clientConfig.putConstraint(SpringLayout.WEST, lblGameAttributes, 20, SpringLayout.WEST, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, lblGameAttributes, -165, SpringLayout.SOUTH, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblGameAttributes, -210, SpringLayout.EAST, clientConfig);
				clientConfig.add(lblGameAttributes);
				
				JLabel lblVolume = new JLabel("Volume: ");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblVolume, 165, SpringLayout.NORTH, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, lblVolume, -114, SpringLayout.SOUTH, clientConfig);
				clientConfig.add(lblVolume);
				
				sliderVolume = new JSlider();
				sl_clientConfig.putConstraint(SpringLayout.EAST, textFieldName, 0, SpringLayout.EAST, sliderVolume);
				sl_clientConfig.putConstraint(SpringLayout.EAST, sliderVolume, -20, SpringLayout.EAST, clientConfig);
				sliderVolume.setPaintTicks(true);
				sliderVolume.setValue(10);
				sliderVolume.setPaintLabels(true);
				sliderVolume.setMajorTickSpacing(1);
				sliderVolume.setMaximum(10);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblVolume, -6, SpringLayout.WEST, sliderVolume);
				sl_clientConfig.putConstraint(SpringLayout.WEST, sliderVolume, 85, SpringLayout.WEST, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.NORTH, sliderVolume, 6, SpringLayout.SOUTH, lblGameAttributes);
				clientConfig.add(sliderVolume);
				
				JLabel lblMouseSensitivity = new JLabel("Mouse:");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblMouseSensitivity, 6, SpringLayout.SOUTH, lblVolume);
				sl_clientConfig.putConstraint(SpringLayout.WEST, lblMouseSensitivity, 38, SpringLayout.WEST, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, lblMouseSensitivity, 29, SpringLayout.SOUTH, lblVolume);
				clientConfig.add(lblMouseSensitivity);
				
				sliderSensitivity = new JSlider();
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblMouseSensitivity, -8, SpringLayout.WEST, sliderSensitivity);
				sliderSensitivity.setValue(500);
				sl_clientConfig.putConstraint(SpringLayout.EAST, sliderSensitivity, -20, SpringLayout.EAST, clientConfig);
				sliderSensitivity.setPaintTicks(true);
				sl_clientConfig.putConstraint(SpringLayout.NORTH, sliderSensitivity, 6, SpringLayout.SOUTH, sliderVolume);
				sl_clientConfig.putConstraint(SpringLayout.WEST, sliderSensitivity, 4, SpringLayout.WEST, sliderVolume);
				sliderSensitivity.setPaintLabels(true);
				sliderSensitivity.setMinorTickSpacing(10);
				sliderSensitivity.setMajorTickSpacing(100);
				sliderSensitivity.setMaximum(1000);
				clientConfig.add(sliderSensitivity);
				
				chckbxInvertedY = new JCheckBox("Inverted Y-axis");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, chckbxFullscreen, 0, SpringLayout.NORTH, chckbxInvertedY);
				sl_clientConfig.putConstraint(SpringLayout.WEST, chckbxFullscreen, 6, SpringLayout.EAST, chckbxInvertedY);
				sl_clientConfig.putConstraint(SpringLayout.WEST, chckbxInvertedY, 48, SpringLayout.WEST, clientConfig);
				chckbxInvertedY.setToolTipText("Inverts your camera's Y-axis to where if you move your mouse up, your camera will move down, and vice versa.");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, chckbxInvertedY, 6, SpringLayout.SOUTH, sliderSensitivity);
				clientConfig.add(chckbxInvertedY);
				
				chckbxDisplayNews = new JCheckBox("Display News");
				sl_clientConfig.putConstraint(SpringLayout.WEST, lblFrameSize, 0, SpringLayout.WEST, chckbxDisplayNews);
				chckbxDisplayNews.setToolTipText("Displays news after game has ended (NOT RECOMMENDED: this feature is broken due to Jagex's Fagification of Ace of Spades)");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, chckbxDisplayNews, 16, SpringLayout.SOUTH, chckbxInvertedY);
				sl_clientConfig.putConstraint(SpringLayout.WEST, chckbxDisplayNews, 10, SpringLayout.WEST, clientConfig);
				clientConfig.add(chckbxDisplayNews);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, btnClose, -10, SpringLayout.SOUTH, clientConfig);
				sl_clientConfig.putConstraint(SpringLayout.EAST, btnClose, 0, SpringLayout.EAST, sliderVolume);
				sl_clientConfig.putConstraint(SpringLayout.NORTH, btnSave, 6, SpringLayout.SOUTH, sliderTeamABlue);
				sl_clientConfig.putConstraint(SpringLayout.EAST, btnSave, -111, SpringLayout.EAST, serverConfig);
				
				JLabel lblPath = new JLabel("Path Dir:");
				sl_clientConfig.putConstraint(SpringLayout.NORTH, lblPath, 6, SpringLayout.SOUTH, lblName);
				sl_clientConfig.putConstraint(SpringLayout.WEST, lblPath, 0, SpringLayout.WEST, lblName);
				sl_clientConfig.putConstraint(SpringLayout.SOUTH, lblPath, -6, SpringLayout.NORTH, lblFrameSize);
				sl_clientConfig.putConstraint(SpringLayout.EAST, lblPath, -7, SpringLayout.EAST, chckbxDisplayNews);
				clientConfig.add(lblPath);

				cfgField = new JTextField((String) Frame.lc.get("aos-dir"));
				sl_clientConfig.putConstraint(SpringLayout.NORTH, cfgField, 6, SpringLayout.SOUTH, textFieldName);
				sl_clientConfig.putConstraint(SpringLayout.WEST, cfgField, 0, SpringLayout.WEST, textFieldName);
				sl_clientConfig.putConstraint(SpringLayout.EAST, cfgField, 0, SpringLayout.EAST, textFieldName);
				clientConfig.add(cfgField);
				cfgField.setColumns(10);
		list.setSelectionModel(lsm);
		list.setValueIsAdjusting(true);
		list.setFont(new Font("Tahoma", Font.PLAIN, 14));
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Client Configuration", "Server Configuration"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(list);
		
		JLabel lblTeam = new JLabel("Team 1:");
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblTeam, 20, SpringLayout.WEST, serverConfig);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblTeam, -322, SpringLayout.EAST, serverConfig);
		serverConfig.add(lblTeam);
		
		JLabel lblRed = new JLabel("R:");
		serverConfig.add(lblRed);
		
		sliderTeamAred = new JSlider();
		sl_serverConfig.putConstraint(SpringLayout.NORTH, sliderTeamAred, 133, SpringLayout.NORTH, serverConfig);
		sliderTeamAred.setValue(0);
		sliderTeamAred.setMinorTickSpacing(1);
		sliderTeamAred.setMajorTickSpacing(1);
		sliderTeamAred.setMaximum(255);
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblRed, 0, SpringLayout.NORTH, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblRed, -24, SpringLayout.WEST, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, lblRed, -3, SpringLayout.SOUTH, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblRed, -4, SpringLayout.WEST, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.WEST, sliderTeamAred, 93, SpringLayout.WEST, serverConfig);
		serverConfig.add(sliderTeamAred);
		
		sliderTeamAGreen = new JSlider();
		sl_serverConfig.putConstraint(SpringLayout.NORTH, sliderTeamAGreen, 6, SpringLayout.SOUTH, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.EAST, sliderTeamAGreen, 0, SpringLayout.EAST, sliderTeamAred);
		sliderTeamAGreen.setValue(255);
		sliderTeamAGreen.setMinorTickSpacing(1);
		sliderTeamAGreen.setMaximum(255);
		sliderTeamAGreen.setMajorTickSpacing(1);
		serverConfig.add(sliderTeamAGreen);
		
		JLabel lblG = new JLabel("G:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblG, 6, SpringLayout.SOUTH, lblRed);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblG, -24, SpringLayout.WEST, sliderTeamAGreen);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, lblG, 0, SpringLayout.SOUTH, sliderTeamAGreen);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblG, -4, SpringLayout.WEST, sliderTeamAGreen);
		serverConfig.add(lblG);
		
		JLabel lblServerName = new JLabel("Server Name:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblServerName, 13, SpringLayout.NORTH, serverConfig);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblServerName, 10, SpringLayout.WEST, serverConfig);
		serverConfig.add(lblServerName);
		
		textFieldServerName = new JTextField();
		textFieldServerName.setToolTipText("The Server Name which will appear on the Master Server List");
		textFieldServerName.setText("Deuces Wild");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, textFieldServerName, 10, SpringLayout.NORTH, serverConfig);
		sl_serverConfig.putConstraint(SpringLayout.WEST, textFieldServerName, 6, SpringLayout.EAST, lblServerName);
		sl_serverConfig.putConstraint(SpringLayout.EAST, textFieldServerName, 188, SpringLayout.EAST, lblServerName);
		serverConfig.add(textFieldServerName);
		textFieldServerName.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblPort, 12, SpringLayout.SOUTH, lblServerName);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblPort, 0, SpringLayout.EAST, lblServerName);
		serverConfig.add(lblPort);
		
		textField = new JTextField();
		sl_serverConfig.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, textFieldServerName);
		sl_serverConfig.putConstraint(SpringLayout.WEST, textField, 6, SpringLayout.EAST, lblPort);
		sl_serverConfig.putConstraint(SpringLayout.EAST, textField, 49, SpringLayout.EAST, lblPort);
		textField.setText("32887");
		serverConfig.add(textField);
		textField.setColumns(10);
		
		txtGreen = new JTextField();
		txtGreen.setText("Green");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, txtGreen, 4, SpringLayout.SOUTH, textField);
		sl_serverConfig.putConstraint(SpringLayout.WEST, txtGreen, 0, SpringLayout.WEST, textFieldServerName);
		serverConfig.add(txtGreen);
		txtGreen.setColumns(10);
		
		JLabel lblB = new JLabel("B:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblB, 9, SpringLayout.SOUTH, sliderTeamAGreen);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblB, 0, SpringLayout.WEST, lblRed);
		serverConfig.add(lblB);
		
		sliderTeamABlue = new JSlider();
		sliderTeamABlue.setMinorTickSpacing(1);
		sliderTeamABlue.setMajorTickSpacing(1);
		sliderTeamABlue.setMaximum(255);
		sliderTeamABlue.setValue(0);
		sl_serverConfig.putConstraint(SpringLayout.NORTH, sliderTeamABlue, 6, SpringLayout.SOUTH, sliderTeamAGreen);
		sl_serverConfig.putConstraint(SpringLayout.WEST, sliderTeamABlue, 0, SpringLayout.WEST, sliderTeamAred);
		serverConfig.add(sliderTeamABlue);
		
		JLabel lblTeam_1 = new JLabel("Team 1:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblTeam, 39, SpringLayout.SOUTH, lblTeam_1);
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblTeam_1, 3, SpringLayout.NORTH, txtGreen);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblTeam_1, -6, SpringLayout.WEST, txtGreen);
		serverConfig.add(lblTeam_1);
		
		txtBlue = new JTextField();
		txtBlue.setText("Blue");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, txtBlue, 6, SpringLayout.SOUTH, txtGreen);
		sl_serverConfig.putConstraint(SpringLayout.WEST, txtBlue, 0, SpringLayout.WEST, textFieldServerName);
		serverConfig.add(txtBlue);
		txtBlue.setColumns(10);
		
		JLabel lblTeam_2 = new JLabel("Team 2:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblTeam_2, 3, SpringLayout.NORTH, txtBlue);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblTeam_2, 0, SpringLayout.EAST, lblServerName);
		serverConfig.add(lblTeam_2);
		
		JLabel lblTeam_3 = new JLabel("Team 2:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblTeam_3, 98, SpringLayout.SOUTH, lblTeam);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblTeam_3, 0, SpringLayout.WEST, lblTeam);
		serverConfig.add(lblTeam_3);
		
		textFieldMaxPlayers = new JTextField();
		sl_serverConfig.putConstraint(SpringLayout.NORTH, textFieldMaxPlayers, 6, SpringLayout.SOUTH, textFieldServerName);
		sl_serverConfig.putConstraint(SpringLayout.WEST, textFieldMaxPlayers, 244, SpringLayout.WEST, serverConfig);
		sl_serverConfig.putConstraint(SpringLayout.EAST, textFieldMaxPlayers, 0, SpringLayout.EAST, textFieldServerName);
		textFieldMaxPlayers.setText("32");
		serverConfig.add(textFieldMaxPlayers);
		textFieldMaxPlayers.setColumns(10);
		
		JLabel lblMaxPlayers = new JLabel("Max Players:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblMaxPlayers, 0, SpringLayout.NORTH, lblPort);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblMaxPlayers, -6, SpringLayout.WEST, textFieldMaxPlayers);
		serverConfig.add(lblMaxPlayers);
		
		JLabel lblR = new JLabel("R:");
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblR, 0, SpringLayout.WEST, lblRed);
		serverConfig.add(lblR);
		
		sliderTeamBRed = new JSlider();
		sliderTeamBRed.setValue(0);
		sliderTeamBRed.setMinorTickSpacing(1);
		sliderTeamBRed.setMajorTickSpacing(1);
		sliderTeamBRed.setMaximum(255);
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblR, 0, SpringLayout.NORTH, sliderTeamBRed);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, lblR, 0, SpringLayout.SOUTH, sliderTeamBRed);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblR, -4, SpringLayout.WEST, sliderTeamBRed);
		sl_serverConfig.putConstraint(SpringLayout.NORTH, sliderTeamBRed, 33, SpringLayout.SOUTH, sliderTeamABlue);
		sl_serverConfig.putConstraint(SpringLayout.WEST, sliderTeamBRed, 0, SpringLayout.WEST, sliderTeamAred);
		serverConfig.add(sliderTeamBRed);
		
		chckbxLogging = new JCheckBox("Logging");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, chckbxLogging, 0, SpringLayout.NORTH, txtGreen);
		sl_serverConfig.putConstraint(SpringLayout.WEST, chckbxLogging, 6, SpringLayout.EAST, txtGreen);
		serverConfig.add(chckbxLogging);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Public Server");
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setToolTipText("If enabled, the server will be advertised on the Master Server. (Disabled due to Jagex's Fagification to Ace of Spades)");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, chckbxNewCheckBox, 0, SpringLayout.NORTH, txtBlue);
		sl_serverConfig.putConstraint(SpringLayout.WEST, chckbxNewCheckBox, 6, SpringLayout.EAST, txtBlue);
		serverConfig.add(chckbxNewCheckBox);
		
		JLabel lblG_1 = new JLabel("G:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblG_1, 6, SpringLayout.SOUTH, lblR);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblG_1, 0, SpringLayout.WEST, lblRed);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, lblG_1, 26, SpringLayout.SOUTH, lblR);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblG_1, -9, SpringLayout.EAST, lblRed);
		serverConfig.add(lblG_1);
		
		sliderTeamBGreen = new JSlider();
		sliderTeamBGreen.setValue(0);
		sliderTeamBGreen.setMajorTickSpacing(1);
		sliderTeamBGreen.setMinorTickSpacing(1);
		sliderTeamBGreen.setMaximum(255);
		sl_serverConfig.putConstraint(SpringLayout.WEST, sliderTeamBGreen, 0, SpringLayout.WEST, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, sliderTeamBGreen, 0, SpringLayout.SOUTH, lblG_1);
		serverConfig.add(sliderTeamBGreen);
		
		JLabel lblB_1 = new JLabel("B:");
		sl_serverConfig.putConstraint(SpringLayout.NORTH, lblB_1, 6, SpringLayout.SOUTH, lblG_1);
		sl_serverConfig.putConstraint(SpringLayout.WEST, lblB_1, 0, SpringLayout.WEST, lblRed);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, lblB_1, 26, SpringLayout.SOUTH, lblG_1);
		sl_serverConfig.putConstraint(SpringLayout.EAST, lblB_1, -10, SpringLayout.EAST, lblRed);
		serverConfig.add(lblB_1);
		
		sliderTeamBBlue = new JSlider();
		sliderTeamBBlue.setMinorTickSpacing(1);
		sliderTeamBBlue.setMaximum(255);
		sliderTeamBBlue.setMajorTickSpacing(1);
		sliderTeamBBlue.setValue(255);
		sl_serverConfig.putConstraint(SpringLayout.WEST, sliderTeamBBlue, 0, SpringLayout.WEST, sliderTeamAred);
		sl_serverConfig.putConstraint(SpringLayout.SOUTH, sliderTeamBBlue, 0, SpringLayout.SOUTH, lblB_1);
		serverConfig.add(sliderTeamBBlue);
		
		//loadConfig();
	}
	
	public void loadConfig() {
		Configuration cfg = new Configuration(new File((String) Frame.lc.get("aos-dir"), "config.ini"));
		Frame.updateName((String) cfg.get("name"));
		textFieldName.setText((String) cfg.get("name"));
		textFieldWidth.setText((String) cfg.get("xres"));
		textFieldHeight.setText((String) cfg.get("yres"));
		sliderVolume.setValue(Integer.valueOf((String) cfg.get("vol")));
		chckbxInvertedY.setSelected(Boolean.valueOf((String) cfg.get("inverty")));
		chckbxFullscreen.setSelected((String) cfg.get("windowed")=="0");
		sliderSensitivity.setValue((Double.valueOf((String) cfg.get("mouse_sensitivity")).intValue()*100));
	}
	
	public void saveConfig() {
		// client vars
		String username = textFieldName.getText();
		String width = textFieldWidth.getText();
		String height = textFieldHeight.getText();
		String vol = Integer.toString(sliderVolume.getValue());
		String y = chckbxInvertedY.isSelected() ? "1" : "0";
		String full = chckbxFullscreen.isSelected() ? "0" : "1";
		
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMinimumFractionDigits(5);
		String sensitivity = f.format((double) (sliderSensitivity.getValue()/100));
		
		functSaveClient(username, width, height, vol, y, full, sensitivity);
		
		Frame.lc.put("aos-dir", cfgField.getText());
		Frame.lc.save();
	}
	
	private void functSaveClient(String... vars) {
		try {
			System.out.println("Saving New Client Configuration");
			BufferedWriter write = new BufferedWriter(new FileWriter(new File((String) Frame.lc.get("aos-dir"), "config.ini")));
			write.newLine();
			write.write("[client]\r\n");
			write.write("name                           = "+vars[0]+"\r\n");
			write.write("xres                           = "+vars[1]+"\r\n");
			write.write("yres                           = "+vars[2]+"\r\n");
			write.write("vol                            = "+vars[3]+"\r\n");
			write.write("inverty                        = "+vars[4]+"\r\n");
			write.write("windowed                       = "+vars[5]+"\r\n");
			write.write("language                       = 0\r\n"); // Default to English for now.
			write.write("mouse_sensitivity              = "+vars[6]+"\r\n");
			write.write("show_news                      = 0\r\n"); // because fuck Jagex
			write.write("\r\n\r\n");
			write.flush();
			write.close();
			Frame.updateName(vars[0]);
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration() {
		
	}
}

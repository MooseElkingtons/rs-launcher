package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FrameCustom extends JFrame {

	private JLabel lblSelectedVersion = new JLabel("Selected Version: 0."
			+ Main.frame.instanceManager.getInstance());
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private FileOverloadPanel pnlPng, pnlKv6, pnlFnt, pnlWav;
	private boolean isLocked = false;
	
	public FrameCustom(Image icon) {
		setTitle("Custom Files");
		setIconImage(icon);
		setSize(700, 450);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		springLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tabbedPane, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, getContentPane());
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		getContentPane().add(tabbedPane);
		
		load();
		tabbedPane.setSelectedIndex(0);
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				Main.frame.customFrame = null;
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, btnClose, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, -6, SpringLayout.NORTH, btnClose);
		springLayout.putConstraint(SpringLayout.SOUTH, btnClose, -10, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(btnClose);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				save();
				setVisible(false);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, 0, SpringLayout.NORTH, btnClose);
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -6, SpringLayout.WEST, btnClose);
		getContentPane().add(btnSave);
		
		springLayout.putConstraint(SpringLayout.WEST, lblSelectedVersion, 0, SpringLayout.WEST, tabbedPane);
		springLayout.putConstraint(SpringLayout.SOUTH, lblSelectedVersion, -10, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(lblSelectedVersion);
	}
	
	private void load() {
		tabbedPane.removeAll();
		pnlPng = new FileOverloadPanel("png");
		tabbedPane.addTab("PNG", null, pnlPng, "Images for scopes, splashes, and sprites.");
		pnlKv6 = new FileOverloadPanel("kv6");
		tabbedPane.addTab("Kv6", null, pnlKv6, "KV6 Models, such as guns and other weapons.");
		pnlWav = new FileOverloadPanel("wav");
		tabbedPane.addTab("WAV", null, pnlWav, "Audio files for weapons/effects.");
		pnlFnt = new FileOverloadPanel("fonts");
		tabbedPane.addTab("Fonts", null, pnlFnt, "Textual Fonts");
	}
	
	private void save() {
		setVisible(false);
		Main.frame.lockCustoms();
		new Thread(new Runnable() {
			@Override
			public void run() {
				pnlPng.save();
				pnlKv6.save();
				pnlWav.save();
				pnlFnt.save();
				Main.frame.unlockCustoms();
			}
		}).start();
	}
	
	private class FileOverloadPanel extends JPanel {
		
		private File file, defaults, customs;
		private final InstanceManager man = Main.frame.instanceManager;
		private java.util.List<File> defaultFiles, customFiles;
		private JList<String> customList;
		
		public FileOverloadPanel(String fileName) {
			file = new File(man.getInstanceFile(), fileName+"/");
			defaults = new File(Constants.ROOT_DIR, "default/"
					+man.getInstance()+"/"+fileName+"/");
			customs = new File(Constants.ROOT_DIR, "custom/"
					+man.getInstance()+"/"+fileName+"/");
			if(!defaults.exists()) {
				defaults.mkdirs();
				Util.copyDir(file, defaults);
			}
			if(!customs.exists())
				customs.mkdirs();
			defaultFiles = new java.util.ArrayList<File>();
			customFiles = new java.util.ArrayList<File>();
			for(File f : defaults.listFiles())
				defaultFiles.add(f);
			for(File f : customs.listFiles())
				customFiles.add(f);
			
			SpringLayout sl_panel = new SpringLayout();
			setLayout(sl_panel);
			
			JSplitPane splitPane = new JSplitPane();
			sl_panel.putConstraint(SpringLayout.WEST, splitPane, 10, SpringLayout.WEST, this);
			sl_panel.putConstraint(SpringLayout.EAST, splitPane, -10, SpringLayout.EAST, this);
			splitPane.setResizeWeight(0.5);
			add(splitPane);
			
			final JLabel lblPreview = new JLabel("no preview");
			lblPreview.setBackground(SystemColor.control);
			lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
			splitPane.setRightComponent(lblPreview);
			
			final DefaultListModel<String> model
					= new DefaultListModel<String>() {
				@Override
				public int getSize() {
					return customFiles.size();
				}
				@Override
				public String getElementAt(int index) {
					return customFiles.get(index).getName();
				}
			};
			customList = new JList<String>(model);
			customList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					boolean valid = true;
					ImageIcon i = null;
					try {
						i = new ImageIcon(ImageIO.read(
								customFiles.get(customList.getSelectedIndex())));
						valid = i != null;
					} catch(Exception ex) {
						valid = false;
					}
					lblPreview.setIcon((Icon) i);
					if(valid)
						lblPreview.setText("");
					else
						lblPreview.setText("No preview");
				}
			});
			splitPane.setLeftComponent(customList);
			
			
			JLabel lblCustomFiles = new JLabel("Custom Files:");
			sl_panel.putConstraint(SpringLayout.NORTH, splitPane, 6, SpringLayout.SOUTH, lblCustomFiles);
			sl_panel.putConstraint(SpringLayout.WEST, lblCustomFiles, 10, SpringLayout.WEST,this);
			sl_panel.putConstraint(SpringLayout.NORTH, lblCustomFiles, 10, SpringLayout.NORTH,this);
			add(lblCustomFiles);
			
			JLabel lblDefaultFiles = new JLabel("Preview: ");
			sl_panel.putConstraint(SpringLayout.NORTH, lblDefaultFiles, 0, SpringLayout.NORTH, lblCustomFiles);
			sl_panel.putConstraint(SpringLayout.EAST, lblDefaultFiles, -10, SpringLayout.EAST,this);
			add(lblDefaultFiles);
			
			JButton btnAddFile = new JButton("Add File");
			final JFileChooser jf = new JFileChooser();
			btnAddFile.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {	
					int r = jf.showOpenDialog(FrameCustom.this);
					if(r == JFileChooser.APPROVE_OPTION) {
						File f = jf.getSelectedFile();
						customFiles.add(f);
						Util.copy(f, customs);
						DefaultListModel<String> m = (DefaultListModel<String>)
								customList.getModel();
						m.addElement(f.getName());
					}
				}
			});
			sl_panel.putConstraint(SpringLayout.WEST, btnAddFile, 10, SpringLayout.WEST,this);
			sl_panel.putConstraint(SpringLayout.SOUTH, splitPane, -6, SpringLayout.NORTH, btnAddFile);
			sl_panel.putConstraint(SpringLayout.SOUTH, btnAddFile, -10, SpringLayout.SOUTH,this);
			add(btnAddFile);
			
			JButton btnRemoveFile = new JButton("Remove File");
			btnRemoveFile.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int idx = customList.getSelectedIndex();
					System.out.println(customList.getComponentCount());
					if(idx < 0)
						return;
					((DefaultListModel) customList.getModel()).remove(idx);
					File f = new File(customs,
							customFiles.get(idx).getName());
					f.delete();
					customFiles.remove(idx);
				}
			});
			sl_panel.putConstraint(SpringLayout.WEST, btnRemoveFile, 6, SpringLayout.EAST, btnAddFile);
			sl_panel.putConstraint(SpringLayout.SOUTH, btnRemoveFile, 0, SpringLayout.SOUTH, btnAddFile);
			add(btnRemoveFile);
		}
		
		public void save() {
			for(File f : defaultFiles) {
				Util.copy(f, file);
			}
			for(File f : customFiles) {
				File r = new File(file, f.getName());
				if(r.exists())
					Util.copy(f, file);
			}
		}
	}
}

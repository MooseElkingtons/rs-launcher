package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class FrameBlacklist extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;

	public FrameBlacklist(Image icon) {
		super("Royal Spades - Blacklist");
		setIconImage(icon);
		setSize(new Dimension(450, 300));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		table = new JTable();
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Filter"
			}
		) {
			private static final long serialVersionUID = 3L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(350);
		
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -40, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPane);
		
		JButton btnDelNode = new JButton("Delete Node");
		btnDelNode.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row > -1)
					deleteNode(row);
			}
		});
		btnDelNode.setIcon(Util.getIcon("blackheart_delete"));
		springLayout.putConstraint(SpringLayout.WEST, btnDelNode, -125, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnDelNode, -10, SpringLayout.EAST, getContentPane());
		btnDelNode.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(btnDelNode);
		
		JButton btnAddNode = new JButton("Add Node");
		btnAddNode.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Main.frame.blacklist.add(editNode(false));
			}
		});
		btnAddNode.setIcon(Util.getIcon("blackheart_add"));
		springLayout.putConstraint(SpringLayout.NORTH, btnDelNode, 0, SpringLayout.NORTH, btnAddNode);
		springLayout.putConstraint(SpringLayout.NORTH, btnAddNode, -33, SpringLayout.SOUTH, getContentPane());
		btnAddNode.setHorizontalAlignment(SwingConstants.LEFT);
		springLayout.putConstraint(SpringLayout.WEST, btnAddNode, -121, SpringLayout.WEST, btnDelNode);
		springLayout.putConstraint(SpringLayout.EAST, btnAddNode, -6, SpringLayout.WEST, btnDelNode);
		getContentPane().add(btnAddNode);
		
		JButton btnEditNode = new JButton("Edit Node");
		btnEditNode.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row > -1) {
					String node = table.getValueAt(row, 0).toString();
					String newNode = editNode(false, node);
					if(newNode == null || newNode.trim().length() < 1)
						return;
					Main.frame.blacklist.remove(node);
					Main.frame.blacklist.add(newNode);
					table.setValueAt(newNode, row, 0);
				} else {
					JOptionPane.showMessageDialog(null, "You must select a " +
							"node before you could edit one!", "Royal Spade" +
							"s - Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnEditNode.setIcon(Util.getIcon("blackheart_edit"));
		btnEditNode.setHorizontalAlignment(SwingConstants.LEFT);
		springLayout.putConstraint(SpringLayout.NORTH, btnEditNode, 7, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnEditNode, -121, SpringLayout.WEST, btnAddNode);
		springLayout.putConstraint(SpringLayout.EAST, btnEditNode, -6, SpringLayout.WEST, btnAddNode);
		getContentPane().add(btnEditNode);
	}
	
	public void loadBlacklist() {
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		for(String node : Main.frame.blacklist.get()) {
			model.addRow(new Object[] {node});
		}
	}
	
	public void addNode(String node) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[] {node});
	}
	
	public void deleteNode(int row) {
		DefaultTableModel model = ((DefaultTableModel) table.getModel());
		String node = model.getValueAt(row, 0).toString();
		model.removeRow(row);
		Main.frame.blacklist.remove(node);
	}
	
	public String editNode(boolean tried) {
		return editNode(tried, "");
	}
	
	public String editNode(boolean tried, String prev) {
		String pane = JOptionPane.showInputDialog("<html>Please insert a" +
				(tried ? " <strong><font color=RED>VALID</font></strong> "
						: " ") + "filter for the blacklist.</html>", prev);
		if(pane.trim().length() < 1 || pane == null)
			return null;
		if(Main.frame.blacklist.get().contains(pane))
			return editNode(true, pane);
		return pane;
	}
}

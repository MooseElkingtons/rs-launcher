package com.mooseelkingtons.royalspades;

import java.awt.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Window.Type;

public class FrameConnection extends JFrame {
	private JTextField textField;

	public FrameConnection(Image icon) {
		super("Royal Spades - Connect to Server");
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(icon);
		setSize(new Dimension(370, 140));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblPleaseInsertAn = new JLabel("<html><center>Please insert an Ace of Spades URL to the text field below to<br> connect, then click 'connect'.</center></html>");
		lblPleaseInsertAn.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, lblPleaseInsertAn, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblPleaseInsertAn, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblPleaseInsertAn);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, lblPleaseInsertAn);
		springLayout.putConstraint(SpringLayout.EAST, lblPleaseInsertAn, 0, SpringLayout.EAST, textField);
		springLayout.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, 354, SpringLayout.WEST, getContentPane());
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnCancel);
		
		JButton btnConnect = new JButton("Connect");
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 0, SpringLayout.NORTH, btnCancel);
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -6, SpringLayout.WEST, btnCancel);
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String u = textField.getText();
				if(!u.contains("aos://")) {
					textField.setForeground(Color.RED);
				} else {
					textField.setForeground(Color.black);
					Main.connectToServer(textField.getText());
					setVisible(false);
				}
			}
		});
		getContentPane().add(btnConnect);
	}
}

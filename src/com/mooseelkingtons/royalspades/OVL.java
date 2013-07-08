package com.mooseelkingtons.royalspades;

import java.io.*;

import javax.swing.JOptionPane;

public class OVL implements Runnable {

	private byte[] buffer = new byte[1024];
	private int playerId = 0;
	
	public static Process process;
	
	public OVL(int playerId) {
		this.playerId = playerId;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Initiating OVL075");
			String filePath = new File(Constants.ROOT_DIR, "ovl075.exe").getAbsolutePath();
			process = Runtime.getRuntime().exec("\""+filePath+"\" "+playerId);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Main.frame, "There was an error while running OVL.",
					"Royal Spades - OVL Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
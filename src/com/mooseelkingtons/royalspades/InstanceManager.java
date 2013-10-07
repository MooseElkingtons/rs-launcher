package com.mooseelkingtons.royalspades;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class InstanceManager {
	
	public static final String INSTANCE_URL = "https://dl.dropboxusercontent.com/u/60275959/instances/";
	
	private int instance = 75;
	
	public InstanceManager() {
		File instanceBet = new File(Constants.ROOT_DIR, "instances/076/");
		if(!instanceBet.exists())
			instanceBet.mkdirs();
		File instanceRec = new File(Constants.ROOT_DIR, "instances/075/");
		if(!instanceRec.exists()) {
			System.out.println("Couldn't find Ace of Spades instances.");
			instanceRec.mkdirs();
			downloadInstances();
		}
		instance = Integer.parseInt(Main.rsCfg.get("instance").toString());
	}
	
	public void downloadInstances() {
		System.out.println("Downloading Ace of Spades Instances.");
		try {
			System.out.println("Downloading 0.75");
			ZipUtil.downloadZip(new URL(
					INSTANCE_URL + "075.zip"),
					new File(Constants.ROOT_DIR,
							"instances/075/"));
			System.out.println("Downloading 0.76");
			ZipUtil.downloadZip(new URL(
					INSTANCE_URL + "076.zip"),
					new File(Constants.ROOT_DIR,
							"instances/076/"));
			
		} catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "There was an error while " +
					"downloading\r\nAce of Spades versions 0.75 and 0.76.",
					"Royal Spades - Error", JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("Finished downloading Ace of Spades instances.");
	}
	
	public void setInstance(int instance) {
		this.instance = instance;
	}
	
	public int getInstance() {
		return instance;
	}
	
	public File getInstanceFile() {
		return new File(Constants.ROOT_DIR, "instances/0"+instance+"/");
	}
	
	public void saveConfiguration(String... vars) {
		try {
			System.out.println("Saving New Client Configuration");
			BufferedWriter write;
			for(int i = 0; i <= 1; i++) {
				switch(i) {
					case 0:
						write = new BufferedWriter(new FileWriter(
								new File(Constants.ROOT_DIR, "instances/075/" +
										"config.ini")));
						break;
					default:
						write = new BufferedWriter(new FileWriter(
								new File(Constants.ROOT_DIR, "instances/076/" + 
										"config.ini")));
				}
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
				write.write("show_news                      = 0\r\n"); // useless.
				write.write("\r\n\r\n");
				write.flush();
				write.close();
			}
			Frame.updateName(vars[0]);
			Main.frame.configFrame.loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

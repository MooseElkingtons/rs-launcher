package com.mooseelkingtons.royalspades;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class InstanceManager {
	
	public static final String INSTANCE_URL = "https://dl.dropboxusercontent.com/u/60275959/instances/";
	
	private int instance = 75;
	
	public InstanceManager() {
		if(!checkIntegrity(new File(Constants.ROOT_DIR, "instances/076/")) ||
			!checkIntegrity(new File(Constants.ROOT_DIR, "instances/075/")) ||
			!checkIntegrity(new File(Constants.ROOT_DIR, "instances/os/"))) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
							"Royal Spades could not find the necessary\r\n" +
							"files required to function properly. Please\r\n" +
							"wait for Royal Spades to download the files.",
							"Royal Spades", JOptionPane.INFORMATION_MESSAGE);
				}
			}).start();
			downloadInstances();
		}
		try {
			instance = Integer.parseInt(Main.rsCfg.get("instance"));
		} catch(NumberFormatException e) {
			instance = 75;
			Main.rsCfg.put("instance", "75");
			Main.rsCfg.save();
		}
	}
	
	private boolean checkIntegrity(File file) {
		if(!file.exists()) {
			file.mkdirs();
			System.out.println("Couldn't find Ace of Spades instance: "+
						file+".");
			return false;
		}
		return true;
	}
	
	public void downloadInstances() {
		System.out.println("Downloading Ace of Spades Instances.");
		System.out.println("Downloading 0.75");
		downloadInstance("075");
		System.out.println("Downloading 0.76");
		downloadInstance("076");
		System.out.println("Downloading Open Spades.");
		downloadInstance("os");
		System.out.println("Finished downloading Ace of Spades instances.");
	}
	
	private void downloadInstance(String file) {
		try {
			ZipUtil.downloadZip(new URL(INSTANCE_URL + file + ".zip"),
					new File(Constants.ROOT_DIR, "instances/"+file+"/"));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "There was an error while " +
					"downloading\r\nAce of Spades",
					"Royal Spades - Error", JOptionPane.ERROR_MESSAGE);
		}
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
				write.write("[client]\r\n");
				write.write("name                           = "+vars[0]+"\r\n");
				Main.frame.cfg().put("name", vars[0]);
				write.write("xres                           = "+vars[1]+"\r\n");
				Main.frame.cfg().put("xres", vars[1]);
				write.write("yres                           = "+vars[2]+"\r\n");
				Main.frame.cfg().put("yres", vars[2]);
				write.write("vol                            = "+vars[3]+"\r\n");
				Main.frame.cfg().put("vol", vars[3]);
				write.write("inverty                        = "+vars[4]+"\r\n");
				Main.frame.cfg().put("inverty", vars[4]);
				write.write("windowed                       = "+vars[5]+"\r\n");
				Main.frame.cfg().put("windowed", vars[5]);
				write.write("language                       = 0\r\n"); // Default to English for now.
				write.write("mouse_sensitivity              = "+vars[6]+"\r\n");
				Main.frame.cfg().put("mouse_sensitivity", vars[6]);
				write.write("show_news                      = 0\r\n"); // useless.
				write.write("\r\n\r\n");
				write.flush();
				write.close();
			}
			Frame.updateName(vars[0]);
			Main.frame.configFrame.loadConfig(Main.frame);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

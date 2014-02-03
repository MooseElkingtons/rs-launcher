package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

public class Main {

	public static Image icon;
	
	public static HashMap<String, Image> images = new HashMap<String, Image>();

	public static Frame frame = null;
	
	public static double v = -1.0;
	
	public static Thread ovlThread;
	
	public static Configuration rsCfg;
		
	public static void main(String... args) {
		setLookAndFeel();
		init();
		try {
			icon = Util.getIcon("icon").getImage();
		} catch(Exception e) {
			icon = null;
		}
		frame = new Frame("Royal Spades Launcher - v"+Constants.VERSION, icon);
		frame.setVisible(true);
		try {
			Frame.updateTable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
		frame.blacklistFrame.loadBlacklist();
		frame.configFrame.loadConfig(frame);
	}
	
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void init() {
		// load RS Configuration
		loadConfiguration();
		
		// Check if the root directory exists
		if(!Constants.ROOT_DIR.exists())
			Constants.ROOT_DIR.mkdirs();
		
		// Load default flag(unknown) into memory
		loadDefaultFlag();
		boolean autoUpdate = false;
		try {
			autoUpdate = (Integer.parseInt((String) rsCfg.get("auto_update"))) == 1;
		} catch(Exception e) {
			autoUpdate = true;
			rsCfg.put("auto_update", "1");
			rsCfg.save();
		}
		if(autoUpdate) {
			double lv = getLatestVersion();
			if(lv != Constants.VERSION)
				downloadLatest(lv);
		} else {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					double lv = getLatestVersion();
					if(lv != Constants.VERSION) {
						int response = JOptionPane.showConfirmDialog(null,
								"There is a newer version of " +
								"Royal Spades ("+lv+"). Update?",
								"Royal Spades - Update Confirmation",
								JOptionPane.YES_NO_OPTION);
						if(response == JOptionPane.YES_OPTION)
							downloadLatest(lv);
					}
				}
			});
			t.start();
		}
	}
	
	private static void loadConfiguration() {
		rsCfg = new Configuration(new File(System.getProperty("user.home"),
				"rs_config.ini"), "=");
		String[] keys = new String[] {"instance", "auto_update", "open_spades"};
		boolean unexistingNode = false;
		for(String s : keys) {
			if(rsCfg.get(s) == null)
				unexistingNode = true;
		}
		if(!rsCfg.file.exists() || unexistingNode) {
			System.out.println("Couldn't find Royal Spades Configuration.");
			rsCfg.put("instance", "75");
			rsCfg.put("auto_update", "1");
			rsCfg.put("open_spades", "0");
			rsCfg.save();
		}
	}
	
	private static void loadDefaultFlag() {
		try {
			Util.images.put("unknown",
					ImageIO.read(Main.class.getResourceAsStream(
							"/icons/flags/unknown.png")));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double getLatestVersion() {
		try {
			if(v < 0) {
				URL url = new URL(Constants.MAIN_URL + "version.txt");
				InputStream in = url.openStream();
				byte[] buffer = new byte[1024];
				int l = 0;
				StringBuilder sb = new StringBuilder();
				while((l = in.read(buffer)) > -1) {
					sb.append(new String(buffer, 0, l));
				}
				v = Double.valueOf(new String(sb));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return v;
	}
	
	public static void downloadLatest(double lv) {
		System.out.println("Version outdated. Latest version: "+lv+". Current version: "+Constants.VERSION);
		try {
			File x = new File("./Royal Spades.jar");
			FileOutputStream f = new FileOutputStream(x);
			URL u = new URL(Constants.MAIN_URL + "Royal%20Spades.jar");
			InputStream in = u.openStream();
			byte[] b = new byte[1024];
			int l = 0;
			while((l = in.read(b)) != -1) {
				f.write(b, 0, l);
			}
			f.flush();
			f.close();
			in.close();
			restart();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void restart(String... args) {
		try {
			String command = "java -jar \"Royal Spades.jar\"";
			if(args.length != 0)
				for(String x : args)
					command += " " + x;
			Runtime.getRuntime().exec(command);
			System.out.println("Restarting: ["+command+"]");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void connectToLocalhost() {
		connectToServer("aos://16777343:"+Constants.DEFAULT_PORT);
	}
	
	public static void connectToServer(String url) {
		System.out.println("Attempting to connect to "+url);
		Util.execClient(url);
	}
}
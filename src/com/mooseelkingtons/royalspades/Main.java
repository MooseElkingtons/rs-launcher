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

	public static double v = -1.0;
	
	public static void main(String... args) {

		setLookAndFeel();
		init();
		try {
			icon = ImageIO.read(Main.class.getResourceAsStream("/icon.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		Frame f = new Frame("Royal Spades Launcher - v"+Constants.VERSION, icon);
		f.setVisible(true);
		try {
			
			Frame.updateTable();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void init() {
		double lv = getLatestVersion();
		if(lv != Constants.VERSION) {
			System.out.println("Version outdated. Latest version: "+lv+". Current version: "+Constants.VERSION);
			downloadLatest();
		}
		else {
			System.out.println("Royal Spades is up to date! ["+lv+"]");
		}
	}
	
	public static double getLatestVersion() {
		try {
			if(v < 0) {
				URL url = new URL(Constants.MAIN_URL + "version.txt");
				System.out.println(Constants.MAIN_URL+"version.txt");
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
	
	public static void downloadLatest() {
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
		connectToServer("aos://"+Util.ipToAos("127.0.0.1")+":"+Constants.DEFAULT_PORT);
	}
	
	public static void connectToServer(String url) {
		System.out.println("Attempting to connect to "+url);
		Util.browseURI(url);
	}
}
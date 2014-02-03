package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Util {
	private static byte[] buffer = new byte[1024];
	public static HashMap<String, Image> images = new HashMap<String, Image>(); // Used for caching images
	
	public static String readURL(URL url) {
		try {
			InputStreamReader in = new InputStreamReader(url.openStream());
			char[] buffer = new char[1024];
			int l = 0;
			StringBuilder sb = new StringBuilder();
			while((l = in.read(buffer, 0, buffer.length)) > -1) {
				sb.append(buffer, 0, l);
			}
			in.close();
			return new String(sb);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String aosToIp(int ipInt) { // Thanks to rakiru for reference
		String ip = "";
		for(int i = 0; i < 4; i++) {
			switch(i) {
				case 3:
					ip += (ipInt>>(i*8) & 255);
					break;
				default:
					ip += (ipInt>>(i*8) & 255) + ".";
			}
		}
		return ip;
	}
	
	public static String toHex(int integer) {
		String x = Integer.toHexString(integer).toUpperCase();		
		return "0x"+ (x.length() < 2 ? "0"+x : x);
	}
	
	public static int ipToAos(String ip) { // Thanks to rakiru for reference
		String[] o = ip.split("\\.");
		int[] octet = new int[4];
		for(int i = 0; i < octet.length; i++) {
			octet[i] = Integer.parseInt(o[i]);
		}
		return (((octet[3] * 256) + octet[2]) * 256 + octet[1]) * 256 + octet[0];
	}

	public static void browseURI(String url) throws Exception {
		// may throw "Access Denied" IOException while opening URI for whatever reason.
		URI u = new URI(url);
		Desktop.getDesktop().browse(u);
	}
	
	public static void execClient(String url) {
		try {
			String filePath, cmd = "-"+url;
			ProcessBuilder pb;
			if(Integer.parseInt(Main.rsCfg.get("open_spades").toString()) == 0) {
				filePath = new File(Frame.instanceManager.getInstanceFile(),
						"/client.exe").getAbsolutePath();
				pb = new ProcessBuilder("\""+filePath+"\"", cmd);
				pb.start();
			} else {
				filePath = new File(Constants.ROOT_DIR, "instances/os/" +
						"OpenSpades.exe").getAbsolutePath();
				String args = "v="+Frame.instanceManager.getInstance();
				pb = new ProcessBuilder("\""+filePath+"\"",
						url, args);
				System.out.println(filePath+" "+url+" "+args);
				pb.start();
				Main.restart();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean ovlExists() {
		return new File(Constants.ROOT_DIR, "ovl075.exe").exists();
	}
	
	public static void downloadFile(URL url, String fileName) {
		try {
			File file = new File(Constants.ROOT_DIR, fileName);
			if(!file.exists())
				file.createNewFile();
			InputStream in = url.openStream();
			FileOutputStream out = new FileOutputStream(file);
			int len = 0;
			while((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ImageIcon getIcon(String fileName) {
		Image icon = images.get(fileName.toLowerCase());
		if(icon == null) {
			try {
				icon = ImageIO.read(Main.class.getResourceAsStream(
						String.format("/icons/%s.png", fileName)));
				images.put(fileName.toLowerCase(), icon);
			} catch(Exception e) {
				return null;
			}
		}
		return new ImageIcon(icon);
	}
	
	public static void copyDir(File from, File to) {
		if(!from.isDirectory())
			return;
		if(!to.exists())
			to.mkdirs();
		for(File f : from.listFiles()) {
			copy(f, to);
		}
	}
	
	public static void copy(File from, File to) {
		try {
			File w = new File(to, from.getName());
			if(!w.exists())
				w.createNewFile();
			BufferedOutputStream write = new BufferedOutputStream(
					new FileOutputStream(w));
			BufferedInputStream read = new BufferedInputStream(
					new FileInputStream(from));
			byte[] buffer = new byte[1024];
			int l = 0;
			while((l = read.read(buffer)) > -1) {
				write.write(buffer, 0, l);
			}
			write.flush();
			write.close();
			read.close();
			System.out.println("Copied "+from.getName()
					+" to "+to.getCanonicalPath());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not copy file "
					+from.getName()+": "+e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

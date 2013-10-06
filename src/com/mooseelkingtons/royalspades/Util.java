package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Util {
	private static byte[] buffer = new byte[1024];
	public static HashMap<String, Image> images = new HashMap<String, Image>(); // Used for caching images
		
	public static Image getStoredImage(String fileName) {
		String u = "http://www.buildandshoot.com/resources/country_flags/_"+fileName.toLowerCase()+".png";
		Image x = images.get(fileName.toLowerCase());
		if(x == null) {
			try {
				return downloadImageToFlags(new URL(u), fileName);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return x;
	}
	
	public static Image downloadImageToFlags(URL url, String name) {
		try {
			URLConnection ur = url.openConnection();
			ur.setRequestProperty("User-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36" +
					" (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 ");
			BufferedImage i = ImageIO.read(ur.getInputStream());
			images.put(name.toLowerCase(), i);
			File f = new File(Constants.ROOT_DIR+"/res/flags/"+name.toLowerCase()+".png");
			if(!f.exists())
				f.createNewFile();
			ImageIO.write((RenderedImage) i, "png",(OutputStream) new FileOutputStream(f));
			return i;
		} catch(IOException e) {
			e.printStackTrace();
			return images.get("unknown");
		}
	}
	
	public static String getCountryCode(String name) {
		try {
			URL url = new URL("http://mooseelkingtons.com/flagiso.php?name="+URLEncoder.encode(name, "UTF-8"));
			return readURL(url);
		} catch (IOException e) {
			// Do nothing for now, I guess.
		}
		return "unknown";
	}
	
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
	
	public static int ipToAos(String ip) { // Thanks to rakiru for reference
		String[] o = ip.split("\\.");
		int[] octet = new int[] {};
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
			String filePath = new File((String) Main.frame.lc.get("aos-dir"), "client.exe").getAbsolutePath();
			System.out.println("Executing Client");
			ProcessBuilder pb = new ProcessBuilder("\""+filePath+"\"", "-"+url);
			pb.start();
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
		Image icon;
		try {
			icon = ImageIO.read(Main.class.getResourceAsStream(
					String.format("/icons/%s.png", fileName)));
		} catch(Exception e) {
			return null;
		}
		return new ImageIcon(icon);
	}
}

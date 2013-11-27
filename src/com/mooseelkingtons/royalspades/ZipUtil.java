package com.mooseelkingtons.royalspades;

import java.io.*;
import java.net.*;
import java.util.zip.*;

/**
 * A Zip Utility which allows you to download and unpack Compressed Files. 
 * Ported from previous project (Aerotana, by MooseElkingtons)
 * 
 * @author moose
 */

public class ZipUtil {

	private static byte[] buffer = new byte[1024];
	/**
	 * Downloads a Zip Archive and extracts it to specified folder.
	 * 
	 * @param file The URL to the zipped file.
	 * @param dump The file to download and extract everything to.
	 */
	public static void downloadZip(URL file, File dump) throws IOException {		
		ZipInputStream in = new ZipInputStream(file.openStream());
		ZipEntry en = in.getNextEntry();
		while(en != null) {
			recursiveUnpack(in, en, dump);
			en = in.getNextEntry();
		}
		in.close();
	}
	
	private static void recursiveUnpack(ZipInputStream in, ZipEntry en, File dump) throws IOException {
		System.out.println(en.getName());
		String name = en.getName().replace("\\", "/");
		if(en.isDirectory()) {
			if(!name.endsWith("/"))
				name += "/";
			File f = new File(dump, en.getName());
			if(!f.exists())
				f.mkdirs();
		} else {
			File f = new File(dump, name);
			createDirFor(dump, name);
			FileOutputStream out = new FileOutputStream(f);
			int len = 0;
			while((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
		}
	}
	
	private static void createDirFor(File dump, String s) {
		try {
			String[] d = s.split("/");
			String x = "";
			for(int i = 0; i < d.length - 1; i++) {
				x += d[i]+"/";
			}
			File f = new File(dump, x);
			if(!f.exists()) {
				System.out.println("Creating directory: "+x);
				f.mkdirs();
			}
		} catch(Exception e) {
			
		}
	}
}

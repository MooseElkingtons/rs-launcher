package com.mooseelkingtons.royalspades;

import java.io.*;
import java.util.*;

public class Configuration {

	public File file;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	public boolean exists = false;
	
	public Configuration(File file) {
			this.file = file;
			load();
	}
	
	
	private void load() {
		try {
			BufferedReader read = new BufferedReader(new FileReader(file));
			String rd;
			while((rd = read.readLine()) != null) {
				String[] tokens = rd.split("\\=\\ ");
				if(tokens.length > 1) {
					String key = tokens[0].trim();
					String value = tokens[1].split(" ;")[0];
					map.put(key, value);
				}
			}
			read.close();
			exists = map.size() > 0;
		} catch(FileNotFoundException e) {
			// do nothing
			return;
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
		
	public Object get(String key) {
		return map.get(key);
	}
	
	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	public void save() {
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(file));
			for(String s : map.keySet()) {
				write.write(s + " = " + map.get(s));
				write.newLine();
			}
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

package com.mooseelkingtons.royalspades;

import java.io.*;
import java.util.*;

public class Configuration {

	public File file;
	private HashMap<String, String> map = new HashMap<String, String>();
	public boolean exists = false;
	private String split = "=";
	
	public Configuration(File file, String split) {
			this.file = file;
			this.split = split;
			load();
	}
	
	
	private void load() {
		try {
			BufferedReader read = new BufferedReader(new FileReader(file));
			String rd;
			while((rd = read.readLine()) != null) {
				String[] tokens = rd.split(split);
				if(tokens.length > 1) {
					String key = tokens[0].trim();
					String value = tokens[1].trim().split(";")[0];
					map.put(key, value);
				}
			}
			read.close();
			exists = map.size() > 0;
		} catch(FileNotFoundException e) {
			return;
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
		
	public String get(String key) {
		return map.get(key);
	}
	
	public Map<String, String> getAll() {
		return map;
	}
	
	public void putAll(Map<String, String> map) {
		this.map.putAll(map);
	}
	
	public void put(String key, String value) {
		map.put(key, value);
	}
	
	public void save() {
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(file));
			for(String s : map.keySet()) {
				write.write(s + split + map.get(s));
				write.newLine();
			}
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

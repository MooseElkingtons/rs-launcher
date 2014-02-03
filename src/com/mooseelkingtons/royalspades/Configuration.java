package com.mooseelkingtons.royalspades;

import java.io.*;
import java.util.*;

public class Configuration {

	public File file;
	private HashMap<String, String> map = new HashMap<String, String>();
	public boolean exists = false;
	private String split = "=";
	private HashMap<Integer, String> headers = new HashMap<Integer, String>();
	
	public Configuration(File file, String split) {
			this.file = file;
			this.split = split;
			load();
	}
	
	public void addHeader(String header, int line) {
		headers.put(line, header);
	}
	
	private void load() {
		try {
			BufferedReader read = new BufferedReader(new FileReader(file));
			String rd;
			int line = 0;
			while((rd = read.readLine()) != null) {
				String r = rd.trim();
				if(r.startsWith("[")) {
					headers.put(line, r.substring(1, r.indexOf("]")));
					line++;
				}
				String[] tokens = r.split(split);
				if(tokens.length > 1) {
					String key = tokens[0].trim();
					String value = tokens[1].trim().split(";")[0];
					map.put(key, value);
					line++;
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
			int line = 0;
			for(String s : map.keySet()) {
				if(headers.containsKey(line)) {
					write.write("["+headers.get(line)+"]");
					write.newLine();
					line++;
				}
				write.write(s +" "+ split +" "+ map.get(s));
				write.newLine();
				line++;
			}
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

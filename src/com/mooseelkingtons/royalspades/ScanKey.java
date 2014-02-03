package com.mooseelkingtons.royalspades;

import java.util.*;

public class ScanKey {
	private int key;
	private String name;
	public static HashMap<Integer, ScanKey> keys = new HashMap<Integer, ScanKey>();
	public static HashMap<String, Integer> names = new HashMap<String, Integer>();
	
	public ScanKey(int key, String name) {
		this.key = key;
		this.name = name;
		keys.put(key, this);
		names.put(name, key);
	}
	
	public String getName() {
		return name;
	}
	
	public int getKey() {
		return key;
	}
}
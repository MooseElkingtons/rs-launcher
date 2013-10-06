package com.mooseelkingtons.royalspades;

import java.io.*;
import java.util.*;

import javax.swing.*;

public class Blacklist {
		
	private List<String> nodes = new ArrayList<String>();
	
	public void load() {
		try {
			File file = new File(Constants.ROOT_DIR, "blacklist.list");
			if(!file.exists())
				file.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(file));
			String r = "";
			while((r = in.readLine()) != null) {
				nodes.add(r);
			}
			in.close();
		} catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error while trying to load the Blacklist.",
					"Royal Spades - Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void add(String node) {
		if(nodes.contains(node))
			return;
		try {
			File file = new File(Constants.ROOT_DIR, "blacklist.list");
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.write(
					String.format("%s",
							node));
			out.newLine();
			out.flush();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error while trying to add a node to " +
					"the Blacklist.", "Royal Spades - Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			nodes.add(node);
			Main.frame.blacklistFrame.addNode(node);
		}
	}
	
	public void remove(String node) {
		if(!nodes.contains(node))
			return;
		try {
			File file = new File(Constants.ROOT_DIR, "blacklist.list");
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			for(String n : nodes) {
				if(!n.equalsIgnoreCase(node)) {
					out.write(
							String.format("%s",
									n));
					out.newLine();
				}
			}
			out.flush();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "There was an error while " +
					"trying to remove a node from the Blacklist.", "Royal " +
							"Spades - Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			nodes.remove(node);
		}
	}
	
	public void save() {
		try {
			File file = new File(Constants.ROOT_DIR, "blacklist.list");
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			for(String node : nodes) {
				out.write(String.format("%s",
						node));
				out.newLine();
			}
			out.flush();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error while trying to save the Blacklist.",
					"Royal Spades - Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public List<String> get() {
		return nodes;
	}
	
	public boolean contains(String anotherString) {
		for(String node : nodes) {
			if(anotherString.toLowerCase().contains(node.toLowerCase()))
					return true;
		}
		return false;
	}

}

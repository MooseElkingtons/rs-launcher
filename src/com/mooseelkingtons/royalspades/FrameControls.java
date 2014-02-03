package com.mooseelkingtons.royalspades;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.*;

public class FrameControls extends JFrame {
	
	public static final ScanKey SK_ESCAPE = new ScanKey(0x01, "Escape"); // <esc>
	public static final ScanKey SK_ONE = new ScanKey(0x02, "1"); // 1
	public static final ScanKey SK_TWO = new ScanKey(0x03, "2"); // 2
	public static final ScanKey SK_THREE = new ScanKey(0x04, "3"); // 3
	public static final ScanKey SK_FOUR = new ScanKey(0x05, "4"); // 4
	public static final ScanKey SK_FIVE = new ScanKey(0x06, "5"); // 5
	public static final ScanKey SK_SIX = new ScanKey(0x07, "6"); // 6
	public static final ScanKey SK_SEVEN = new ScanKey(0x08, "7"); // 7
	public static final ScanKey SK_EIGHT = new ScanKey(0x09, "8"); // 8
	public static final ScanKey SK_NINE = new ScanKey(0x0A, "9"); // 9
	public static final ScanKey SK_ZERO = new ScanKey(0x0B, "0"); // 0
	public static final ScanKey SK_DASH = new ScanKey(0x0C, "- or _"); // -
	public static final ScanKey SK_EQUAL = new ScanKey(0x0D, "+ or ="); // =
	public static final ScanKey SK_BACKSPACE = new ScanKey(0x0E, "Backspace"); // <bksp>
	public static final ScanKey SK_TAB = new ScanKey(0x0F, "Tab"); // <tab>
	public static final ScanKey SK_Q = new ScanKey(0x10, "Q"); // Q
	public static final ScanKey SK_W = new ScanKey(0x11, "W"); // W
	public static final ScanKey SK_E = new ScanKey(0x12, "E"); // E
	public static final ScanKey SK_R = new ScanKey(0x13, "R"); // R
	public static final ScanKey SK_T = new ScanKey(0x14, "T"); // T
	public static final ScanKey SK_Y = new ScanKey(0x15, "Y"); // Y
	public static final ScanKey SK_U = new ScanKey(0x16, "U"); // U
	public static final ScanKey SK_I = new ScanKey(0x17, "I"); // I
	public static final ScanKey SK_O = new ScanKey(0x18, "O"); // O
	public static final ScanKey SK_P = new ScanKey(0x19, "P"); // P
	public static final ScanKey SK_LEFT_BRACE = new ScanKey(0x1A, "[ or {"); // [
	public static final ScanKey SK_RIGHT_BRACE = new ScanKey(0x1B, "] or }"); // ]
	public static final ScanKey SK_ENTER = new ScanKey(0x1C, "Enter"); // <enter>
	public static final ScanKey SK_CONTROL = new ScanKey(0x1D, "Ctrl"); // <ctrl>
	public static final ScanKey SK_A = new ScanKey(0x1E, "A"); // A
	public static final ScanKey SK_S = new ScanKey(0x1F, "S"); // S
	public static final ScanKey SK_D = new ScanKey(0x20, "D"); // D
	public static final ScanKey SK_F = new ScanKey(0x21, "F"); // F
	public static final ScanKey SK_G = new ScanKey(0x22, "G"); // G
	public static final ScanKey SK_H = new ScanKey(0x23, "H"); // H
	public static final ScanKey SK_J = new ScanKey(0x24, "J"); // J
	public static final ScanKey SK_K = new ScanKey(0x25, "K"); // K
	public static final ScanKey SK_L = new ScanKey(0x26, "L"); // L
	public static final ScanKey SK_SEMI_COLON = new ScanKey(0x27, "; or :"); // ; :
	public static final ScanKey SK_APOSTROPHE = new ScanKey(0x28, "' or \""); // '
	public static final ScanKey SK_GRAVE = new ScanKey(0x29, "Grave"); // `
	public static final ScanKey SK_LEFT_SHIFT = new ScanKey(0x2A, "LShift"); // <shift>
	public static final ScanKey SK_SLASH = new ScanKey(0x2B, "\\"); // \
	public static final ScanKey SK_Z = new ScanKey(0x2C, "Z"); // Z
	public static final ScanKey SK_X = new ScanKey(0x2D, "X"); // X
	public static final ScanKey SK_C = new ScanKey(0x2E, "C"); // C
	public static final ScanKey SK_V = new ScanKey(0x2F, "V"); // V
	public static final ScanKey SK_B = new ScanKey(0x30, "B"); // B
	public static final ScanKey SK_N = new ScanKey(0x31, "N"); // N
	public static final ScanKey SK_M = new ScanKey(0x32, "M"); // M
	public static final ScanKey SK_COMMA = new ScanKey(0x33, ", or <"); // ,
	public static final ScanKey SK_PERIOD = new ScanKey(0x34, ". or >"); // .
	public static final ScanKey SK_BACKSLASH = new ScanKey(0x35, "/"); // /
	public static final ScanKey SK_RIGHT_SHIFT = new ScanKey(0x36, "RShift"); // <shift>
	public static final ScanKey SK_PRINT_SCREEN = new ScanKey(0x37, "PrtScn"); // <PtScr>
	public static final ScanKey SK_ALT = new ScanKey(0x38, "Alt"); // <alt>
	public static final ScanKey SK_SPACE = new ScanKey(0x39, "Space"); // <space>
	public static final ScanKey SK_CAPS_LOCK = new ScanKey(0x3A, "CpsLk"); // <CpsLk>
	public static final ScanKey SK_F1 = new ScanKey(0x3B, "F1"); // <f1>
	public static final ScanKey SK_F2 = new ScanKey(0x3C, "F2"); // <f2>
	public static final ScanKey SK_F3 = new ScanKey(0x3D, "F3"); // <f3>
	public static final ScanKey SK_F4 = new ScanKey(0x3E, "F4"); // <f4>
	public static final ScanKey SK_F5 = new ScanKey(0x3F, "F5"); // <f5>
	public static final ScanKey SK_F6 = new ScanKey(0x40, "F6"); // <f6>
	public static final ScanKey SK_F7 = new ScanKey(0x41, "F7"); // <f7>
	public static final ScanKey SK_F8 = new ScanKey(0x42, "F8"); // <f8>
	public static final ScanKey SK_F9 = new ScanKey(0x43, "F9"); // <f9>
	public static final ScanKey SK_F10 = new ScanKey(0x44, "F10"); // <f10>
	public static final ScanKey SK_NUM_LOCK = new ScanKey(0x45, "NumLk"); // <NumLk>
	public static final ScanKey SK_SCROLL_LOCK = new ScanKey(0x46, "ScrlLk"); // <ScrlLk>
	public static final ScanKey SK_HOME = new ScanKey(0x47, "Home"); // <home>
	public static final ScanKey SK_ARROW_UP = new ScanKey(0x48, "Up Arrow"); // <Up>
	public static final ScanKey SK_PAGE_UP = new ScanKey(0x49, "Page Up"); // <PgUp>
	public static final ScanKey SK_NUMPAD_DASH = new ScanKey(0x4A, "Dash (num)"); // -
	public static final ScanKey SK_NUMPAD_FOUR = new ScanKey(0x4B, "4 (num)"); // 4
	public static final ScanKey SK_NUMPAD_FIVE = new ScanKey(0x4C, "5 (num)"); // 5
	public static final ScanKey SK_NUMPAD_SIX = new ScanKey(0x4D, "6 (num)"); // 6
	public static final ScanKey SK_NUMPAD_ADD = new ScanKey(0x4E, "+ (num)"); // +
	public static final ScanKey SK_NUMPAD_ONE = new ScanKey(0x4F, "1 (num)"); // 1
	public static final ScanKey SK_NUMPAD_TWO = new ScanKey(0x50, "2 (num)"); // 2
	public static final ScanKey SK_NUMPAD_THREE = new ScanKey(0x51, "3 (num)"); // 3
	public static final ScanKey SK_NUMPAD_ZERO = new ScanKey(0x52, "0 (num)"); // 0
	public static final ScanKey SK_DELETE = new ScanKey(0x53, "Delete"); // <del>
	public static final ScanKey SK_F11 = new ScanKey(0x85, "F11"); // <f11>
	public static final ScanKey SK_F12 = new ScanKey(0x86, "F12"); // <f12>

	private Configuration ctrlCfg;
	private HashMap<String, JComboBox<String>> map =
			new HashMap<String, JComboBox<String>>();
	private java.util.List<String> names = new ArrayList<String>();
	
	JPanel pane = new JPanel();
	SpringLayout sl_pane = new SpringLayout();
	
	public FrameControls(Image icon) {
		ctrlCfg = new Configuration(
				new File(Frame.instanceManager.getInstanceFile(),
						"controls.ini"), "=");
		setIconImage(icon);
		setTitle("Controls");
		setSize(275, 600);
		
		for(ScanKey s : ScanKey.keys.values()) {
			names.add(s.getName());
		}
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		load();
		
		JScrollPane scrollPane = new JScrollPane(pane);
		pane.setLayout(sl_pane);
		
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPane);
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				load();
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, btnClose, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, btnClose);
		springLayout.putConstraint(SpringLayout.SOUTH, btnClose, -10, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(btnClose);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				save();
				setVisible(false);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, 0, SpringLayout.NORTH, btnClose);
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -6, SpringLayout.WEST, btnClose);
		getContentPane().add(btnSave);
		
	}
	
	public void save() {
		for(String key : map.keySet()) {
			JComboBox<String> m = map.get(key);
			String p = (String) m.getSelectedItem();
			int i = ScanKey.names.get(p);
			ctrlCfg.put(key, Util.toHex(i));
		}
		File f075 = new File(Constants.ROOT_DIR, "instances/075/");
		File f076 = new File(Constants.ROOT_DIR, "instances/076/");
		Configuration fr = new Configuration(new File(f075, "controls.ini"), "=");
		Configuration fd = new Configuration(new File(f076, "controls.ini"), "=");
		fr.putAll(ctrlCfg.getAll());
		fd.putAll(ctrlCfg.getAll());
		fr.save();
		fd.save();
	}
	
	public void load() {
		for(JComboBox<String> m : map.values()) {
			pane.remove(m);
		}
		map.clear();
		HashMap<String, String> cfg = new HashMap<String, String>(
				ctrlCfg.getAll());
		String lastKey = "";
		for(String key : cfg.keySet()) {
			String k = cfg.get(key);
			int in = Integer.parseInt(k.replace("0x", ""), 16);
			if(ScanKey.keys.containsKey(in)) {
				JLabel label = new JLabel(key.replace("_", " ")+": ");
				label.setHorizontalAlignment(SwingConstants.TRAILING);
				if(!lastKey.isEmpty())
					sl_pane.putConstraint(SpringLayout.NORTH, label, 8, SpringLayout.SOUTH, map.get(lastKey));
				else
					sl_pane.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, pane);
				sl_pane.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, pane);
				pane.add(label);
				
				JComboBox<String> comboBox = new JComboBox<String>();
				comboBox.setEditable(false);
				for(String s : names) {
					comboBox.addItem(s);
				}
				comboBox.setSelectedItem(ScanKey.keys.get(in).getName());
				sl_pane.putConstraint(SpringLayout.WEST, comboBox, 125, SpringLayout.WEST, pane);
				sl_pane.putConstraint(SpringLayout.EAST, label, -10, SpringLayout.WEST, comboBox);
				if(!lastKey.isEmpty())
					sl_pane.putConstraint(SpringLayout.NORTH, comboBox, 5, SpringLayout.SOUTH, map.get(lastKey));
				else
					sl_pane.putConstraint(SpringLayout.NORTH, comboBox, 10, SpringLayout.NORTH, pane);
				sl_pane.putConstraint(SpringLayout.EAST, comboBox, -10, SpringLayout.EAST, pane);
				pane.add(comboBox);
				map.put(key, comboBox);
				lastKey = key;
			}
		}
	}
}

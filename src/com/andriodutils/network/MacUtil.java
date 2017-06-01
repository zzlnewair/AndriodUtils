package com.andriodutils.network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class MacUtil {

	
	
	
	private MacUtil(){
		
		throw new AssertionError();
	}
	
	
	/**
	 * 获取Ethernet的MAC地址
	 * 
	 * @return
	 */
	public static String getEthernetMac() {
		try {
			return loadFileAsString("/sys/class/net/eth0/address").toUpperCase(
					Locale.ENGLISH).substring(0, 17);
		} catch (IOException e) {
			return null;
		}
	}
/*
 * 获取wifi的MAC地址
 * /sys/class/net/wlan0/address
 * 
 * 
 */
	public static String getWifiMac(){
		
		
		try {
			return loadFileAsString("/sys/class/net/wlan0/address").toUpperCase(
					Locale.ENGLISH).substring(0, 17);
		} catch (IOException e) {
			return null;
		}
		
	}
	
	
	private static String loadFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}
	
	

}

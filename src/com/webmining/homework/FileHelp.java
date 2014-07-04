/*
 * 文件读写类
 */
package com.webmining.homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FileHelp {
	// 读文件转成dict
	public static Set<String> readFileToDict(String fileName) {
		File file = new File(fileName);
		Set<String> set = new HashSet<String>();
		String s = null;
		StringBuffer sb = new StringBuffer();
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "UTF-8"));
				while ((s = br.readLine()) != null) {
					//sb.append(s);
					set.add(s.trim());
				}
				//System.out.println(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在!");
		}
		return set;
	}

	// 写文件
	public static void writeToFile(String path, String content) {
		try {
			File f = new File(path);
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(f,true));
			writer.write(content);
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

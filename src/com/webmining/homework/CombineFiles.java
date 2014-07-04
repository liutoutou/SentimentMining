package com.webmining.homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class CombineFiles {
	private final static String INPUT_FILE_DIR = "files\\语料100篇";
	private final static String INPUT_FILE_TOKENIZE="files\\inputTokenize.txt";
	private final static String INPUT_FILES_TO_ONEFILE="files\\inputfile.txt";

	public static void main(String[] args) throws Exception, IOException {
		readfile(INPUT_FILE_DIR);
	}

	public static boolean readfile(String filepath)
			throws FileNotFoundException, IOException {
		try {
			PaodingAnalyzer analyzer = new PaodingAnalyzer();
			File file = new File(filepath);

			if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						InputStreamReader read = new InputStreamReader(
								new FileInputStream(readfile));
						BufferedReader bufferedReader = new BufferedReader(read);
						String lineTXT = null;
						StringBuilder sb = new StringBuilder();
						StringBuilder text=new StringBuilder();
						while ((lineTXT = bufferedReader.readLine()) != null) {
							text.append(lineTXT.trim());
							
							//分词
							/*StringReader sr = new StringReader(lineTXT.trim().replace("", " "));
							TokenStream ts = analyzer.tokenStream("", sr);
							try {
								org.apache.lucene.analysis.Token token;
								while ((token = ts.next()) != null) {
									sb.append(token.termText());
									sb.append(" ");
								}

							} catch (Exception e) {
							}*/
						}
						//FileHelp.writeToFile(INPUT_FILE_TOKENIZE,readfile.getName() + "	"+ sb.toString());
						FileHelp.writeToFile(INPUT_FILES_TO_ONEFILE,readfile.getName() + "	"+ text.toString());
						
					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return true;
	}
}

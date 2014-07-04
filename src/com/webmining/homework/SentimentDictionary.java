/*
 * 获取情感词及程度词集合
 * 词典文本文件—>Set集合
 */
package com.webmining.homework;

import java.util.Set;

public class SentimentDictionary {
	private final static String POS_FILE_STRING = "files\\dict\\posdict.txt";
	private final static String NEG_FILE_STRING = "files\\dict\\negdict.txt";
	private final static String MOST_FILE_STRING = "files\\dict\\mostdict.txt";
	private final static String VERY_FILE_STRING = "files\\dict\\verydict.txt";
	private final static String MORE_FILE_STRING = "files\\dict\\moredict.txt";
	private final static String ISH_FILE_STRING = "files\\dict\\ishdict.txt";
	private final static String INSUFFICENT_FILE_STRING = "files\\dict\\insufficentdict.txt";
	private final static String INVERSE_STRING = "files\\dict\\inversedict.txt";

	public static void main(String[] args) {
		Set<String> posdic = getPositiveDictionary();
		Set<String> negdic = getNegativeDictionary();
		Set<String> mostdict = getMostDictionary();
		Set<String> verydict = getVeryDictionary();
		Set<String> moredict = getMoreDictionary();
		Set<String> ishdict = getIshDictionary();
		Set<String> insufficentdict = getInsufficentDictionary();
		Set<String> inversedict = getInverseeDictionary();
	}

	//积极
	public static Set<String> getPositiveDictionary() {
		return FileHelp.readFileToDict(POS_FILE_STRING);
	}
	//消极
	public static Set<String> getNegativeDictionary() {
		return FileHelp.readFileToDict(NEG_FILE_STRING);
	}
	//极其、最
	public static Set<String> getMostDictionary() {
		return FileHelp.readFileToDict(MOST_FILE_STRING);
	}
	//很
	public static Set<String> getVeryDictionary() {
		return FileHelp.readFileToDict(VERY_FILE_STRING);
	}
	//较
	public static Set<String> getMoreDictionary() {
		return FileHelp.readFileToDict(MORE_FILE_STRING);
	}
	//稍
	public static Set<String> getIshDictionary() {
		return FileHelp.readFileToDict(ISH_FILE_STRING);
	}
	//欠
	public static Set<String> getInsufficentDictionary() {
		return FileHelp.readFileToDict(INSUFFICENT_FILE_STRING);
	}

	public static Set<String> getInverseeDictionary() {
		return FileHelp.readFileToDict(INVERSE_STRING);
	}
}

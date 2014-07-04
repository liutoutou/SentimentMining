/*
 * 测试程序
 */
package com.webmining.homework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.TokenStream;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class PaodingTest {

	public static void main(String[] args) throws IOException {
		Set<String> posdic = SentimentDictionary.getPositiveDictionary();
		Set<String> negdic = SentimentDictionary.getNegativeDictionary();
		Set<String> mostdict = SentimentDictionary.getMostDictionary();
		Set<String> verydict = SentimentDictionary.getVeryDictionary();
		Set<String> moredict = SentimentDictionary.getMoreDictionary();
		Set<String> ishdict = SentimentDictionary.getIshDictionary();
		Set<String> insufficentdict = SentimentDictionary
				.getInsufficentDictionary();
		Set<String> inversedict = SentimentDictionary.getInverseeDictionary();

		Map<String, String> map = new HashMap<String, String>();
		StringBuilder segmentword = null;

		String testStr = "这手机的画面极好，操作也比较流畅。不过拍照真的太烂了！系统也不好。";
		String regEx = "[：？！。，； ]";
		String[] result = testStr.split(regEx);

		PaodingAnalyzer analyzer = new PaodingAnalyzer();
		for (int i = 0; i < result.length; i++) {
			segmentword = new StringBuilder();
			List<String> list = new ArrayList<String>();
			
			System.out.println("PaodingAnalyzer分词:");
			StringReader sr = new StringReader(result[i]);
			TokenStream ts = analyzer.tokenStream("", sr);
			org.apache.lucene.analysis.Token token;
			while ((token = ts.next()) != null) {
				System.out.println(token.termText());
				list.add(token.termText());
			}
			System.out.println("IKAnalyzer分词:");
			StringReader reader = new StringReader(result[i]);
			IKSegmentation ik = new IKSegmentation(reader, true);// 当为true时，分词器进行最大词长切分
			Lexeme lexeme = null;
			while ((lexeme = ik.next()) != null) {
				System.out.println(lexeme.getLexemeText());
				//list.add(lexeme.getLexemeText());
			}
			System.out.println();
			int pos = 0; // 记录扫描到的词的位置
			int a = 0; // 记录情感词的位置
			float poscount = 0; // 积极词的第一次分值
			float poscount3 = 0; // 积极词的最后分值
			float negcount = 0;
			float negcount3 = 0;

			for (String word : list) {
				if (posdic.contains(word)) {
					segmentword = new StringBuilder(word);
					poscount += 1;
					int c = 0;
					List<String> segtmp = new ArrayList<String>();
					for (int count = 0; count < pos; count++) {
						segtmp.add(list.get(count));
					}
					for (String w : segtmp) {
						if (mostdict.contains(w)) {
							poscount *= 4.0;
						} else if (verydict.contains(w)) {
							poscount *= 3.0;
						} else if (moredict.contains(w)) {
							poscount *= 2.0;
						} else if (ishdict.contains(w)) {
							poscount /= 2.0;
						} else if (insufficentdict.contains(w)) {
							poscount /= 4.0;
						} 

					}
					poscount3+=poscount;
					if (segmentword != null) {
						map.put(segmentword.toString(),format(poscount3, negcount3) + ":"+ result[i]);
					}
				} else if (negdic.contains(word)) {
					segmentword = new StringBuilder(word);
					negcount += 1;
					int d = 0;
					List<String> segtmp = new ArrayList<String>();
					for (int count = 0; count < pos; count++) {
						segtmp.add(list.get(count));
					}
					for (String w : segtmp) {
						if (mostdict.contains(w)) {
							negcount *= 4.0;
						} else if (verydict.contains(w)) {
							negcount *= 3.0;
						} else if (moredict.contains(w)) {
							negcount *= 2.0;
						} else if (ishdict.contains(w)) {
							negcount /= 2.0;
						} else if (insufficentdict.contains(w)) {
							negcount /= 4.0;
						} 
					}
					negcount3+=negcount;
					
					if (segmentword != null) {
						map.put(segmentword.toString(),
								format(poscount3, negcount3) + ":" + result[i]);
					}	
				}
				pos += 1;
			}
		}
		for (Map.Entry<String, String> MapString : map.entrySet()) {
			String key = MapString.getKey();// 次方法获取键值对的名称
			String value = MapString.getValue();// 次方法获取键值对的值
			System.out.println(key + ":" + value);
		}
	}

	public static String format(float poscount3, float negcount3) {
		return poscount3 + ":" + negcount3;
	}
}

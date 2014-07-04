/*
 * 情感词值计算
 */
package com.webmining.homework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.TokenStream;

public class SentimentScore {
	// 常量定义
	public final static String INPUT_FILE_STRING = "files\\inputfile.txt";
	public final static String STUDENT_NUM = "1130332027";
	public final static String SENTIMENT_OUTDIR_STRING = "files\\sentimentresult.txt";

	// 获取情感词及程度词集合
	public static Set<String> posdic = SentimentDictionary
			.getPositiveDictionary();
	public static Set<String> negdic = SentimentDictionary
			.getNegativeDictionary();
	public static Set<String> mostdict = SentimentDictionary
			.getMostDictionary();
	public static Set<String> verydict = SentimentDictionary
			.getVeryDictionary();
	public static Set<String> moredict = SentimentDictionary
			.getMoreDictionary();
	public static Set<String> ishdict = SentimentDictionary.getIshDictionary();
	public static Set<String> insufficentdict = SentimentDictionary
			.getInsufficentDictionary();
	public static Set<String> inversedict = SentimentDictionary
			.getInverseeDictionary();

	// 主程序
	public static void main(String[] args) throws IOException {
		List<SentimentModel> sentimentList = new ArrayList<SentimentModel>();
		Map<String, String> mapList = new HashMap<String, String>();
		String s = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(INPUT_FILE_STRING)), "UTF-8"));
		while ((s = br.readLine()) != null) {
			mapList.putAll(splitClause(s));
		}
		// 格式化结果
		sentimentList = formatList(mapList);
		// 按置信度值排序
		Collections.sort(sentimentList);
		// 输出结果至文件中
		int len = sentimentList.size();
		System.out.println(len);

		int count = 1;
		for (int i = len - 1; i >= len-100; i--) {
			SentimentModel model = sentimentList.get(i);
			String formatString = count++ + "	" + model.getStudentnum() + "	"
					+ model.getWordString() + "	" + model.getDocid() + "	"
					+ model.getWordpolarity() + "	" + model.getContextString()
					+ "	" + model.getConfidentscore();
			FileHelp.writeToFile(SENTIMENT_OUTDIR_STRING, formatString);// 输出结果到文件
			System.out.println(formatString);
		}
	}

	// 对文件中句子进行分句
	public static Map<String, String> splitClause(String file)
			throws IOException {
		String testStr = file.split("	")[1];
		String fileName = file.split("	")[0];
		String regEx = "[：？！。，；!.,~ ]";
		String[] result = testStr.split(regEx);
		Map<String, String> mapList = new HashMap<String, String>();
		for (int i = 0; i < result.length; i++) {
			mapList.putAll(splitWords(fileName, result[i]));
		}
		return mapList;
	}

	// 按句子分词
	public static Map<String, String> splitWords(String fileName, String clause)
			throws IOException {
		PaodingAnalyzer analyzer = new PaodingAnalyzer();
		List<String> list = new ArrayList<String>();
		// 分词
		StringReader sr = new StringReader(clause);
		TokenStream ts = analyzer.tokenStream("", sr);
		org.apache.lucene.analysis.Token token;
		while ((token = ts.next()) != null) {
			list.add(token.termText());
		}

		Map<String, String> maplist = new HashMap<String, String>();
		maplist.putAll(getSentimentScore(list, fileName, clause));
		return maplist;
	}

	// 格式化结果Map-->list
	public static List<SentimentModel> formatList(Map<String, String> map) {
		List<SentimentModel> list = new ArrayList<SentimentModel>();

		for (Map.Entry<String, String> MapString : map.entrySet()) {
			String key = MapString.getKey();// 次方法获取键值对的名称
			String value = MapString.getValue();// 次方法获取键值对的值
			// System.out.println(key + ":" + value);

			String[] valueStrings = value.split(":");

			String posScore = valueStrings[0];
			String negScore = valueStrings[1];
			String docid = valueStrings[2];
			String clauseString = valueStrings[3];
			String confidentScore = Float.parseFloat(posScore) > Float
					.parseFloat(negScore) ? posScore : negScore;
			String polarity = Float.parseFloat(posScore) > Float
					.parseFloat(negScore) ? "pos" : "neg";

			SentimentModel model = new SentimentModel();
			model.setWordString(key);
			model.setStudentnum(STUDENT_NUM);
			model.setDocid(docid);
			model.setConfidentscore(confidentScore);
			model.setWordpolarity(polarity);
			model.setContextString(clauseString);
			list.add(model);
		}
		return list;
	}

	// 计算情感词分值
	public static Map<String, String> getSentimentScore(List<String> list,
			String fileName, String clause) {

		int p = 0; // 记录扫描到的词的位置
		float posScore = 0; // 积极词的第一次分值
		float posScoreResult = 0; // 积极词的最后分值
		float negScore = 0;
		float negScoreResult = 0;

		Map<String, String> map = new HashMap<String, String>();
		StringBuilder segmentword = null;

		for (String word : list) {
			// positive
			if (SentimentScore.posdic.contains(word)) {
				segmentword = new StringBuilder(word);
				posScore += 1;
				List<String> segtmp = new ArrayList<String>();
				for (int count = 0; count < p; count++) {
					segtmp.add(list.get(count));
				}
				// 查找情感词前的程度词，并赋予相应的权值计算情感值
				for (String w : segtmp) {
					if (SentimentScore.mostdict.contains(w)) {
						posScore *= 4.0;
					} else if (SentimentScore.verydict.contains(w)) {
						posScore *= 3.0;
					} else if (SentimentScore.moredict.contains(w)) {
						posScore *= 2.0;
					} else if (SentimentScore.ishdict.contains(w)) {
						posScore /= 2.0;
					} else if (SentimentScore.insufficentdict.contains(w)) {
						posScore /= 4.0;
					}
				}
				posScoreResult += posScore;
				if (segmentword != null) {
					map.put(segmentword.toString(),
							format(posScoreResult, negScoreResult) + ":"
									+ fileName + ":" + clause);
				}
			}
			// negative
			if (SentimentScore.negdic.contains(word)) {
				segmentword = new StringBuilder(word);
				negScore += 1;
				List<String> segtmp = new ArrayList<String>();
				for (int count = 0; count < p; count++) {
					segtmp.add(list.get(count));
				}
				for (String w : segtmp) {
					if (SentimentScore.mostdict.contains(w)) {
						negScore *= 4.0;
					} else if (SentimentScore.verydict.contains(w)) {
						negScore *= 3.0;
					} else if (SentimentScore.moredict.contains(w)) {
						negScore *= 2.0;
					} else if (SentimentScore.ishdict.contains(w)) {
						negScore /= 2.0;
					} else if (SentimentScore.insufficentdict.contains(w)) {
						negScore /= 4.0;
					}
				}
				negScoreResult += negScore;

				if (segmentword != null) {
					map.put(segmentword.toString(),
							format(posScoreResult, negScoreResult) + ":"
									+ fileName + ":" + clause);
				}
			}
			p += 1;
		}

		return map;
	}

	// 格式化积极、消极输出结果
	public static String format(float posScoreResult, float negScoreResult) {
		return posScoreResult + ":" + negScoreResult;
	}
}

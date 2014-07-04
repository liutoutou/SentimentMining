/*
 * 实体类
 */
package com.webmining.homework;

public class SentimentModel implements Comparable<SentimentModel>{
	private String studentnum;     //学号
	private String wordString;     //情感词
	private String docid;          //所在文档
	private String wordpolarity;   //词的极性
	private String contextString;  //情感词前后文
	private String confidentscore; //情感词值
	
	public String getStudentnum() {
		return studentnum;
	}
	public void setStudentnum(String studentnum) {
		this.studentnum = studentnum;
	}
	public String getWordString() {
		return wordString;
	}
	public void setWordString(String wordString) {
		this.wordString = wordString;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getWordpolarity() {
		return wordpolarity;
	}
	public void setWordpolarity(String wordpolarity) {
		this.wordpolarity = wordpolarity;
	}
	public String getContextString() {
		return contextString;
	}
	public void setContextString(String contextString) {
		this.contextString = contextString;
	}
	public String getConfidentscore() {
		return confidentscore;
	}
	public void setConfidentscore(String confidentscore) {
		this.confidentscore = confidentscore;
	}
	@Override
	public int compareTo(SentimentModel arg0) {
		 return this.getConfidentscore().compareTo(arg0.getConfidentscore());
	}
}

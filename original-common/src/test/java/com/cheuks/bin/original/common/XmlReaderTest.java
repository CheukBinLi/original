package com.cheuks.bin.original.common;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.cheuks.bin.original.common.util.xml.XmlReaderAll;

public class XmlReaderTest {

//	String a="<xml>"+
//			"<ToUserName><![CDATA[toUser]]></ToUserName>"+
//			"<FromUserName><![CDATA[fromUser]]></FromUserName>"+
//			"<CreateTime>1357290913</CreateTime>"+
//			"<MsgType><![CDATA[voice]]></MsgType>"+
//			"<MediaId><![CDATA[media_id]]></MediaId>"+
//			"<Format><![CDATA[Format]]></Format>"+
//			"<Recognition><![CDATA[腾讯微信团队]]></Recognition>"+
//			"<MsgId>1234567890123456</MsgId>"+
//			"</xml>";
	
	XmlReaderAll xra=new XmlReaderAll();
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException, IOException {
		String str="<xml>"+
				"<B><ToUserName><![CDATA[toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[fromUser]]></FromUserName>"+
				"<CreateTime>1357290913</CreateTime>"+
				"<MsgType><![CDATA[voice]]></MsgType>"+
				"<MediaId><![CDATA[media_id]]></MediaId>"+
				"<Format><![CDATA[Format]]></Format>"+
				"<Recognition><![CDATA[腾讯微信团队]]></Recognition>"+
				"<MsgId>1234567890123456</MsgId></B>"+
				"<ToUserName><![CDATA[toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[fromUser]]></FromUserName>"+
				"<CreateTime>1357290913</CreateTime>"+
				"<MsgType><![CDATA[voice]]></MsgType>"+
				"<MediaId><![CDATA[media_id]]></MediaId>"+
				"<Format><![CDATA[Format]]></Format>"+
				"<Recognition><![CDATA[腾讯微信团队]]></Recognition>"+
				"<MsgId>1234567890123456</MsgId>"+
				"</xml>";
		A a=XmlReaderAll.paddingModel(str.getBytes(),XmlReaderTest.A.class);
		System.out.println(a.getToUserName());
		System.out.println(a.getRecognition());
	}
	
	public static class A{
		private A B;
		private String ToUserName;
		private String FromUserName;
		private String CreateTime;
		private String MsgType;
		private String MediaId;
		private String Format;
		private String Recognition;
		private String MsgId;
		public String getToUserName() {
			return ToUserName;
		}
		public A setToUserName(String toUserName) {
			ToUserName = toUserName;
			return this;
		}
		public String getFromUserName() {
			return FromUserName;
		}
		public A setFromUserName(String fromUserName) {
			FromUserName = fromUserName;
			return this;
		}
		public String getCreateTime() {
			return CreateTime;
		}
		public A setCreateTime(String createTime) {
			CreateTime = createTime;
			return this;
		}
		public String getMsgType() {
			return MsgType;
		}
		public A setMsgType(String msgType) {
			MsgType = msgType;
			return this;
		}
		public String getMediaId() {
			return MediaId;
		}
		public A setMediaId(String mediaId) {
			MediaId = mediaId;
			return this;
		}
		public String getFormat() {
			return Format;
		}
		public A setFormat(String format) {
			Format = format;
			return this;
		}
		public String getRecognition() {
			return Recognition;
		}
		public A setRecognition(String recognition) {
			Recognition = recognition;
			return this;
		}
		public String getMsgId() {
			return MsgId;
		}
		public A setMsgId(String msgId) {
			MsgId = msgId;
			return this;
		}
		public A() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
	}

}

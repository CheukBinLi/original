package com.cheuks.bin.original.test;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

import com.sun.mail.util.MailSSLSocketFactory;

public class MailX {

	@Test
	public void a() throws GeneralSecurityException, MessagingException {
		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("mdp-admin", "admin4@MDP");
			}
		};

		Properties props = new Properties();
		props.put("mail.smtp.host", "10.17.32.111");
		props.put("mail.smtp.port", 25);
		props.put("mail.transport.protocol", "smtp");// 发送邮件的协议
		props.put("mail.smtp.auth", "true");
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "false");
		props.put("mail.smtp.ssl.socketFactory", sf);

		Session sendMailSession = Session.getDefaultInstance(props, authenticator);
		sendMailSession.setDebug(true);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress("mdp-admin@midea.com.cn");
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);

		mailMessage.setRecipients(Message.RecipientType.TO, new Address[] {
				new InternetAddress("20796698@qq.com")
		});

		mailMessage.setSubject("商城网购账单对账异常提醒：京东的JD20170220004账单对账时出现异常数据，共2笔，合计金额379.00请登录美捷报查看并及时处理；");

		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		html.setContent("袁勔，你好<br><br>&nbsp&nbsp&nbsp京东的JD20170220004账单中存在对账失败数据，共2笔，合计金额379.00。<br><br>&nbsp&nbsp&nbsp为保证集团整体的正常结算，请在7号前完成异常处理，7号将再次对账，届时对于仍匹配失败的订单将直接剔除结算，造成的滞纳金由延迟结算的单位承担。<br><br><br><br>异常处理地址：http://localhost:3000/#/app/smart_buy_account/exception/jdExceptionOrders/list/109801872155475968", "text/html; charset=utf-8");
		mainPart.addBodyPart(html);

		mailMessage.setContent(mainPart);

		Transport trans = sendMailSession.getTransport("smtp");
		// 邮件服务器名,用户名，密码
		// String host = cdpMsmMailMessage.getMailServerHost();
		System.out.println("0");
		trans.connect("10.17.32.111", "mdp-admin", "admin4@MDP");
		// trans.sendMessage(msg, msg.getAllRecipients());
		// 发送邮件
		Transport.send(mailMessage);
		trans.close();
		
		System.out.println("1");
	}

	public static void main(String[] args) throws GeneralSecurityException, MessagingException {
		new MailX().a();
	}

}

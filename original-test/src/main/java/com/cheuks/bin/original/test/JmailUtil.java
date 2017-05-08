package com.midea.smart.buy.account.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author 作者 ：Bin
 * @version 版本：1.0
 * @createTime 创建时间：2017年04月19日 17:38
 * @E-mail 邮箱：chaobin.li@meicloud.com
 * 类说明
 */
public class JmailUtil {

    public void send(final MailModel mailModel) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail."+mailModel.getProtocol()+".auth", Boolean.toString(mailModel.isAuth()));//设置访问smtp服务器需要认证
//        props.setProperty("mail.smtps.auth", Boolean.toString(mailModel.isAuth()));//设置访问smtp服务器需要认证
        props.setProperty("mail.transport.protocol", mailModel.getProtocol()); //设置访问服务器的协议

        Session session = Session.getDefaultInstance(props);

        session.setDebug(true); //打开debug功能

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailModel.getMailUser())); //设置发件人，163邮箱要求发件人与登录用户必须一致（必填），其它邮箱不了解
        msg.setSubject(mailModel.getMailThreme()); //设置邮件主题
        msg.setText(mailModel.getMailContent()); //设置邮件内容

        Transport trans = session.getTransport();
        trans.connect(mailModel.getServerAddress(), mailModel.getPort(), mailModel.getMailUser(), mailModel.getMailPassword()); //连接邮箱smtp服务器，25为默认端口
        trans.sendMessage(msg, new Address[]{new InternetAddress(mailModel.getReceiveAddress())}); //发送邮件
        trans.close(); //关闭连接
    }

    public static class MailModel{
        /***
         * 发件人邮箱
         */
        private String mailUser;
        /***
         * 邮箱密码
         */
        private String mailPassword;
        /***
         * 邮箱服务器地址
         */
        private String serverAddress;
        /***
         * 端口:25
         */
        private int port=25;
        /***
         * 是否需要验证,默认为true
         */
        private boolean auth=true;
        /***
         * 协议:smtp,pop3
         */
        private String protocol;
        /***
         * 收件邮箱
         */
        private String receiveAddress;
        /***
         * 邮件主题
         */
        private String mailThreme;
        /***
         * 邮件内容
         */
        private String mailContent;

        public String getMailUser() {
            return mailUser;
        }

        public MailModel setMailUser(String mailUser) {
            this.mailUser = mailUser;
            return this;
        }

        public String getMailPassword() {
            return mailPassword;
        }

        public MailModel setMailPassword(String mailPassword) {
            this.mailPassword = mailPassword;
            return this;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public MailModel setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public int getPort() {
            return port;
        }

        public MailModel setPort(int port) {
            this.port = port;
            return this;
        }

        public boolean isAuth() {
            return auth;
        }

        public MailModel setAuth(boolean auth) {
            this.auth = auth;
            return this;
        }

        public String getProtocol() {
            return protocol;
        }

        public MailModel setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public String getReceiveAddress() {
            return receiveAddress;
        }

        public MailModel setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
            return this;
        }

        public String getMailThreme() {
            return mailThreme;
        }

        public MailModel setMailThreme(String mailThreme) {
            this.mailThreme = mailThreme;
            return this;
        }

        public String getMailContent() {
            return mailContent;
        }

        public MailModel setMailContent(String mailContent) {
            this.mailContent = mailContent;
            return this;
        }

        public MailModel(String mailUser, String mailPassword, String serverAddress,int port, boolean auth, String protocol, String receiveAddress, String mailThreme, String mailContent) {
            this.mailUser = mailUser;
            this.mailPassword = mailPassword;
            this.serverAddress = serverAddress;
            this.port=port;
            this.auth = auth;
            this.protocol = protocol;
            this.receiveAddress = receiveAddress;
            this.mailThreme = mailThreme;
            this.mailContent = mailContent;
        }

        public MailModel() {
        }
    }
    public static void main(String[] args) throws MessagingException {
//        MailModel mailModel=new MailModel("f41234567890f4@163.com","zxcvbnm0","smtp.163.com",false,"smtp","20796698@qq.com","工文","工文");
        MailModel mailModel=new MailModel("chaobin.li@meicloud.com","Aa_123456","mail.midea.com",994,true,"smtps","20796698@meicloud.com","工文","工文");
//        MailModel mailModel=new MailModel("f41234567890f4@163.com","zxcvbnm0","smtp.163.com",false,"smtp","20796698@qq.com","工文","工文");
        new JmailUtil().send(mailModel);
    }

}

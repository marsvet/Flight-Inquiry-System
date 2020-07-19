package edu.cauc.flight_inquiry.util;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


public class MailUtil {

  private static String senderAccount = "1358044937@qq.com";	// 发件人邮箱
  private static String senderPassword = "lgtwkslkgpkdghjf";	// 发件人密码
  private static String senderName = "民航信息查询系统";
  private static String smtpHost = "smtp.qq.com";
  private static String smtpPort = "465";


  public static void sendMail(String receiverAccount, String subject, String msg) {
	try {
	  Properties props = new Properties();	// 参数配置
	  props.setProperty("mail.transport.protocol", "smtp");	// 使用的协议（JavaMail规范要求）
	  props.setProperty("mail.smtp.host", smtpHost);	// 发件人的邮箱的 SMTP 服务器地址
	  props.setProperty("mail.smtp.auth", "true");
	  props.setProperty("mail.smtp.port", smtpPort);
	  props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	  props.setProperty("mail.smtp.socketFactory.fallback", "false");
	  props.setProperty("mail.smtp.socketFactory.port", smtpPort);

	  Session session = Session.getDefaultInstance(props);
	  session.setDebug(true);

	  MimeMessage message = createMimeMessage(session, senderAccount, receiverAccount, subject, msg);

	  Transport transport = session.getTransport();
	  transport.connect(senderAccount, senderPassword);
	  transport.sendMessage(message, message.getAllRecipients());
	  transport.close();

	} catch (Exception ex) {
	  System.out.println(ex.getMessage());
	}
  }

  private static MimeMessage createMimeMessage(Session session, String senderAccount, String receiverAccount, String subject, String msg) {
	MimeMessage message = new MimeMessage(session);
	try {
	  message.setFrom(new InternetAddress(senderAccount, senderName, "UTF-8"));	// 绑定发件人
	  message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiverAccount, "尊敬的用户", "UTF-8"));	// 绑定收件人
	  message.setSubject(subject, "UTF-8");	// 邮件主题
	  message.setContent(msg, "text/html; charset=UTF-8");	// 邮件正文
	  message.setSentDate(new Date());	// 发送时间
	  message.saveChanges();	// 保存设置
	} catch (Exception ex) {
	  System.out.println(ex.getMessage());
	}
	return message;
  }

}
package edu.cauc.flight_inquiry.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class PwdUtil {

  public static String getHashedPwd(String str) {

	String res = "";

	try {
	  BASE64Encoder base64En = new BASE64Encoder();
	  MessageDigest md5 = MessageDigest.getInstance("MD5");
	  res = base64En.encode(md5.digest(str.getBytes("UTF-8")));
	} catch (Exception ex) {
	  System.out.println(ex.getMessage());
	}

	return res;

  }

  public static boolean checkPwd(String pwd, String hashedPwd) {

	return getHashedPwd(pwd).equals(hashedPwd);

  }

}

package com;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 	常用字符串处理方法
 */
public class ToString {
	
	/**
	 * 	将给定的字符串加密成MD5串
	 * @param string 给定的字符串
	 * @return MD5 字符串
	 */
	public static String getMD5(String string) {
		String encoder = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = string.getBytes("UTF8");
			byte[] hash = md.digest(bytes);
			encoder = Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			System.out.println("1.不支持MD5算法异常;2.不支持UTF8格式异常.");
		}
		
		return encoder;
	}
}

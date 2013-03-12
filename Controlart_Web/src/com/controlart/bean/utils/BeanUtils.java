package com.controlart.bean.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class BeanUtils {
	public static final String 	LOGIN_PAGE 						= "/login.xhtml";
	public static final String 	HOME_PAGE						= "/home.xhtml";
	
	public static final String 	LOGGEDUSER_SESSION_ATTRIBUTE 	= "usuarioLogado";

	public static final int    	SEVERITY_ERROR 					= 1;
	public static final int    	SEVERITY_FATAL 					= 2;
	public static final int    	SEVERITY_INFO 					= 3;
	public static final int    	SEVERITY_WARN 					= 4;

	private static final String CRIPTOGRAPHY_ALGORITHM 			= "md5";

	public static final String encryptPassword(String password)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(CRIPTOGRAPHY_ALGORITHM);
		BASE64Encoder encoder = new BASE64Encoder();

		md.update(password.getBytes());

		return toHexCode(encoder.encode(md.digest()).getBytes());
	}

	private static final String toHexCode(byte[] passHash) {
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < passHash.length; i++) {
			if ((0xff & passHash[i]) < 0x10)
				hexString.append("0"
						+ Integer.toHexString((0xFF & passHash[i])));
			else
				hexString.append(Integer.toHexString(0xFF & passHash[i]));
		}

		return hexString.toString();
	}
}
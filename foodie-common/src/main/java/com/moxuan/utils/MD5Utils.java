package com.moxuan.utils;

import cn.hutool.core.codec.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * 对字符串进行md5加密
	 */
	public static String getMD5Str(String strValue) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return Base64.encode(md5.digest(strValue.getBytes()));
	}

}

package edu.cust.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Sha1Hash;

public class PasswordServiceTest {
	
	public static String encryptPassword(String password, byte[] salt, int it) {
		
		Sha1Hash sh = new Sha1Hash(password, salt, it);
		return Hex.encodeToString(salt) + sh.toHex();
	}
	
	public static String encryptPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(password.getBytes());
		md.update(salt);
		byte[] inputpwbyte = md.digest();
		byte[] result = new byte[inputpwbyte.length + salt.length];
		System.arraycopy(inputpwbyte, 0, result, 0, inputpwbyte.length);
		System.arraycopy(salt, 0, result, inputpwbyte.length, salt.length);
		System.out.println("----------length:" + result.length);
		System.out.println("----------result:" + Hex.encodeToString(result));
		System.out.println("----------result:" + Base64.encodeToString(result));
		//Sha1Hash sh = new Sha1Hash(password, salt, it);
		return "";
	}
	
	public static boolean validatePassword(String plainPassword, String encryptedPassword) {
		byte[] salt = Hex.decode(encryptedPassword.substring(0,16));
		for(int i = 1; i <= 1024; i++) {
			String ep = encryptPassword(plainPassword, salt, i);
			//System.out.println(ep.equals(encryptedPassword));
		}
		
		return false;
	}
	
    public static boolean verifySHA(String ldappw, String inputpw)
			throws NoSuchAlgorithmException {
		// MessageDigest 提供了消息摘要算法，如 MD5 或 SHA，的功能，这里LDAP使用的是SHA-1
		MessageDigest md = MessageDigest.getInstance("SHA");
		// 取出加密字符
		ldappw = ldappw.substring(6);
		System.out.println(ldappw);
		// 解码BASE64
		byte[] ldappwbyte = Base64.decode(ldappw);
		System.out.println(ldappwbyte.length);
		byte[] shacode;
		byte[] salt;
 
		// 前20位是SHA-1加密段，20位后是最初加密时的随机明文
		if (ldappwbyte.length <= 20) {
			shacode = ldappwbyte;
			salt = new byte[0];
		} else {
			shacode = new byte[20];
			salt = new byte[ldappwbyte.length - 20];
			System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
			System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
			System.out.println(Hex.encodeToString(shacode));
			System.out.println(Hex.encodeToString(salt));
		}
		// 把用户输入的密码添加到摘要计算信息
		md.update(inputpw.getBytes());
		// 把随机明文添加到摘要计算信息
		md.update(salt);
		// 按SSHA把当前用户密码进行计算
		byte[] inputpwbyte = md.digest();
		// 返回校验结果
		return MessageDigest.isEqual(shacode, inputpwbyte);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] salt = new byte[8];
		new SecureRandom().nextBytes(salt);
		
		try {
			encryptPassword("111", salt);
			System.out.println(verifySHA("{SSHA}uAn1wL3PjkjVIHZGaqG8ENCY80g7BbEexlYHFQ==","111"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		PasswordService ps = new PasswordService();
		System.out.println("***" + ps.encryptPassword("111"));
		//3b05b11ec6560715
		boolean flag = PasswordServiceTest.validatePassword("233219", "3b05b11ec6560715b809f5c0bdcf8e48d52076466aa1bc10d098f348");
		System.out.println(flag);
		System.out.println(ps.encryptPassword("Wonder2019"));
		//byte[] key = Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");
		byte[] key = Base64.decode("nwEHmyG0f66WQT7Qu6N6SQ==");
		System.out.println(key.length);
		try {
			//System.out.println(new String(key, "unicode"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		byte[] bytes = new byte[16];
		new SecureRandom().nextBytes(bytes);
		System.out.println(Base64.encodeToString(bytes));
		System.out.println(Base64.encodeToString(key));
	}

}

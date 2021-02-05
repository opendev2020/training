package edu.cust.login;

import java.security.SecureRandom;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.stereotype.Component;
@Component
public class PasswordService {
	
	private SecureRandom random = new SecureRandom();
	
	public byte[] generateSalt(int numBytes) {
		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}
	
	public String encryptPassword(String password, byte[] salt) {
		Sha1Hash sh = new Sha1Hash(password, salt, 1024);
		return Hex.encodeToString(salt) + sh.toHex();
	}
	
	public String encryptPassword(String password) {
		byte[] salt = generateSalt(8);
		return encryptPassword(password, salt);
	}
	
	public boolean validatePassword(String plainPassword, String encryptedPassword) {
		byte[] salt = Hex.decode(encryptedPassword.substring(0,16));
		String ep = encryptPassword(plainPassword, salt);
		return ep.equals(encryptedPassword);
	}

}

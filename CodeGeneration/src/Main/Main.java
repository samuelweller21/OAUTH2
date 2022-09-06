package Main;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String cv = generateCodeVerifier();
		System.out.println("Code Verifier: " + cv);
		
		String cc = generateCodeChallenge(cv);
		System.out.println("Code Challenge: " + cc);
		
	}
	
	public static String generateCodeVerifier() {
		SecureRandom sr = new SecureRandom();
		byte[] cv = new byte[32];
		sr.nextBytes(cv);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(cv);
	}
	
	public static String generateCodeChallenge(String cv) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] bytes = cv.getBytes("US-ASCII");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes, 0, bytes.length);
		byte[] digest = md.digest();
		
		return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
		
	}
	
	
}

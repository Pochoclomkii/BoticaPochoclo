/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author User
 */
public class SHA256 {
    public static String getSHA256(String password) {
	MessageDigest md = null;
	try {
		md = MessageDigest.getInstance("SHA-256");
	} 
	catch (NoSuchAlgorithmException e) {		
		e.printStackTrace();
		return null;
	}
	    
	byte[] hash = md.digest(password.getBytes());
	StringBuffer sb = new StringBuffer();
	    
	for(byte b : hash) {        
		sb.append(String.format("%02x", b));
	}
	    
	return sb.toString();
}
    public static void main(String[] args) {
        String s = "1111";
        System.out.println("HashCode Generated for the string is: " + getSHA256(s));
        
    }
    
}

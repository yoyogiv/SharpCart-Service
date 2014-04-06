package com.sharpcart.rest.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.sharpcart.rest.utilities.PasswordHash;

public class SharpCartPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		try {
			return PasswordHash.createHash(rawPassword.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		try {
			return PasswordHash.validatePassword(rawPassword.toString(), encodedPassword);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}

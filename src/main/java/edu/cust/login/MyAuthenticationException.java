package edu.cust.login;

import org.apache.shiro.authc.AuthenticationException;

public class MyAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyAuthenticationException(String message){
		super(message);
	}
}

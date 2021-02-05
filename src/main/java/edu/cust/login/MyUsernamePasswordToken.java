package edu.cust.login;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MyUsernamePasswordToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean correctCaptcha;

	private String roleBase;

	public MyUsernamePasswordToken(String username, String password,
                                   boolean rememberMe, String host, boolean correctCaptcha, String roleBase) {
		super(username, password, rememberMe, host);
		// TODO Auto-generated constructor stub
		this.correctCaptcha = correctCaptcha;
		this.roleBase = roleBase;
	}

	public boolean isCorrectCaptcha() {
		return correctCaptcha;
	}

	public String getRoleBase() {
		return roleBase;
	}
}

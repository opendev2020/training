package edu.cust.login;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * Created by qh on 2017/4/9.
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private String roleBase;

    public void setRoleBase(String roleBase) {
        this.roleBase = roleBase;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request,
                                              ServletResponse response) {
        // TODO Auto-generated method stub
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        /*String answer = request.getParameter("captcha");
        Captcha c = (Captcha)((HttpServletRequest)request).getSession().getAttribute(Captcha.NAME);
        return new MyUsernamePasswordToken(username, password, rememberMe,
                host, c.isCorrect(answer), roleBase);*/
        return new MyUsernamePasswordToken(username, password, rememberMe,
                host, true, roleBase);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    protected void setFailureAttribute(ServletRequest request,
                                       AuthenticationException ae) {
        // TODO Auto-generated method stub
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// TODO Auto-generated method stub
		Subject subject = getSubject(request, response);
		return (subject.isAuthenticated() && subject.getPrincipal() != null) || subject.isRemembered()
				|| (!isLoginRequest(request, response) && isPermissive(mappedValue));
	}
}

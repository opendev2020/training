package edu.cust.util;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qh on 2017/4/13.
 */
@ControllerAdvice
public class AppWideExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(HttpServletRequest req, Model model, Exception ex){
        String uri = req.getRequestURI();
        log.debug("uri:{}", uri);
        String retStr = uri.endsWith("Ajax") ? "json" : "error2";

        model.addAttribute("retCode", "err");
        if(ex instanceof AuthorizationException){
            model.addAttribute("retMsg", "非法访问，您无权限执行此操作");
            return retStr;
        }
        String msg = ex.getMessage();
        if(ex instanceof BusinessException) {
        	log.warn(msg);
        }else {
        	log.warn(msg, ex);
        }
        model.addAttribute("retMsg", msg != null && msg.indexOf("SQL") > -1 ? "未知异常" : msg);
        return retStr;
    }
}

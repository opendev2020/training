package edu.cust.login;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.cust.util.AbstractController;

@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyPasswd extends AbstractController {
	
	@Autowired
	private JdbcTemplate jt;
	@Autowired
	private PasswordService passwordService;
    
    @RequestMapping(value={"/adm/changepasswd"})
    public String changepasswd(){
        return "adm/changepasswd";
    }
    
    @RequestMapping(value={"/adm/changepasswdAjax"})
    @Transactional
    public String changepasswdAjax(String piNewPass, Model model){
    	String username = (String)SecurityUtils.getSubject().getPrincipal();
    	piNewPass = passwordService.encryptPassword(piNewPass);
        jt.update("update c_user set c_password=? where c_username=?",new Object[] {piNewPass,username});
        model.addAttribute("retMsg","修改成功！");
        return "json";
    }
}

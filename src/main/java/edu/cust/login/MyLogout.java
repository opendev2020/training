package edu.cust.login;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.cust.util.AbstractController;

@Controller
public class MyLogout extends AbstractController {
	
	@RequestMapping(value = { "/adm/logout" })
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/adm/index.jsp";
	}

}

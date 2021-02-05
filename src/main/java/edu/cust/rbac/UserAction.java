package edu.cust.rbac;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.cust.login.PasswordService;
import edu.cust.rbac.dao.UserDAO;
import edu.cust.rbac.dao.UserDAO2;
import edu.cust.rbac.domain.User;
import edu.cust.util.AbstractController;
import edu.cust.util.page.Page;
import edu.cust.util.page.PageFactory;
import edu.cust.util.search.Search;

@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserAction extends AbstractController {
	
	//@Autowired
	//private JdbcTemplate jt;
	
	@Autowired
	private UserDAO2 userDAO2;//没有密码
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PasswordService passwordService;

	@RequiresPermissions({"yhgl"})
	@RequestMapping("/adm/users/listAjax")
	public String list(int rows, int page, Search search, Model model) {
		String sql = search.buildSQL(userDAO2);
		sql += " order by c_username asc";
		Page mlpage = PageFactory.getPage();
		mlpage.setPageNum(page);
		mlpage.setRecordNum(rows);
		List<Object> params = search.getParams();
		//params.add(cp);
		List<?> result = mlpage.getOnePage(sql, params, userDAO2);
		model.addAttribute("pages", mlpage);
		model.addAttribute("result", result);
		return "json";
	}

	@Transactional
	@RequiresPermissions({"yhgl"})
	@RequestMapping("/adm/users/deleteAjax")
	public String delete(String username, Model model) {
		userDAO2.delete(username);
		model.addAttribute("retMsg", "删除成功");
		return "json";
	}

	@Transactional
	@RequiresPermissions({"yhgl"})
	@RequestMapping("/adm/users/addAjax")
	public String add(User user, Model model) {
		user.setPassword(passwordService.encryptPassword(user.getUsername()));
		userDAO.insert(user);
		model.addAttribute("retCode", "OK");
		model.addAttribute("retMsg", "添加成功");
		return "json";
	}

	@Transactional
	@RequiresPermissions({"yhgl"})
	@RequestMapping("/adm/users/updateAjax")
	public String update(Model model, User user) {
		User a = userDAO.loadOne(user.getUsername());
		a.setName(user.getName());
		userDAO.update(a);
		model.addAttribute("retCode", "OK");
		model.addAttribute("retMsg", "更新成功");
		return "json";
	}

	@ResponseBody
	@RequiresPermissions({"yhgl"})
	@RequestMapping("/adm/users/existAjax")
	public boolean existAjax(String username, Model model) {
		User adm = userDAO2.loadOne(username);
		return adm == null;
	}
}

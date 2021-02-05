package edu.cust.rbac;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.cust.rbac.dao.PermissionDAO;
import edu.cust.rbac.dao.RoleDAO;
import edu.cust.rbac.dao.RolePermissionDAO;
import edu.cust.rbac.dao.UserDAO;
import edu.cust.rbac.dao.UserRoleDAO;
import edu.cust.rbac.domain.Role;
import edu.cust.rbac.domain.RolePermission;
import edu.cust.rbac.domain.UserRole;
import edu.cust.util.AbstractController;
import edu.cust.util.ListTemplate;

@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleAction extends AbstractController {
	
	@Autowired
    private RoleDAO roleDAO;
	
	@Autowired
    private RolePermissionDAO rolePermissionDAO;
	
	@Autowired
	private PermissionDAO permissionDAO;
	
	@Autowired
	private UserRoleDAO userRoleDAO;
	
	@Autowired
	private UserDAO userDAO;

    @Autowired
    private JdbcTemplate jt;
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping(value={"/adm/roles/listRolesAjax"})
	public String listRolesAjax(Model model){
    	List<Role> roles = roleDAO.loadMore(" order by c_id asc", null);
    	model.addAttribute("result", roles);
		return "json";
	}
    
    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/updateRoleAjax")
    public String updateRole(Role role, Model model){
        roleDAO.update(role);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "修改成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/deleteRoleAjax")
    public String deleteRole(Role role, Model model){
        roleDAO.delete(new Object[] {role.getId()});
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "删除成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/addRoleAjax")
    public String addRole(Role role, Model model){
        roleDAO.insert(role);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listPermsInRole")
    public String listPermsInRole(String rid, Model model){
    	Role r = roleDAO.loadOne(new Object[] {rid});
    	model.addAttribute("role", r);
        return "adm/roles/listPermsInRole";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listUsersInRole")
    public String listUsersInRole(String rid, Model model){
    	Role r = roleDAO.loadOne(new Object[] {rid});
    	model.addAttribute("role", r);
        return "adm/roles/listUsersInRole";
    }
    
    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/addPermsToRoleAjax")
    public String addPermsToRole(String rid, String[] pids, Model model){
    	if(pids == null) {
    		model.addAttribute("retCode", "ERR");
            model.addAttribute("retMsg", "请选择权限");
            return "json";
    	}
    	for(String pid : pids) {
    		RolePermission rp = new RolePermission();
    		rp.setRoleId(rid);
    		rp.setPermId(pid);
    		log.info("insert perm:{},{}", rid, pid);
    		rolePermissionDAO.insert(rp);
    	}
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }
    
    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/delPermsFromRoleAjax")
    public String delPermsFromRole(String rid, String[] pids, Model model){
    	if(pids == null) {
    		model.addAttribute("retCode", "ERR");
            model.addAttribute("retMsg", "请选择权限");
            return "json";
    	}
    	for(String pid : pids) {
    		rolePermissionDAO.delete(new Object[] {rid, pid});
    	}
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "删除成功");
        return "json";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listPermsNotInRoleAjax")
    public String listPermsNotInRole(String rid, Model model){
    	PermsNotInRoleSearch search = new PermsNotInRoleSearch();
    	ListTemplate lt = permissionDAO.getLt();
    	String sql = search.buildSQL(lt) + " order by c_id asc";
    	List<?> perms = jt.query(sql, new Object[] {rid}, lt);
        model.addAttribute("result", perms);
        return "json";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listPermsInRoleAjax")
    public String listPermsInRoleAjax(String rid, Model model){
    	PermsInRoleSearch search = new PermsInRoleSearch();
    	ListTemplate lt = permissionDAO.getLt();
    	String sql = search.buildSQL(lt) + " order by c_id asc";
    	List<?> perms = jt.query(sql, new Object[] {rid}, lt);
        model.addAttribute("result", perms);
        return "json";
    }
    
    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/addUsersToRoleAjax")
    public String addUsersToRole(String rid, String[] usernames, Model model){
    	for(String un : usernames) {
    		UserRole ur = new UserRole();
    		ur.setRoleId(rid);
    		ur.setUsername(un);
    		userRoleDAO.insert(ur);
    	}
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }
    
    @Transactional
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/delUsersFromRoleAjax")
    public String delUsersFromRole(String rid, String[] usernames, Model model){
    	for(String un : usernames) {
    		userRoleDAO.delete(new Object[] {un, rid});
    	}
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listUsersNotInRoleAjax")
    public String listUsersNotInRole(String rid, UsersNotInRoleSearch search, Model model){
    	ListTemplate lt = userDAO.getLt();
    	String sql = search.buildSQL(lt) + " order by c_username asc";
    	List<?> users = jt.query(sql, new Object[] {rid}, lt);
        model.addAttribute("result", users);
        return "json";
    }
    
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/listUsersInRoleAjax")
    public String listUsersInRole(String rid, UsersInRoleSearch search, Model model){
    	ListTemplate lt = userDAO.getLt();
    	String sql = search.buildSQL(lt) + " order by c_username asc";
    	List<?> users = jt.query(sql, new Object[] {rid}, lt);
        model.addAttribute("result", users);
        return "json";
    }

    @ResponseBody
    @RequiresPermissions({"jsgl"})
    @RequestMapping("/adm/roles/validateRolePk")
    public boolean validateRolePk(String id, Model model){
        Role r = roleDAO.loadOne(new Object[] {id});
        return r == null;
        
    }

}

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
import edu.cust.rbac.domain.Permission;
import edu.cust.util.AbstractController;

@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PermAction extends AbstractController {
	@Autowired
	private JdbcTemplate jt;
    @Autowired
    private PermissionDAO permissionDAO;
    
    @RequiresPermissions({"qxgl"})
    @RequestMapping(value={"/adm/perms/listPermissionsAjax"})
	public String listPermsAjax(PermSearch search, Model model){
    	//以下代码使用了简单搜索方式，前端只需要传values参数即可
    	//如果需要分页，可以使用page，如果不需要分页，直接用JdbcTemplate
    	String sql = search.buildSQL(permissionDAO);
    	List<?> params = search.getParams();
    	List<?> result = jt.query(sql, params.toArray(), permissionDAO.getLt());
        model.addAttribute("result", result);
        return "json";
	}

    @Transactional
    @RequiresPermissions({"qxgl"})
    @RequestMapping("/adm/perms/updatePermissionAjax")
    public String updatePermission(Permission permission, Model model){
        permissionDAO.update(permission);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "修改成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"qxgl"})
    @RequestMapping("/adm/perms/deletePermissionAjax")
    public String deleteRole(Permission permission, Model model){
        permissionDAO.delete(new Object[] {permission.getId()});
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "删除成功");
        return "json";
    }

    @Transactional
    @RequiresPermissions({"qxgl"})
    @RequestMapping("/adm/perms/addPermissionAjax")
    public String addPermission(Permission permission, Model model){
        permissionDAO.insert(permission);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }

    @ResponseBody
    @RequiresPermissions({"qxgl"})
    @RequestMapping("/adm/perms/validatePermissionPk")
    public boolean validatePermissionPk(String id, Model model){
    	Permission p = permissionDAO.loadOne(new Object[] {id});
    	return p == null;
    }

}

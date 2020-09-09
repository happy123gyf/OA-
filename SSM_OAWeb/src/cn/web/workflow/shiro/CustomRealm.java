package cn.web.workflow.shiro;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.EmployeeService;
import cn.web.workflow.service.SysPermissionService;
import cn.web.workflow.service.TreeMenuService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TreeMenuService treeMenuService;
    @Autowired
    private SysPermissionService sysPermissionService;

    // 权限认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
       //获得认证主体
		ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();

		List<SysPermission> sysPermissionList = null;
		List<String> permissions = new ArrayList<>();
		try {
			//获得该用户的权限列表
			sysPermissionList = sysPermissionService.findPermissionListByEmpname(activeUser.getUsername());
		} catch (Exception e) {

			e.printStackTrace();
		}
		//将权限下的权限操作保存到permissions集合里
		for (SysPermission sysPermission : sysPermissionList) {
			permissions.add(sysPermission.getPercode());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissions);
		return info;

    }

    // 身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        /*System.out.println("进来cn.web.workflow.shiro.CustomRealm吗?");*/
        String username_db = null;
        String password_db = null;
        String salt = null;
        Employee employee = null;
        List<TreeMenu> menuList = null;
        // 用户输入的用户名
        String username = (String) token.getPrincipal();
        try {
            // 数据库查询出来的用户对象
            employee = employeeService.findUserByUsername(username);
            username_db = employee.getName();
            // 获得用户密码
            password_db = employee.getPassword();

            // 获取盐
            salt = employee.getSalt();
            // 获取用户菜单
            menuList = treeMenuService.findMenuList();
        } catch (Exception e) {

            e.printStackTrace();
        }

        // 创建一个ActiveUser对象,用于存储用户信息,和权限信息,方便调用
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid(employee.getId());
        activeUser.setUsername(employee.getName());
        activeUser.setMenus(menuList);
        activeUser.setEmployee(employee);
        // 从数据库中的唯一账号名与密码
        if (!username.equals(username_db)) {
            return null; // 返回null,会报UnknownAccountException
        }
        // 密码认证不过关返回IncorrectCredentialsException
        AuthenticationInfo info = new SimpleAuthenticationInfo(activeUser, password_db, ByteSource.Util.bytes(salt),
                "CustomRealm");
        return info;

    }


}

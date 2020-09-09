package cn.web.workflow.controller;

import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.service.EmployeeService;
import cn.web.workflow.service.SysPermissionService;
import cn.web.workflow.service.SysRoleService;
import cn.web.workflow.shiro.exception.CustomException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysPermissionService sysPermissionService;


    @RequestMapping("/login")
    public String login(HttpServletRequest request) throws Exception {
        String ex = (String) request.getAttribute("shiroLoginFailure");
        if (ex != null) {
            if (UnknownAccountException.class.getName().equals(ex)) {
                throw new CustomException("帐号错误");
            } else if (IncorrectCredentialsException.class.getName().equals(ex)) {
                throw new CustomException("密码错误");
            } else if (ex.equals("1")) {
                throw new CustomException("验证码不正确");
            } else {
                throw new Exception();
            }
        }
        return "login";

    }


    //用户管理 findUserList
    @RequestMapping("/findUserList")
    public ModelAndView findUserList(ModelAndView mv) {
        List<EmployeeCustom> userList = employeeService.findUserAndRoleList();
        List<SysRole> allRoles = sysRoleService.findSysrroleList();
        mv.addObject("userList", userList);
        mv.addObject("allRoles", allRoles);
        mv.setViewName("userlist");
        return mv;
    }

    @RequestMapping("/viewPermissionByEmpname")
    @ResponseBody
    public SysRole viewPermissionByEmpname(@RequestBody String empname) {
        String[] str = empname.split("=");
        String name = str[1];
        SysRole sysRole = sysPermissionService.findRoleAndPermissionListByEmpname(name);
        return sysRole;
    }


    //修改用户的角色
    @RequestMapping("/assignRole")
    @ResponseBody
    public Map<String, String> assignRole(String roleId, String userName) {
        System.out.println(roleId);
        System.out.println(userName);
        Map<String, String> map = new HashMap<>();
        int count = sysRoleService.updateRoleSys_user_role(roleId, userName);
        System.out.println(count);
        if (count==1){
            map.put("msg", "修改成功!");
        }else{
            map.put("msg", "修改失败!");
        }
        return map;

    }


}



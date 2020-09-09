package cn.web.workflow.junit;

import cn.web.workflow.mapper.EmployeeCustomMapper;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.service.SysPermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml","classpath:spring/applicationContext-shiro.xml"})
public class TestfindRoleAndPermissionListByEmpname {
    @Autowired
    private EmployeeCustomMapper employeeCustomMapper;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Test
    public void test(){
        SysRole sysRole = sysPermissionService.findRoleAndPermissionListByEmpname("zhang");

            System.out.println(sysRole.getId()+sysRole.getName());
            for (SysPermission sysPermission : sysRole.getPermissionList()) {
                System.out.println(sysPermission.getId()+sysPermission.getName()+sysPermission.getUrl()+sysPermission.getPercode());
            }
        }


    }



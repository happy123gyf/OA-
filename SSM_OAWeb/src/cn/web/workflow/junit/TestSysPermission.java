package cn.web.workflow.junit;

import cn.web.workflow.mapper.SysPermissionMapperCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.service.SysPermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml","classpath:spring/applicationContext-shiro.xml"})
public class TestSysPermission {

    @Autowired
    private SysPermissionMapperCustom sysPermissionMapperCustom;
    @Autowired
    private SysPermissionService sysPermissionService;


    @Test
    public void testsysPermission()  {
        List<SysPermission> permissionList = sysPermissionMapperCustom.findPermissionListByEmpname("zhang");
        for (SysPermission sysPermission : permissionList) {
            System.out.println("\t" + "权限名:"+sysPermission.getName() + ",url地址:"+sysPermission.getUrl()+",percode名:"+sysPermission.getPercode());
        }

    }


}
package cn.web.workflow.junit;


import cn.web.workflow.mapper.SysUserRoleMapper;
import cn.web.workflow.pojo.SysUserRole;
import cn.web.workflow.pojo.SysUserRoleExample;
import cn.web.workflow.service.SysRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/springmvc.xml", "classpath:spring/applicationContext-shiro.xml"})
public class TestupdateRoleSys_user_role {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void test() {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysUserId("chen");
        sysUserRole.setSysRoleId("3");
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = sysUserRoleExample.createCriteria();
        criteria.andSysUserIdEqualTo("chen");
        int count = sysUserRoleMapper.updateByExampleSelective(sysUserRole,sysUserRoleExample);
        System.out.println(count);


    }


}

package cn.web.workflow.junit;

import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml"})
public class TestTreeMenu {

    @Autowired
    private SysPermissionCustomMapper sysPermissionCustomMapper;

    @Test
    public void testMenu() {

        List<TreeMenu> treeMenus = sysPermissionCustomMapper.findMenuList();

        for (TreeMenu menu : treeMenus) {
            System.out.println(menu.getId()+"."+menu.getName());

            List<SysPermission> children = menu.getChildren();
            for (SysPermission sysPermission : children) {
                System.out.println("\t" + sysPermission.getName() + ","+sysPermission.getUrl()+","+sysPermission.getPercode());
            }
        }
    }
}
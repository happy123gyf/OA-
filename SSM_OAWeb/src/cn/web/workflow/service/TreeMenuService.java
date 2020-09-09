package cn.web.workflow.service;

import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;

import java.util.List;

public interface TreeMenuService {
    public List<TreeMenu> findMenuList();

    public List<SysPermission> getSubMenu(Integer id);

}

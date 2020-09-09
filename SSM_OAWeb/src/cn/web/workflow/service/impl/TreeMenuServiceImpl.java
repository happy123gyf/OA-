package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.SysPermissionCustomMapper;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.TreeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeMenuServiceImpl implements TreeMenuService {
    @Autowired
    private SysPermissionCustomMapper sysPermissionCustomMapper;

    @Override
    public List<TreeMenu> findMenuList() {
        return sysPermissionCustomMapper.findMenuList();
    }

    @Override
    public List<SysPermission> getSubMenu(Integer id) {
        return sysPermissionCustomMapper.getSubMenu(id);
    }
}

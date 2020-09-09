package cn.web.workflow.mapper;

import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.TreeMenu;

import java.util.List;

public interface SysPermissionCustomMapper {

     List<TreeMenu>  findMenuList();

     List<SysPermission> getSubMenu(Integer id);


}
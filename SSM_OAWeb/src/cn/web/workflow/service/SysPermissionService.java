package cn.web.workflow.service;

import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;

import java.util.List;

public interface SysPermissionService {


    public List<SysPermission> findPermissionListByEmpname(String empname) ;

    SysRole findRoleAndPermissionListByEmpname(String empname);

}

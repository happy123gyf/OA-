package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.EmployeeCustomMapper;
import cn.web.workflow.mapper.SysPermissionMapperCustom;
import cn.web.workflow.pojo.SysPermission;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private SysPermissionMapperCustom sysPermissionMapperCustom;
    @Autowired
    private EmployeeCustomMapper employeeCustomMapper;


    @Override
    public List<SysPermission> findPermissionListByEmpname(String empname) {
        List<SysPermission> permissionList = sysPermissionMapperCustom.findPermissionListByEmpname(empname);
        if (permissionList != null && permissionList.size() > 0) {
            return permissionList;
        }

        return null;
    }

    @Override
    public SysRole findRoleAndPermissionListByEmpname(String empname) {
        SysRole sysRole = employeeCustomMapper.findRoleAndPermissionListByEmpname(empname);
        if (sysRole != null ) {
            return sysRole;
        }


        return null;
    }


}

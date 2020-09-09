package cn.web.workflow.mapper;

import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.SysRole;

import java.util.List;

public interface EmployeeCustomMapper {

   List<EmployeeCustom>  findUserAndRoleList();

   SysRole findRoleAndPermissionListByEmpname(String empname);

}
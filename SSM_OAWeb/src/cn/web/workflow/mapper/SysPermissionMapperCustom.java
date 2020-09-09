package cn.web.workflow.mapper;

import cn.web.workflow.pojo.SysPermission;

import java.util.List;


public interface SysPermissionMapperCustom {
	

	//根据用户id查询权限url
	List<SysPermission> findPermissionListByEmpname(String empname);

}

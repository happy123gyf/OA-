package cn.web.workflow.service;

import cn.web.workflow.pojo.SysRole;

import java.util.List;

public interface SysRoleService {

    List<SysRole> findSysrroleList();

    int updateRoleSys_user_role(String rid, String uname);
}

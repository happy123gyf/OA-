package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.SysRoleMapper;
import cn.web.workflow.mapper.SysUserRoleMapper;
import cn.web.workflow.pojo.SysRole;
import cn.web.workflow.pojo.SysUserRole;
import cn.web.workflow.pojo.SysUserRoleExample;
import cn.web.workflow.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public List<SysRole> findSysrroleList() {
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(null);
        if (sysRoleList != null && sysRoleList.size() > 0) {
            return sysRoleList;
        }

        return null;

    }

    @Override
    public int updateRoleSys_user_role(String rid, String uname) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysUserId(uname);
        sysUserRole.setSysRoleId(rid);
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = sysUserRoleExample.createCriteria();
        criteria.andSysUserIdEqualTo(uname);
        int count = sysUserRoleMapper.updateByExampleSelective(sysUserRole, sysUserRoleExample);
        return count;

    }
}

package cn.web.workflow.pojo;

import java.util.List;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 *
 * @author Thinkpad
 */
public class ActiveUser implements java.io.Serializable {
    private long userid;//用户id（主键）

    private String username;// 用户名称
    private Employee employee;


    private List<TreeMenu> menus;// 菜单
    private List<SysPermission> permissions;// 权限

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<TreeMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<TreeMenu> menus) {
        this.menus = menus;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }
}

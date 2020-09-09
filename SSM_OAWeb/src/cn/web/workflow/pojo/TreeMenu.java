package cn.web.workflow.pojo;

import java.util.List;

public class TreeMenu {

    private int id;
    private String name;   //一级菜单的名称

    private List<SysPermission> children; //二级菜单的信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SysPermission> getChildren() {
        return children;
    }

    public void setChildren(List<SysPermission> children) {
        this.children = children;
    }
}
package cn.web.workflow.controller;

import cn.web.workflow.pojo.ActiveUser;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.TreeMenu;
import cn.web.workflow.service.TreeMenuService;
import cn.web.workflow.utils.Constants;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private TreeMenuService treeMenuService;

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView mv, HttpSession session){
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Employee employee = activeUser.getEmployee();
        session.setAttribute(Constants.GLOBAL_SESSION_ID,employee);
        List<TreeMenu> menuList = activeUser.getMenus();
        mv.addObject("menuList", menuList);
        mv.setViewName("index");
        return mv;
    }




}

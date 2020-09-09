package cn.web.workflow.listener;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.EmployeeService;
import cn.web.workflow.utils.Constants;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class ManagerTaskHandler implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        // 硬编码获取spring容器
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        EmployeeService employeeService = (EmployeeService) context.getBean("employeeServiceImpl");

        // servlet API
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpSession session = servletRequestAttributes.getRequest().getSession();
        Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);

        Employee manager = employeeService.findEmployeeByManagerId(employee.getManagerId());
        task.setAssignee(manager.getName());
    }
}

package cn.web.workflow.service;

import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;

import java.util.List;

public interface EmployeeService {

	Employee findUserByUsername(String username);
	
	
	Employee findEmployeeByManagerId(Long manager_id);

	List<EmployeeCustom> findUserAndRoleList();
}


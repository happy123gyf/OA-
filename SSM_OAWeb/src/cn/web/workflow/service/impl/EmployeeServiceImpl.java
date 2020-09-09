package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.EmployeeCustomMapper;
import cn.web.workflow.mapper.EmployeeMapper;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.pojo.EmployeeCustom;
import cn.web.workflow.pojo.EmployeeExample;
import cn.web.workflow.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private EmployeeCustomMapper employeeCustomMapper;

	@Override
	public Employee findUserByUsername(String username) {
		EmployeeExample employeeExample = new EmployeeExample();
		EmployeeExample.Criteria criteria = employeeExample.createCriteria();
		criteria.andNameEqualTo(username);
		List<Employee> employees = employeeMapper.selectByExample(employeeExample);
		if (employees!=null && employees.size()>0) {
			return employees.get(0);
		}

		return null;
	}
	
	

	@Override
	public Employee findEmployeeByManagerId(Long manager_id) {
		
		return employeeMapper.selectByPrimaryKey(manager_id);
	}

	@Override
	public List<EmployeeCustom> findUserAndRoleList() {
		List<EmployeeCustom> employeeCustomList = employeeCustomMapper.findUserAndRoleList();
		if (employeeCustomList!=null&&employeeCustomList.size()>0){
			return  employeeCustomList;
		}
		return null;
	}

	
}

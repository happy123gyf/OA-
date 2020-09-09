package cn.web.workflow.controller;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.WorkFlowService;
import cn.web.workflow.utils.Constants;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
public class WorkFlowController {
	@Autowired
	private WorkFlowService workFlowService;

	// 部署流程
	@RequestMapping("/deployProcess")
	public String deployProcess(String processName, MultipartFile processFile) {

		workFlowService.deploy(processName, processFile);

		return "redirect:/processDefinitionList";

	}

	// 查看部署流程,流程实例
	@RequestMapping("/processDefinitionList")
	public ModelAndView processDefinitionList(ModelAndView mv) {

		List<ProcessDefinition> pdList = workFlowService.findprocessDefinitionList();
		List<Deployment> depList = workFlowService.findDeploymentList();
		mv.addObject("pdList", pdList);
		mv.addObject("depList", depList);
		mv.setViewName("workflow_list");

		return mv;

	}

	//删除部署 delDeployment
	@RequestMapping("/delDeployment")
	public String delDeployment(String deploymentId){
		workFlowService.delDeploymentBydeploymentId(deploymentId);
		return "redirect:/processDefinitionList";
	}

	//查看流程定义图 viewImage
	@RequestMapping("/viewImage")
	public String viewImage(String deploymentId, String imageName, HttpServletResponse response) throws IOException {
		InputStream in = workFlowService.findImageInputStream(deploymentId, imageName);

		OutputStream out = response.getOutputStream();

		// 4：将输入流中的数据读取出来，写到输出流中
		for (int b = -1; (b = in.read()) != -1;) {
			out.write(b);
		}
		out.close();
		in.close();

		return null;
	}

	//报销申请  保存请假单 ,并开始流程 saveStartBaoxiao
	@RequestMapping("/saveStartBaoxiao")
	public String saveStartBaoxiao(Baoxiaobill baoxiaobill, HttpSession session) {
		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		workFlowService.saveStartBaoxiao(baoxiaobill, employee);

		return "redirect:/myTaskList";
	}


	//查看当前任务
	@RequestMapping("/myTaskList")
	public ModelAndView myTaskList(ModelAndView mv, HttpSession session) {
		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);

		List<Task> taskList = workFlowService.findTaskListByUserId(employee.getName());

		mv.addObject("taskList", taskList);
		mv.setViewName("workflow_task");
		return mv;
	}

	//viewCurrentImage 查看当前任务进度
// 查看当前流程图片

	@RequestMapping("/viewCurrentImage")
	public ModelAndView viewCurrentImage(String taskId, ModelAndView mv) {
		//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		System.out.println("有进来这里吗!!!!!!!!!!");
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
		mv.addObject("deploymentId", pd.getDeploymentId());
		mv.addObject("imageName", pd.getDiagramResourceName());

 //二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中
			Map<String, Object> map = workFlowService.findCoordingByTask(taskId);

			mv.addObject("acs", map);
			mv.setViewName("viewimage");
		return mv;
	}


	//办理任务 viewTaskForm
	@RequestMapping("/viewTaskForm")
	public ModelAndView viewTaskForm(ModelAndView mv, String taskId) {
		//报销实体
		Baoxiaobill bill = workFlowService.findBaoxiaoBillByTaskId(taskId);
		//批注集合
		List<Comment> commentList = workFlowService.findCommentListByTaskId(taskId);
		//流程线名称集合
		List<String> outcomeList = workFlowService.findOutComeListByTaskId(taskId);

		mv.addObject("bill", bill);
		mv.addObject("commentList", commentList);
		mv.addObject("outcomeList", outcomeList);
		mv.addObject("taskId", taskId);
		mv.setViewName("approve_baoxiao");
		return mv;
	}


	//办理任务 submitTask
	@RequestMapping("/submitTask")
	public String submitTask(int id, String taskId, String comment, HttpSession session,String outcome) {
		System.out.println("请假id-->" + id);
		System.out.println("任务id-->" + taskId);
		System.out.println("批注-->"+comment);
		System.out.println(outcome);
		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		workFlowService.saveSubmitTask(id, taskId, comment, employee.getName(),outcome);
		return "redirect:/myTaskList";
	}









}

package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.BaoxiaobillMapper;
import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.WorkFlowService;
import cn.web.workflow.utils.Constants;
import org.activiti.engine.*;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private BaoxiaobillMapper baoxiaobillMapper;


    // 流程发布
    @Override
    public void deploy(String processName, MultipartFile processFile) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        try {
            inputStream = processFile.getInputStream();
            zipInputStream = new ZipInputStream(inputStream);
        } catch (IOException e) {

            e.printStackTrace();
        }

        Deployment deployment = repositoryService.createDeployment().name(processName).addZipInputStream(zipInputStream)
                .deploy();

    }

    // 流程定义集合
    @Override
    public List<ProcessDefinition> findprocessDefinitionList() {
        return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();

    }

    // 查流程部署集合
    @Override
    public List<Deployment> findDeploymentList() {

        return repositoryService.createDeploymentQuery().list();
    }

    @Override
    public void delDeploymentBydeploymentId(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public InputStream findImageInputStream(String deploymentId, String imageName) {
        InputStream is = repositoryService.getResourceAsStream(deploymentId, imageName);
        return is;
    }

    @Override
    public void saveStartBaoxiao(Baoxiaobill baoxiaobill, Employee employee) {
        baoxiaobill.setUserId(employee.getId().intValue());
        baoxiaobill.setCreatdate(new Date());
        baoxiaobill.setState(1);
        // 还有一个leavebillId 会在 LeavebillMapper.xml 里面通过sql语句生成
        baoxiaobillMapper.insert(baoxiaobill); // mybatis把主键回填到pojo对象中
        // 自己起的与业务相关的名字
        String key = Constants.BaoxiaoBILL_KEY;
        // businessKey由 自己起的与业务相关的名字 .业务的id组成
        String businessKey = key + "." + baoxiaobill.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("inputUser", employee.getName());
        runtimeService.startProcessInstanceByKey(key, businessKey, map);
    }

    @Override
    public List<Task> findTaskListByUserId(String name) {
        return taskService.createTaskQuery().taskAssignee(name).list();

    }

    //根据任务id找到流程定义
    @Override
    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
        //使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            //获取流程定义ID
            String processDefinitionId = task.getProcessDefinitionId();
            //查询流程定义的对象
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef
                    .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                    .singleResult();
            return pd;
        }
        return null;


    }

    @Override
    public Map<String, Object> findCoordingByTask(String taskId) {
        //存放坐标
        Map<String, Object> map = new HashMap<String, Object>();
        //使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程定义的ID
        String processDefinitionId = task.getProcessDefinitionId();
        //获取流程定义的实体对象（对应.bpmn文件中的数据）
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //获取当前活动的ID
        String activityId = pi.getActivityId();
        //获取当前活动对象
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
        //获取坐标
        map.put("x", activityImpl.getX());
        map.put("y", activityImpl.getY());
        map.put("width", activityImpl.getWidth());
        map.put("height", activityImpl.getHeight());
        return map;
    }

    @Override
    public Baoxiaobill findBaoxiaoBillByTaskId(String taskId) {
        // 1.根据任务id找到任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 2.根据任务找到流程对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();

        // 3.根据流程对象找到businesskey
        String businesskey = processInstance.getBusinessKey();
        System.out.println(businesskey);
        // 请假单的id
        // . 是特殊字符 要用\\转义
        String baoxiaobillId = businesskey.split("\\.")[1];
        System.out.println(baoxiaobillId);
        // 4.根据businesskey中的 .id 找到 请假单
        Baoxiaobill baoxiaobill = baoxiaobillMapper.selectByPrimaryKey(Integer.parseInt(baoxiaobillId));
        return baoxiaobill;
    }

    @Override
    public List<Comment> findCommentListByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        return comments;
    }


    // 完成任务
    @Override
    public void saveSubmitTask(int billId, String taskId, String comment, String name, String outcome) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        // 指定注解的发起人
        Authentication.setAuthenticatedUserId(task.getAssignee());
        // 1.保存批注
        taskService.addComment(taskId, processInstanceId, comment);
        Map<String, Object> variables = new HashMap<String,Object>();
        if(outcome!=null && !outcome.equals("默认提交")){
            variables.put("message", outcome);
            //3：使用任务ID，完成当前人的个人任务，同时流程变量
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
        // 3.更新请假单的状态
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) { // 流程结束,更新请假单的状态
            Baoxiaobill baoxiaobill = baoxiaobillMapper.selectByPrimaryKey(billId);
            baoxiaobill.setState(2);
            baoxiaobillMapper.updateByPrimaryKey(baoxiaobill);
        }

    }

    //动态查看连线
    @Override
    public List<String> findOutComeListByTaskId(String taskId) {
        //返回存放连线的名称集合
        List<String> list = new ArrayList<String>();
        //1:使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2：获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        //3：查询ProcessDefinitionEntiy对象
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //获取当前活动的id
        String activityId = pi.getActivityId();
        //4：获取当前的活动
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
        //5：获取当前活动完成之后连线的名称
        List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
        if (pvmList != null && pvmList.size() > 0) {
            for (PvmTransition pvm : pvmList) {
                String name = (String) pvm.getProperty("name");

                if (StringUtils.isNotBlank(name)) {
                    list.add(name);
                } else {
                    list.add("默认提交");
                }
            }
        }
        return list;
    }

    @Override
    public String findtaskIdBybillId(int billId) {
        String bussiness_key = Constants.BaoxiaoBILL_KEY + "." + billId;

        return null;
    }

    @Override
    public Baoxiaobill findBaoxiaoBillByBillId(int billId) {

        return baoxiaobillMapper.selectByPrimaryKey(billId);
    }

    @Override
    public Task findTaskByBussinessKey(String bUSSINESS_KEY) {
        Task task = this.taskService.createTaskQuery().processInstanceBusinessKey(bUSSINESS_KEY).singleResult();
        return task;
    }


}

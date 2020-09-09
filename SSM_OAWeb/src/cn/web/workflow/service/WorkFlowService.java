package cn.web.workflow.service;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface WorkFlowService {

    void deploy(String processName, MultipartFile processFile);


    List<ProcessDefinition> findprocessDefinitionList();

    List<Deployment> findDeploymentList();

    void delDeploymentBydeploymentId(String deploymentId);

    InputStream findImageInputStream(String deploymentId, String imageName);

    void saveStartBaoxiao(Baoxiaobill baoxiaobill, Employee employee);

    List<Task> findTaskListByUserId(String name);

    ProcessDefinition findProcessDefinitionByTaskId(String taskId);

    Map<String, Object> findCoordingByTask(String taskId);


    Baoxiaobill findBaoxiaoBillByTaskId(String taskId);

    List<Comment> findCommentListByTaskId(String taskId);

    void saveSubmitTask(int id, String taskId, String comment, String outcome,String name);

    //连线信息业务方法
    List<String> findOutComeListByTaskId(String taskId);

    String findtaskIdBybillId(int billId);

    Baoxiaobill findBaoxiaoBillByBillId(int billId);

    Task findTaskByBussinessKey(String bussiness_key);
}

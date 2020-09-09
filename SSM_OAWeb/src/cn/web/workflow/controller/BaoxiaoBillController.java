package cn.web.workflow.controller;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.Employee;
import cn.web.workflow.service.BaoxiaoBillService;
import cn.web.workflow.service.WorkFlowService;
import cn.web.workflow.utils.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class BaoxiaoBillController {
    @Autowired
    private BaoxiaoBillService baoxiaoBillService;
    @Autowired
    private WorkFlowService workFlowService;

    //我的报销单
    @RequestMapping("/myBaoxiaoBill")
    public ModelAndView myBaoxiaoBill(ModelAndView mv, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum) {
        Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
        Long empid = employee.getId();

        PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
        List<Baoxiaobill> baoxiaobillList = baoxiaoBillService.findBaoxiaoBillByEmpId(empid);
        PageInfo pageInfo = new PageInfo<>(baoxiaobillList);
        mv.addObject("pageInfo", pageInfo);
        mv.addObject("baoxiaobillList", baoxiaobillList);
        mv.setViewName("MybaoxiaoBill");
        return mv;
    }


    //删除报销表 deleteBillByBillId?billId
    @RequestMapping("/deleteBillByBillId")
    public String deleteBillByBillId(int billId) {

        baoxiaoBillService.deleteBillByBillId(billId);
        return "redirect:/myBaoxiaoBill";
    }

    //查看审核记录 viewShenheRecord?billId=${baoxiaobill.id}
    @RequestMapping("/viewShenheRecord")
    public ModelAndView viewShenheRecord(int billId, ModelAndView mv) {
        //报销实体
        Baoxiaobill bill = workFlowService.findBaoxiaoBillByBillId(billId);
        List<Comment> commentList = baoxiaoBillService.findCommentByBaoxiaoBillId(billId);

        //批注集合
        /*   List<Comment> commentList = workFlowService.findCommentListByTaskId(taskId);
         */
        mv.addObject("bill",bill);
        mv.addObject("commentList",commentList);
        mv.setViewName("shenherecord");
        return mv;
    }


    //通过billId查看当前流程图
    @RequestMapping("/viewCurrentImageByBill")
    public String viewCurrentImageByBill(long billId, ModelMap model) {
        String BUSSINESS_KEY = Constants.BaoxiaoBILL_KEY+ "." + billId;
        Task task = this.workFlowService.findTaskByBussinessKey(BUSSINESS_KEY);
        /**一：查看流程图*/
        //1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
        ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());

        model.addAttribute("deploymentId", pd.getDeploymentId());
        model.addAttribute("imageName", pd.getDiagramResourceName());
        /**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
        Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());

        model.addAttribute("acs", map);
        return "viewimage";
    }

}

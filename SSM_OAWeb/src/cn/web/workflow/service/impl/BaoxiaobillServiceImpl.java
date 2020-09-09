package cn.web.workflow.service.impl;

import cn.web.workflow.mapper.BaoxiaobillMapper;
import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.BaoxiaobillExample;
import cn.web.workflow.service.BaoxiaoBillService;
import cn.web.workflow.utils.Constants;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaoxiaobillServiceImpl implements BaoxiaoBillService {
    @Autowired
    private BaoxiaobillMapper baoxiaobillMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;


    //删除报销表
    @Override
    public void deleteBillByBillId(int billId) {
        baoxiaobillMapper.deleteByPrimaryKey(billId);
    }


    @Override
    public List<Baoxiaobill> findBaoxiaoBillByEmpId(Long empid) {
        BaoxiaobillExample baoxiaobillExample = new BaoxiaobillExample();
        BaoxiaobillExample.Criteria criteria = baoxiaobillExample.createCriteria();

        criteria.andUserIdEqualTo(empid.intValue());
        List<Baoxiaobill> baoxiaobillList = baoxiaobillMapper.selectByExample(baoxiaobillExample);
        return baoxiaobillList;
    }


    //根据报销单ID查询历史批注
    @Override
    public List<Comment> findCommentByBaoxiaoBillId(int id) {
        String bussiness_key = Constants.BaoxiaoBILL_KEY + "." + id;
        HistoricProcessInstance pi = this.historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(bussiness_key).singleResult();
        if (pi != null) {
            List<Comment> commentList = this.taskService.getProcessInstanceComments(pi.getId());
            if (commentList != null && commentList.size() > 0) {
                return commentList;
            }
        }

        return null;

    }


}

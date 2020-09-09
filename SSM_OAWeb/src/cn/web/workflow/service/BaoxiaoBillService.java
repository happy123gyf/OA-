package cn.web.workflow.service;

import cn.web.workflow.pojo.Baoxiaobill;
import org.activiti.engine.task.Comment;

import java.util.List;

public interface BaoxiaoBillService {


    void deleteBillByBillId(int billId);


    List<Baoxiaobill> findBaoxiaoBillByEmpId(Long empid);

    List<Comment> findCommentByBaoxiaoBillId(int billId);
}

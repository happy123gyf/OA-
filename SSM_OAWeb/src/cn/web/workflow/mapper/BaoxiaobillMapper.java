package cn.web.workflow.mapper;

import cn.web.workflow.pojo.Baoxiaobill;
import cn.web.workflow.pojo.BaoxiaobillExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaoxiaobillMapper {
    int countByExample(BaoxiaobillExample example);

    int deleteByExample(BaoxiaobillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Baoxiaobill record);

    int insertSelective(Baoxiaobill record);

    List<Baoxiaobill> selectByExample(BaoxiaobillExample example);

    Baoxiaobill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Baoxiaobill record, @Param("example") BaoxiaobillExample example);

    int updateByExample(@Param("record") Baoxiaobill record, @Param("example") BaoxiaobillExample example);

    int updateByPrimaryKeySelective(Baoxiaobill record);

    int updateByPrimaryKey(Baoxiaobill record);
}
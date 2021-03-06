package com.ncu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ncu.pojo.Orderitem;
import com.ncu.pojo.OrderitemExample;
import com.ncu.pojo.vo.OrderitemVO;

public interface OrderitemMapper {
    int countByExample(OrderitemExample example);

    int deleteByExample(OrderitemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Orderitem record);

    int insertSelective(Orderitem record);

    List<OrderitemVO> selectByExample(OrderitemExample example);

    Orderitem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Orderitem record, @Param("example") OrderitemExample example);

    int updateByExample(@Param("record") Orderitem record, @Param("example") OrderitemExample example);

    int updateByPrimaryKeySelective(Orderitem record);

    int updateByPrimaryKey(Orderitem record);
}
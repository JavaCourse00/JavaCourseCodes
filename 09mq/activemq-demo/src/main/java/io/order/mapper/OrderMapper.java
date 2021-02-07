package io.order.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import io.order.model.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-07
 */
@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByStatusId(@Param("statusId")Integer statusId);
}
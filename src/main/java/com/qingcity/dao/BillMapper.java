package com.qingcity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.qingcity.entity.Bill;

/**
 * 
 * @author leehotin
 * @Date 2017年4月9日 下午4:13:55
 * @Description 订单Mapper接口
 */
@Repository
public interface BillMapper {
	/**
	 * 根据订单id 查询 订单信息
	 * 
	 * @param orderid
	 * @return 订单信息
	 */
	public Bill selectByOrderId(String orderId);

	/**
	 * 根据订单号删除订单信息
	 * 
	 * @param orderid
	 * @return 成功返回1 否则返回null
	 */
	public Integer deleteByOrderId(String orderId);

	/**
	 * 插入一个订单记录，
	 * 
	 * @param record
	 * @return
	 */
	public Integer insertBill(Bill record);

	/**
	 * 根据订单id 更新订单信息
	 * 
	 * @param record
	 * @return
	 */
	public Integer updateByOrderId(Bill record);

	/**
	 * 查询某个订单的状态
	 * 
	 * @param orderId
	 * @return 订单状态
	 */
	public Integer selectStatusById(String orderId);

	/**
	 * 根据订单号查询订单中的商品id
	 * 
	 * @param orderId
	 * @return
	 */
	public Integer selectPidByOrderId(String orderId);

	/**
	 * 根据玩家id 查询当前玩家的所有订单信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Bill> selectBillByUserId(int userId);
	
	public Integer updateStatusByOrderId(@Param("status")int status,@Param("orderId") String orderId);

}
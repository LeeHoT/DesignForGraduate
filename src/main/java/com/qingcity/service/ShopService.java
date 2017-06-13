package com.qingcity.service;

import org.springframework.stereotype.Service;

import com.qingcity.entity.PlayerEntity;

@Service
public interface ShopService {

	/**
	 * 
	 * 购买商品， 判断商品类型 做不同的事情
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public String buy(int userId, int type, int itemId);
	
	public String gainProduct(PlayerEntity player, String detail);

}

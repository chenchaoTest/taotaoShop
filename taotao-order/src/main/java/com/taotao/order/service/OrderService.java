package com.taotao.order.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.TbOrder;
import com.taotao.order.pojo.TbOrderItem;
import com.taotao.order.pojo.TbOrderShipping;

public interface OrderService {
	TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping);
}

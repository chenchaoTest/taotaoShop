package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.mapper.TbOrderItemMapper;
import com.taotao.order.mapper.TbOrderMapper;
import com.taotao.order.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.TbOrder;
import com.taotao.order.pojo.TbOrderItem;
import com.taotao.order.pojo.TbOrderShipping;
import com.taotao.order.service.OrderService;
/**
 * 订单管理Service
 * @author chenchao
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisClient jedisClinet;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMaper;
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;
	
	@Override
	public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList,
			TbOrderShipping orderShipping) {
		//向订单表中插入数据
		//获取订单号
		String str = jedisClinet.get("ORDER_GEN_KEY");
		if(StringUtils.isBlank(str)){
			jedisClinet.set("ORDER_GEN_KEY", ORDER_INIT_ID);
		}
		long orderId = jedisClinet.incr(ORDER_GEN_KEY);
		//补全pojo属性
		order.setOrderId(orderId + "");
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		order.setStatus(1);
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		//0：未评价 1：已评价
		order.setBuyerRate(0);
		//向订单表插入数据
		orderMapper.insert(order);
		//插入订单明细
		for (TbOrderItem tbOrderItem : itemList) {
			//补全订单明细
			//取订单明细id
			long orderDetailId = jedisClinet.incr(ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(orderDetailId + "");
			tbOrderItem.setOrderId(orderId + "");
			//向订单明细插入记录
			orderItemMapper.insert(tbOrderItem);
		}
		//插入物流表
		//补全物流表信息
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMaper.insert(orderShipping);
		return TaotaoResult.ok(orderId);
	}

}

package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.mapper.TbItemDescMapper;
import com.taotao.rest.mapper.TbItemMapper;
import com.taotao.rest.mapper.TbItemParamItemMapper;
import com.taotao.rest.pojo.TbItem;
import com.taotao.rest.pojo.TbItemDesc;
import com.taotao.rest.pojo.TbItemParamItem;
import com.taotao.rest.pojo.TbItemParamItemExample;
import com.taotao.rest.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.service.ItemService;
/**
 * 商品信息管理service
 * @author chao
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamMapper;
	@Autowired
	private JedisClient jedisClinet;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {
		try {
			//添加缓存逻辑
			//从缓存中获取商品信息，商品id对应的信息
			String json = jedisClinet.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
			if(!StringUtils.isBlank(json)){
				System.out.println("从缓存中读取数据...");
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return TaotaoResult.ok(item);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//根据商品id查询商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//使用TaoTaoResult包装一下
		
		try {
			//把商品信息写入缓存
			jedisClinet.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(tbItem));
			//设置key的有效期
			jedisClinet.exprie(REDIS_ITEM_KEY + ":" + itemId + ":base", REDIS_ITEM_EXPIRE);
			System.out.println("从数据库中获取信息，并把商品信息存入redis");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaotaoResult.ok(tbItem);
	}
	@Override
	public TaotaoResult getItemDescInfo(long itemId) {
		//添加缓存逻辑
		try {
			//添加缓存逻辑
			//从缓存中获取商品信息，商品id对应的信息
			String json = jedisClinet.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
			if(!StringUtils.isBlank(json)){
				System.out.println("从缓存中读取数据...");
				TbItemDesc itemdesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return TaotaoResult.ok(itemdesc);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//创建查询条件
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			//把商品信息写入缓存
			jedisClinet.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(itemDesc));
			//设置key的有效期
			jedisClinet.exprie(REDIS_ITEM_KEY + ":" + itemId + ":desc", REDIS_ITEM_EXPIRE);
			System.out.println("从数据库中获取信息，并把商品信息存入redis");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaotaoResult.ok(itemDesc);
	}
	@Override
	public TaotaoResult getItemParamInfo(long itemId) {
		try {
			//添加缓存逻辑
			//从缓存中获取商品信息，商品id对应的信息
			String json = jedisClinet.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
			if(!StringUtils.isBlank(json)){
				System.out.println("从缓存中读取数据...");
				TbItemParamItem itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return TaotaoResult.ok(itemParamItem);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//根据商品id查询规格参数
		//设置查询条件
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		//执行查询
		List<TbItemParamItem> itemParamItems = itemParamMapper.selectByExampleWithBLOBs(example);
		if (itemParamItems != null && itemParamItems.size()>0) {
			TbItemParamItem paramItem = itemParamItems.get(0);
			try {
				//把商品信息写入缓存
				jedisClinet.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(paramItem));
				//设置key的有效期
				jedisClinet.exprie(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
				System.out.println("从数据库中获取信息，并把商品信息存入redis");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return TaotaoResult.ok(paramItem);
		}
		return TaotaoResult.build(400, "无此商品记录");
	}

}

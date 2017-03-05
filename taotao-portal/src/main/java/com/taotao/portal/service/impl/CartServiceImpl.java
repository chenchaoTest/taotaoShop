package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.TbItem;
import com.taotao.portal.service.CartService;
/**
 * 添加购物车商品
 * @author chao
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	
	@Override
	public TaotaoResult addCarItem(long itemId, int num,HttpServletRequest request,HttpServletResponse response) {
		CartItem ct = null;
		//取购物车商品列表
		List<CartItem> cartList = getCartItemList(request);
		//判断购物车商品是否存在该商品
		for (CartItem cItem : cartList) {
			//如果存在该商品
			if(cItem.getId()==itemId){
				//增加商品数量、
				cItem.setNum(cItem.getNum() + num);
				ct = cItem;
				break;
			}
		}
		if(ct == null){
			ct = new CartItem();
			//根据商品id查询商品信息
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			//把json转换成java对象
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
			if(result.getStatus()==200){
				TbItem tb = (TbItem)result.getData();
				ct.setId(tb.getId());
				ct.setTitle(tb.getTitle());
				ct.setImage(tb.getImages() == null ? "":tb.getImage().split(",")[0]);
				ct.setNum(num);
				ct.setPrice(tb.getPrice());
			}
			//添加到购物车列表
			cartList.add(ct);
		}
		//把购物车列表写入到cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList),true);
		return TaotaoResult.ok();
	}
	public List<CartItem> getCartItemList(HttpServletRequest request){
		//从cookie去商品列表
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART",true);
		if(cartJson == null){
			return new ArrayList<>();
		}
		//把json转换成商品列表
		try {
			List<CartItem> cartList = JsonUtils.jsonToList(cartJson, CartItem.class);
			return cartList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request,
			HttpServletResponse response) {
		List<CartItem> cartList = getCartItemList(request);
		return cartList;
	}
	/**
	 * 删除购物车商品
	 */
	@Override
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		// 从cookie中获取商品列表
		List<CartItem> cartList = getCartItemList(request);
		for (CartItem cartItem : cartList) {
			if(cartItem.getId()==itemId){
				cartList.remove(cartItem);
				break;
			}
		}
		//把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList),true);
		return TaotaoResult.ok();
	}
}
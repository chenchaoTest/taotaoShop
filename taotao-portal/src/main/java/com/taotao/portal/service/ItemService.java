package com.taotao.portal.service;

import com.taotao.portal.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(Long itemId);
	String getItemDescById(Long itemId);
	String getItemParam(Long itemId);
}
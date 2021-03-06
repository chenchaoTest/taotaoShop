package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/info/{itemId}")
	@ResponseBody
	public TaotaoResult getItemBaseInfo(@PathVariable Long itemId){
		TaotaoResult taotaoResult = itemService.getItemBaseInfo(itemId);
		return taotaoResult;
	}
	
	@RequestMapping("/desc/{itemId}")
	@ResponseBody
	public TaotaoResult getItemDescInfo(@PathVariable Long itemId){
		TaotaoResult result = itemService.getItemDescInfo(itemId);
		return result;
	}
	@RequestMapping("/param/{itemId}")
	@ResponseBody
	public TaotaoResult getItemParamInfo(@PathVariable Long itemId){
		TaotaoResult result = itemService.getItemParamInfo(itemId);
		return result;
	}
}

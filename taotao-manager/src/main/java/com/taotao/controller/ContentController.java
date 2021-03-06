package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import com.taotao.utils.TaotaoResult;

@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataGridResult queryList(@RequestParam(value="categoryId", defaultValue="0")Long categoryId){
		EUDataGridResult dataGridResult = contentService.queryList(categoryId);
		return dataGridResult;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		TaotaoResult result = contentService.insertContent(content);
		return result;
	}
}

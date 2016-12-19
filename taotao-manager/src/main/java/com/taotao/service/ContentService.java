package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.utils.TaotaoResult;

public interface ContentService {
	EUDataGridResult queryList(long categoryId);
	TaotaoResult insertContent(TbContent content);
}

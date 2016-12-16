package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.utils.EasyUIResult;
import com.taotao.utils.TaotaoResult;

public interface ItemParamService {
	TaotaoResult getItemParamByCid(long cid);
	EUDataGridResult getItemList(int page,int rows);
	EasyUIResult getItemParamList(Integer page, Integer rows);
	TaotaoResult insertItemParam(TbItemParam itemParam);
}

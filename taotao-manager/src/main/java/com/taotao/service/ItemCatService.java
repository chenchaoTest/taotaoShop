package com.taotao.service;

import java.util.List;

import com.taotao.pojo.TbItemCat;

public interface ItemCatService {
	List<TbItemCat> getItemCatList(long parentId);
}

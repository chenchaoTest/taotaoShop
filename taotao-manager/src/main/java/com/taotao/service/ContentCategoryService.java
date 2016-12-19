package com.taotao.service;

import java.util.List;

import com.taotao.utils.EUTreeNode;
import com.taotao.utils.TaotaoResult;

public interface ContentCategoryService {
	List<EUTreeNode> getCategoryList(long parentId);
	TaotaoResult insertContentCategory(long parentId, String name);
	TaotaoResult deleteContentCategory(long parentId,long id);
}

package com.taotao.search.pojo;

import java.util.List;

public class SearchResult {
	//��Ʒ�б�
	private List<Item> itemList;
	//�ܼ�¼��
	private long recordCount;
	//��ҳ��
	private long pageCount;
	//��ǰҳ
	private long curPage;
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getCurPage() {
		return curPage;
	}
	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}
	
}
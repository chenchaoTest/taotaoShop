package com.taotao.utils;

import java.util.List;

/**
 * easyUIDataGrid对象返回值
 * <p>Title: EasyUIResult</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	陈超
 * @date	2016年11月30日 15:17:19
 * @version 1.0
 */
public class EasyUIResult {

	private Integer total;
	
	private List<?> rows;
	
	public EasyUIResult(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public EasyUIResult(long total, List<?> rows) {
		this.total = (int) total;
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}

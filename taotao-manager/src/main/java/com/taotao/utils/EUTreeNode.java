package com.taotao.utils;
/**
 * easyUI树形控件节点格式
 * <p>Title: EUTreeNode</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	陈超
 * @date	2016年12月18日 16:49:01
 * @version 1.0
 */
public class EUTreeNode {

	private long id;
	private String text;
	private String state;
	private long parentId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
}

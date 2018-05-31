package com.otitan.xnbhq.supertreeview;

/**
 * 节点资源实体类
 */
public class NodeResource {
	/** 父节点id*/
	protected String parentId;
	/***/
	protected String title;
	/** 父节点id*/
	protected String value;
	/** 父节点id*/
	//protected int iconId;
	/** 父节点id*/
	protected String curId;
	/** */
	public NodeResource(String parentId, String curId, String title,String value) {
		super();
		this.parentId = parentId;
		this.title = title;
		this.value = value;
		//this.iconId = iconId;
		this.curId = curId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

//	public int getIconId() {
//		return iconId;
//	}

	public String getCurId() {
		return curId;
	}

}

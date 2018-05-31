package com.otitan.xnbhq.entity;

import com.otitan.xnbhq.db.sqlite.Column;
import com.otitan.xnbhq.db.sqlite.Id;
import com.otitan.xnbhq.db.sqlite.Table;

/**
 * 小地名搜索历史
 */
@Table(name = "History")
public class XdmSearchHistory {

	/**小地名搜索历史列表 主键Id*/
	@Id(autoGenerate = true, name = "Id")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	/**小地名搜索历史输入名称*/
	@Column(name = "name")
	private String name;
	/**小地名搜索历史输入 时间*/
	@Column(name = "time")
	private String time;


}

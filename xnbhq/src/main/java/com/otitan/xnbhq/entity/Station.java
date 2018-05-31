package com.otitan.xnbhq.entity;

import com.otitan.xnbhq.db.sqlite.Column;
import com.otitan.xnbhq.db.sqlite.Id;
import com.otitan.xnbhq.db.sqlite.Table;

/**
 * 小地名信息实体类
 */
@Table(name = "station")
public class Station {
	/**小地名表主键id*/
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
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**小地名名称*/
	@Column(name = "name")
	private String name;
	/**小地名经度*/
	@Column(name = "x")
	private String x;
	/**小地名纬度*/
	@Column(name = "y")
	private String y;
	/**小地名，地名类型*/
	@Column(name = "type")
	private String type;

	public Station() {
		// TODO Auto-generated constructor stub
	}

	public Station(String name,String lon,String lat,String type) {
		this.name = name;
		this.x = lon;
		this.y = lat;
		this.type = type;
	}

}

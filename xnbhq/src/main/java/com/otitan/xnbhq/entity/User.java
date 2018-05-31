package com.otitan.xnbhq.entity;

import com.otitan.xnbhq.db.sqlite.Column;
import com.otitan.xnbhq.db.sqlite.Id;
import com.otitan.xnbhq.db.sqlite.Table;

/**
 * 用户信息实体类
 */
@Table(name = "User")
public class User {
	
	@Id(autoGenerate = true, name = "Id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "password")
	private String psw;
	
	public User() {
		
	}
	
	public User(String name,String pwd) {
		this.name = name;
		this.psw = pwd;
	}
	
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
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	
}

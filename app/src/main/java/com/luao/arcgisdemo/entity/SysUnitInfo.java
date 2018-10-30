/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.luao.arcgisdemo.entity;



/**
 * 站点信息Entity
 * @author tangguo
 * @version 2017-12-21
 */
public class SysUnitInfo  {
	
	private String id;
	private String name;		// 站点名称
	private Double lgtd;		// 经度
	private Double lttd;		// 纬度
	private String isShow;		// 是否显示
	private String esstym;		// 建站年月
	private String adag;		// 管理单位
	private Integer sort;		// 排序
	private String addr;		// 站址
	private String key1;		// key1
	private String key2;		// key2
	private String key3;		// key3

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLgtd() {
		return lgtd;
	}

	public void setLgtd(Double lgtd) {
		this.lgtd = lgtd;
	}

	public Double getLttd() {
		return lttd;
	}

	public void setLttd(Double lttd) {
		this.lttd = lttd;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getEsstym() {
		return esstym;
	}

	public void setEsstym(String esstym) {
		this.esstym = esstym;
	}

	public String getAdag() {
		return adag;
	}

	public void setAdag(String adag) {
		this.adag = adag;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}
}
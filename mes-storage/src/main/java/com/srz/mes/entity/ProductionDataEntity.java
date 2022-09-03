package com.srz.mes.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-08-29 13:01:16
 */
@Data
@TableName("production_data")
public class ProductionDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String clientId;
	/**
	 * 
	 */
	private String cellId;
	/**
	 * 
	 */
	private Integer result;
	/**
	 * 
	 */
	private String data;
	/**
	 * 
	 */
	private String note;

}

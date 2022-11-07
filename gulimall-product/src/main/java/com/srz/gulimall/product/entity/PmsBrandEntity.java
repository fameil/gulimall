package com.srz.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.srz.common.valid.AddGroup;
import com.srz.common.valid.ListValue;
import com.srz.common.valid.UpdataeStatusGroup;
import com.srz.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;


/**
 * 品牌
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@Data
@TableName("pms_brand")
public class PmsBrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改必须指品牌定id",groups = UpdateGroup.class)
	@Null(message = "新增不能指定id",groups = AddGroup.class)
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	//jsr303校验注解（配合@Valid使用）
	@NotBlank(message = "品牌名必须填写",groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(groups = AddGroup.class)
	@URL(message = "logo必须是一个合法的url地址",groups = {AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {AddGroup.class, UpdataeStatusGroup.class})
	@ListValue(vals = {0,1},groups = {AddGroup.class, UpdataeStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(groups = AddGroup.class)
	@Pattern(regexp = "^[a-zA-z]$",message = "检索首字母必是一个字母",groups = {AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = AddGroup.class)
	@Min(value = 0,message = "排序必须大于等于0",groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;

}

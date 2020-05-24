package com.byy.product.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.byy.common.validate.AddGroup;
import com.byy.common.validate.ListValue;
import com.byy.common.validate.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	//在添加的时候是不允许，必须按照后端的规则自动填充主键
	@Null(message = "不能自定义ID",groups = {AddGroup.class})
	//在更新的时候必须携带主键值，因为需要根据主键查找修改的那一行
	@NotNull(message = "更新时ID不能为空",groups = {UpdateGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotNull(message = "logo不能为空",groups = {AddGroup.class})
	@URL
	private String logo;
	/**
	 * 介绍
	 */
	@NotEmpty(message = "介绍不能为空",groups = {AddGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空",groups = {AddGroup.class})
	@ListValue(listValue = {1,2},groups = {AddGroup.class, UpdateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotNull(message = "检索首字母不能为空",groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]{1}$",message = "检索首字母必须在a-z或者A-Z之间",groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空",groups = {AddGroup.class})
	@Min(value = 0,message = "排序大于0",groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}

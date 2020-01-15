package com.video.vip.entity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wxn
 * @Description: 分页查询请求对象
 */
@Data
@ApiModel(description = "分页查询请求对象")
public class QueryPageRequest<T> implements Serializable {
    private static final long serialVersionUID = 6057973394226489584L;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "每页记录数")
    private Integer size;

    @ApiModelProperty(value = "检索条件")
    private T data;

}
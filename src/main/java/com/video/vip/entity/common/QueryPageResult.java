package com.video.vip.entity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: wxn
 * @Description: 通用分页查询返回对象
 */
@Data
@ApiModel(description = "通用分页查询返回对象")
public class QueryPageResult<T> implements Serializable {
    private static final long serialVersionUID = -4805648937567953921L;

    @ApiModelProperty(value = "数据列表")
    private List<T> list;

    @ApiModelProperty(value = "数据总数")
    private Long total;

    @ApiModelProperty(value = "总页数")
    private Long totalPage;
}
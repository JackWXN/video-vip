package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@TableName(value = "order_info")
public class OrderInfo extends BaseModel {
    private static final long serialVersionUID = -905529792733117851L;

    /**
     * pid
     */
    private Long pid;

    /**
     * vip类型。枚举 VipTypeEnum
     * 1:体验卡 2:月卡 3:季卡 4:半年卡 5:年卡 6:终身卡
     */
    private Integer vipType;

    /**
     * 订单金额，单位分
     */
    private Long orderAmount;

    /**
     * 订单状态。枚举 orderStatusEnum
     * 1:支付失败 2:支付失败
     */
    private Integer orderStatus;
}

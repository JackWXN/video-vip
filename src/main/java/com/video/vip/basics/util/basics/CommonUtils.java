package com.video.vip.basics.util.basics;

import org.apache.commons.lang.StringUtils;

/**
 * @author linzhixiong
 * @desc
 * @date 2019-04-01 11:32
 */
public class CommonUtils {

    /**
     * @description:  手机号码前三后四脱敏
     * @author linzhixiong
     * @date 2019/4/1 11:33
     * @param:
     * @return:
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * @description:  身份证前三后四脱敏
     * @author linzhixiong
     * @date 2019/4/1 11:33
     * @param:
     * @return:
     */
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{4})\\w(?=\\w{4})", "*");
    }

    /**
     * @description: 银行卡后四脱敏
     * @author linzhixiong
     * @date 2019/4/1 19:29
     * @param:
     * @return:
     */
    public static String bankcardEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 16)) {
            return id;
        }
        return id.replaceAll("\\w(?=\\w{4})", "*");
    }

    /**
     * @Desc: 根据身份证获取性别(请款使用)
     * @param: [certId] 身份证号
     * @Return: int  1：男   2：女
     * @Author: lixiping
     * @Date: 2018/5/22
     */
    public static String getSexForCertId(String certId){
        char lastNextChar = certId.charAt(certId.length() - 2);
        if(Integer.valueOf(String.valueOf(lastNextChar)) % 2 == 0){
            return "N0202";
        }else{
            return "N0201";
        }
    }
}

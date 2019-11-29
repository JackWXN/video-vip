package com.video.vip.basics.util.basics;

import java.math.BigDecimal;

/**
 * @Author lixiping
 * @Description 还款计划生成器帮助类
 * @Date 2019/5/27 20:20
 * @Param 
 * @return 
 **/
public class RepayUtil {

    /**
     * @Author lixiping
     * @Description 息费分离订单总合同金额计算
     * @Date 2019/5/27 20:23
     * @Param [loanAmt 借款金额, lowNper 低额期数, highNper 高额期数, lowRate 低额利率, highRate 高额利率, monthRate 月利率]
     * @return java.math.BigDecimal
     **/
    public static BigDecimal contractTotalAmt(BigDecimal loanAmt,int lowNper,int highNper,BigDecimal lowRate,BigDecimal highRate,BigDecimal monthRate){
        //月还款金额=相应的高低额利率*借款金额
        BigDecimal monthRetAmt = BigDecimal.ZERO;
        //总低额贴现金额
        BigDecimal lowDisAmt = BigDecimal.ZERO;
        if(lowNper>0) {
            monthRetAmt = loanAmt.multiply(lowRate);
            for (int a = 0; a < lowNper; a++) {
                lowDisAmt = lowDisAmt.add(getFdis(monthRetAmt, monthRate, a+1));
            }
        }
        //总高额贴现金额
        BigDecimal hightDisAmt = BigDecimal.ZERO;
        if(highNper>0) {
            monthRetAmt = loanAmt.multiply(highRate);
            int total = lowNper+highNper;
            for (int b = lowNper; b < total; b++) {
                hightDisAmt = hightDisAmt.add(getFdis(monthRetAmt, monthRate, b+1));
            }
        }
        return lowDisAmt.add(hightDisAmt);
    }

    /**
     * @Author lixiping
     * @Description 每月贴现计算:当期应还款金额*(1+合同月利率)^-期数
     * @Date 2019/5/27 20:07
     * @Param [retAmt 当期应还, contractMIr 月利率, nper 当前期数]
     * @return java.math.BigDecimal
     **/
    public static BigDecimal  getFdis(BigDecimal retAmt,BigDecimal contractMIr,int nper){
        double orgDisAmt = 1.0 ;
        for(int m = 0 ; m < nper ; m++){
            orgDisAmt *= new BigDecimal(( 1 + contractMIr.doubleValue())).doubleValue();
        }
        return new BigDecimal(1).divide(new BigDecimal(orgDisAmt) , 10 , BigDecimal.ROUND_HALF_UP).multiply(retAmt).divide(new BigDecimal(1), 2 , BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @Author lixiping
     * @Description 月利率计算 = （1+年利率）^(1/12)-1
     * @Date 2019/5/27 20:38
     * @Param [yearRate]
     * @return java.math.BigDecimal
     **/
    public static BigDecimal monthRate(BigDecimal yearRate){
        //合同月利率=(1+合同年利率)^(1/12)-1
        return new BigDecimal(Math.pow(yearRate.add(new BigDecimal(1)).doubleValue(), (1/12.0))).subtract(new BigDecimal(1)).setScale(8 , BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @Author lixiping
     * @Description 借款金额与总合同金额比值
     * @Date 2019/5/27 20:59
     * @Param [loanAmt, contractTotalAmt]
     * @return java.math.BigDecimal
     **/
    public static BigDecimal ratio(BigDecimal loanAmt,BigDecimal contractTotalAmt){
        return loanAmt.divide(contractTotalAmt,8,BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[]args){
        System.out.println(contractTotalAmt(new BigDecimal("10000"),6,14,new BigDecimal("0.01"),new BigDecimal("0.081429"),new BigDecimal("0.00797414")));
        System.out.println(monthRate(new BigDecimal("0.1")));
        System.out.println(ratio(new BigDecimal("10000"),new BigDecimal("10829.81")));

        System.out.println(new BigDecimal(10000).multiply(new BigDecimal("0.081429")).multiply(new BigDecimal("0.92337723").setScale(2,BigDecimal.ROUND_HALF_UP)));

    }




}
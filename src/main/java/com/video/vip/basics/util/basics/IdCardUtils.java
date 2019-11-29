package com.video.vip.basics.util.basics;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 身份证号工具类
 *
 * @author zhanglimin
 * @ClassName: IdCardUtils
 * @date 2017-2-16 下午2:03:48
 */
public class IdCardUtils {
    /**
     * 获取出生日期
     *
     * @param @param  idCard
     * @param @return
     * @return String 默认格式 yyyy-MM-dd
     * @Title: getBirthday
     */
    public static String getBirthday(String idCard, String pattern) {
        if (StringUtils.isEmpty(idCard)) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        String birthday = null;
        try {
            birthday = idCard.substring(6, 14);
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            birthday = DateUtil.getDateTime(birthdate, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return birthday;
    }

    /**
     * 获取年龄
     *
     * @param @param  idCard
     * @param @return
     * @return int
     * @Title: getAge
     */
    public static int getAge(String idCard) {
        int age = 0;
        if (StringUtils.isEmpty(idCard)) {
            return 0;
        }
        try {
            String birthday = idCard.substring(6, 14);
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            int year = DateUtil.getYear(birthdate);
            int nowYear = DateUtil.getYear(new Date());
            age = nowYear - year;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * 获取真实年龄
     *
     * @param @param  idCard
     * @param @return
     * @return int
     * @Title: getAge
     */
    public static int getRealAge(String idCard) {
        int age = 0;
        if (StringUtils.isEmpty(idCard)) {
            return 0;
        }
        try {
            String birthday = idCard.substring(6, 14);
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(birthdate);

            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            age = yearNow - yearBirth;
            if (monthNow < monthBirth || (monthNow == monthBirth && dayOfMonthNow < dayOfMonthBirth)) {
                age--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "未知";
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "男";
        } else {
            sGender = "女";
        }
        return sGender;
    }

    public static void main(String[] args) {
        System.out.println(getBirthday("130427198810043756", "yyyy-MM-dd"));
        System.out.println(getAge("130427198810043756"));



        String  aa  = "20180208-20280208";
        System.out.println(aa.substring(aa.length()-8));
    }
}

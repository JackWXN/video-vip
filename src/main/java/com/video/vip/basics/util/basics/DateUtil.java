package com.video.vip.basics.util.basics;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author Administrator
 */
@Slf4j
public class DateUtil {

	/**
	 * 获取指定日期的指定的格式的字符串格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateTime(Date date , String pattern){
		if(StringUtils.isEmpty(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);

	}

	/**
	 * 获取指定日期的指定的格式的字符串格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getStrToStr(String date , String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(paraseDate2(date,pattern));

	}

	public static Date parseDateToDate(Date date , String pattern) throws ParseException {
		return paraseDate(getDateTime(date, pattern) , pattern);

	}

	public static Date paraseDate(String str , String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(str);
	}


	/**
	 * 日期类型转换日期类型
	 * @param date 时期
	 * @param pattern 日期规则，如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date parseDateToDate2(Date date , String pattern) {
		return paraseDate2(getDateTime(date, pattern) , pattern);

	}

	/**
	 * 格式化字符串日期，返回date类型
	 * @param str 时期
	 * @param pattern 日期规则，如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date paraseDate2(String str , String pattern) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try{
			date = format.parse(str);
		}catch (ParseException pe){
			log.error("日期转换出现异常,str:{},pattern:{}");
			throw new RuntimeException(pe);
		}
		return date;
	}

	/**
	 * 获取与当前日期相差指定月份的月份
	 * @return
	 */
	public static Calendar getSpecMonth(Integer diffMonth){

		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MONTH, diffMonth);

		return nowCal;
	}

	/**
	 * 获取指定日期的日
	 * @return
	 */
	public static Integer getDay(Date date){

		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		int dayOfMonth = nowCal.get(Calendar.DAY_OF_MONTH);

		return dayOfMonth;

	}
	/**
	 * 获取与指定日期相差指定月份的月份
	 * @return
	 */
	public static Calendar getSpecMonth(Date date, Integer diffMonth){

		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		nowCal.add(Calendar.MONTH, diffMonth);

		return nowCal;
	}
	/**
	 * 获取指定日期的月份
	 * @param date
	 * @return
	 */
	public static Integer getMonth(Date date){

		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		int month = nowCal.get(Calendar.MONTH);

		return month + 1;

	}

	public static Integer getHour(Date date){
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		int hour = nowCal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	/**
	 * 获取指定日期的年份
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date){

		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		int year = nowCal.get(Calendar.YEAR);

		return year;

	}
	/**
	 * 计算两个日期相差多少个月
	 * @return
	 */
	public static int diffMonth(Date startDate , Date endDate){

		Long startTime = startDate.getTime();
		Long endTime = endDate.getTime();
		Long diffTime = endTime - startTime ;

		if(diffTime < 0){
			return -1 ;
		}

		return Integer.parseInt(String.valueOf(diffTime /(60 * 60 * 24 * 30 )));

	}
    /**
     * 计算两个日期相差多少秒,返回秒
     * @return
     */
    public static Long diffSecond(Date startDate , Date endDate){
        Long startTime = startDate.getTime();
        Long endTime = endDate.getTime();
        Long diffTime = (endTime - startTime)/1000L ;
        return diffTime;

    }

	public static String getDate() {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			String date=sdf.format(new Date());
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDateStr() {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
			String date=sdf.format(new Date());
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDateCn(){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=sdf.format(new Date());
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween (Date smdate, Date bdate) throws ParseException
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		smdate=sdf.parse(sdf.format(smdate));
		bdate=sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 计算两个日期之间相差的秒数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int secondBetween (Date smdate, Date bdate) throws ParseException
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		smdate=sdf.parse(sdf.format(smdate));
		bdate=sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000);
		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 得到几天后的时间
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 获取当天凌晨时间
	 * @author wuqiang
	 */
	public static Date getMorningDate(Date d){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str = df.format(d);
		Date m = null;
		try {
			m = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return m;
	}
	/**
	 * 获取指定时间的前几天或后几天的指定时分秒
	 * @Title: getAppointDate
	 * @param @param source 源时间
	 * @param @param day 相差天数(为负数则为前几天)
	 * @param @param Hms 时分秒
	 * @param @return
	 * @return Date
	 */
	public static Date getAppointDate(Date source, int day, String Hms){
		Date tarGetDate=getDateAfter(source, day);
		String tarGetTime=getDateTime(tarGetDate, "yyyy-MM-dd")+" "+Hms;
		Date result=null;
		try {
			result = paraseDate(tarGetTime, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			result=new Date();
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取下一天
	 * @Title: getNextDay
	 * @Description:
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getNextDay(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		return sdf.format(date);
	}

	/**
	 * 获取指定时间的前几天或后几天
	 * flag = true 前几天  = false 后几天
	 * @return Date
	 */
	public static Date getDayOfNum(int num, Date date,boolean flag){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if(flag){
			ca.add(Calendar.DATE, num);
		} else {
			ca.add(Calendar.DATE, num*(-1));
		}
		Date time = ca.getTime();
		return time;
	}

	/**
	 *
	 * @Title: checkLeapYear
	 * @Description:判断是否是闰年
	 * @param @param year
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkLeapYear(int year) {
		boolean flag = false;
		if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
			flag = true;
		}
		return flag;
	}

	public static java.sql.Date parseMongoDateQuery(@NotNull Date date){
		return new java.sql.Date(date.getTime());
	}

	/**
	 *
	 * @Title: isRunYear
	 * @Description:是否闰年
	 * @param @param year
	 * @param @return
	 * @return Boolean
	 * @throws
	 */
	public static Boolean isRunYear(int year){
		if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 增加月
	 * @param date 要增加的日期
	 * @param monthNum 增加的月数
	 * @return
	 */
	public static Date getAddMonth(Date date, Integer monthNum) {
		Calendar calBig = Calendar.getInstance();
		calBig.setTime(date);
		calBig.add(Calendar.MONTH, monthNum);
		return calBig.getTime();
	}

	/**
	 * 增加小时
	 * @param date 要增加的日期
	 * @param hourNum 增加的小时数
	 * @return
	 */
	public static Date getAddHour(Date date, Integer hourNum) {
		Calendar calBig = Calendar.getInstance();
		calBig.setTime(date);
		calBig.add(Calendar.HOUR_OF_DAY, hourNum);
		return calBig.getTime();
	}
	/**
	 * 增加毫秒
	 * @param date
	 * 日期
	 * @param millisecondNum
	 * 增加的毫秒数，可为负数
	 * @return
	 */
	public static Date getAddMillisecond(Date date, Integer millisecondNum){
		Calendar calBig= Calendar.getInstance();
		calBig.setTime(date);
		calBig.add(Calendar.MILLISECOND , millisecondNum);
		return calBig.getTime();
	}

	/**
	 * 获得加上或减去天数后的时间
	 * @param num 		 天数
	 * @param newDate	 时间
	 * @param flag  	true:加法；false:减法；
	 */
	public static Date getOperationDate(int num , String newDate, boolean flag){
		try {
			int number = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currdate = format.parse(newDate.toString());
			if(flag) {
				number = num;
			}else{
				number = num * (-1);
			}
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, number);
			currdate = ca.getTime();
			return format.parse(format.format(currdate).toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static int compareDate(Date DATE1, Date DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(df.format(DATE1));
			Date dt2 = df.parse(df.format(DATE2));
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static String getLastDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd");
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		return last;
	}

	public static Long getRemainSecondsOneDay() {
		Date currentDate = new Date();
		LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),ZoneId.systemDefault());
		long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
		return (Long) seconds;
	}

	/**
	 * 转换字符串日期格式为date类型，精确到毫秒，不足毫秒的补0.注意！必须从年开始,yyyyMMddHHmmssSSS
	 * 必须为24小时时间制
	 * 每位如果小于10，必须有0补齐如2019-01-05T03:04:09不能为2019-1-5T3:4:9
	 * @param date 要转换的时间
	 * @return
	 */
	public static Date parseStrToDateAll(@NonNull String date){
		StringBuffer dateStrb = new StringBuffer();
		dateStrb = new StringBuffer(date.replaceAll("[^\\d]",""));
		System.out.println(dateStrb);
		int dsLen = dateStrb.length();
		if(dsLen!=8&&dsLen!=10&&dsLen!=12&&dsLen!=14&&dsLen!=17){
			throw new RuntimeException("日期格式错误:date = "+date);
		}
		if(dateStrb.length()<17){
			for(int i=17-dateStrb.length();i<17;i++){
				dateStrb.append("0");
			}
		}
		Date dretu = DateUtil.paraseDate2(dateStrb.toString(),"yyyyMMddHHmmssSSS");
		return dretu;
	}



	public static void main(String[] args) throws Exception {
//		int a = getHour(new Date());
//		System.out.println(a);
//		System.out.println(isDeductDate());
		//DateUtil.paraseDate(DateUtil.getDateTime(new Date(),"yyyy-MM-dd")+" 23:59:59","yyyy-MM-dd HH:mm:ss");
		//int a = secondBetween(paraseDate("2017-11-30 00:00:11","yyyy-MM-dd HH:mm:ss"),paraseDate("2017-11-30 00:00:40","yyyy-MM-dd HH:mm:ss"));
//		System.out.println(getRemainSecondsOneDay());
//		int a  = compareDate("2017-02-20","2017-02-20");
//		System.out.println(a);

		String a = "2019-01-03　的双方各";
		System.out.println(DateUtil.parseStrToDateAll(a));
		System.out.println(DateUtil.getDateTime(DateUtil.parseStrToDateAll(a),"yyyy-MM-dd HH:mm:ss"));
	}
}

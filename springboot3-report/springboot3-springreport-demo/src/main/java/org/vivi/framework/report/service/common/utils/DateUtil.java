package org.vivi.framework.report.service.common.utils;

import org.vivi.framework.report.service.common.constants.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**  
 * 日期工具类
*/
public class DateUtil {

	/**
     * 预设不同的时间格式
     */
    //精确到年月日（英文） eg:2019-12-31
    //精确到时分秒的完整时间（英文） eg:2010-11-11 12:12:12
	public static String FORMAT_LONOGRAM = "yyyy-MM-dd";
	public static String FORMAT_LONOGRAM_2 = "yyyy/MM/dd";
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    //精确到毫秒完整时间（英文） eg:2019-11-11 12:12:12.55
    public static String FORMAT_LONOGRAM_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
    //精确到年月日（中文）eg:2019年11月11日
    public static String FORMAT_LONOGRAM_CN = "yyyy年MM月dd日";
    public static String FORMAT_LONOGRAM_CN_2 = "yyyy年M月d日";
    //精确到时分秒的完整时间（中文）eg:2019年11月11日 12时12分12秒
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日 HH时MM分SS秒";
    //精确到毫秒完整时间（中文）
    public static String FORMAT_LONOGRAM_MILL_CN = "yyyy年MM月dd日HH时MM分SS秒SSS毫秒";
    
    public static String FORMAT_YEARMONTH = "yyyy-MM";
    
    public static String FORMAT_DATEHOUR = "yyyy-MM-dd HH";
    
    public static String FORMAT_WITHOUTSECONDS = "yyyy-MM-dd HH:mm";
    
    public static String FORMAT_HOURSMINUTES = "hh:mm a";
    
    public static String FORMAT_HOURSMINUTES_2 = "hh:mm";
    
    public static String FORMAT_HOURSMINUTES_3 = "h:mm";
    
    public static String FORMAT_HOURSMINUTESSECONDS = "hh:mm:ss";
    
    public static String FORMAT_HOURSMINUTESSECONDS_2 = "h:mm:ss";
    
    public static String FORMAT_FULL_12 = "yyyy-MM-dd hh:mm a";
    
    public static String FORMAT_DATE = "MM-dd";
    
    public static String FORMAT_DATE_2 = "M-d";
    
    public static String FORMAT_DATE_CN = "M月d日";
    
    public static String FORMAT_YEAR = "yyyy";
	
	 /**  
	 * 默认日期格式
	 */ 
	private static final SimpleDateFormat format = new SimpleDateFormat(FORMAT_FULL);
	
	 /**
	 * 日期转换成string，采用末日的日期格式
	 */ 
	public static String date2String(Date date) {
		 return format.format(date);
	 }
	
    /**
     * 日期转换成string，自定义日期格式
     */ 
    public static String date2String(Date date,String dateFormat) {
    	if (StringUtil.isNullOrEmpty(dateFormat)) {
			return format.format(date);
		}
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return sdf.format(date);
    }
    
    /**
     * 字符串转日期，采用默认日期格式
     */ 
    public static Date string2Date(String date) {
    	 try {
             return format.parse(date);
         } catch (Exception e) {
             e.printStackTrace();
         }

         return null;
    }
    
    /**
     * 字符串转日期，自定义日期格式
     */ 
    public static Date string2Date(String date, String dateFormat) {
    	try {
    		if (StringUtil.isNullOrEmpty(dateFormat)) {
        		return format.parse(date);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
        	return sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 返回当前时间字符串，默认格式
     */ 
    public static String getNow() {
        return date2String(new Date());
    }
    
    /**
     * 返回当前时间字符串，自定义格式
     */ 
    public static String getNow(String format) {
        return date2String(new Date(),format);
    }
    
    /**
     * 获取时间戳
     */ 
    public static String getTimeStamp() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.getTimeInMillis());
    }
    
    /**
     * 获取日期的年份
     */ 
    public static String getYear(Date date) {
        return date2String(date).substring(0,4);
    }
    
    /**
     * 获取日期的年份+月份
     */ 
    public static String getYearMonth(Date date) {
        return date2String(date).substring(0, 7);
    }
    
    /**
     * 获取日期的小时数
     */ 
    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * 时间戳转化为字符串，自定义格式
     */ 
    public static String Stamp2String(Long stamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(stamp));
    }

    /**
     * 时间戳转化为字符串，默认格式
     */ 
    public static String Stamp2String(Long stamp) {
        return format.format(new Date(stamp));
    }
    
	/**
	 * 获取long类型的时间戳
	 */ 
	public static Long getTimestampLong(){
		return Long.valueOf(System.currentTimeMillis());
	}
	
	/**
	 * 日期加天数
	 */ 
	public static String addDays(int num,String date) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_LONOGRAM);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	public static String addDays(int num,String date,String dateFormat) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	/**
	 * 月份加减
	 */  
	public static String addMonth(int num,String date) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_LONOGRAM);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.MONTH, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	public static String addMonth(int num,String date,String dateFormat) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.MONTH, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	public static String addYear(int num,String date,String dateFormat) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.YEAR, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	/**
	 * 日期减去天数
	 */ 
	public static String subDays(int num,String date) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_LONOGRAM);
		Date currdate = format.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.DATE, -num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}
	
	/**
	 * 日期大小比较 1 开始 大于结束;0 两者等于;-1 开始小于结束
	 */ 
	public static int daysCompare(String start,String end) throws ParseException
	{
		SimpleDateFormat sdft = new SimpleDateFormat(FORMAT_LONOGRAM);
		Date startDate =  sdft.parse(start);
		Date endDate =  sdft.parse(end);
		return startDate.compareTo(endDate);
	}
	
	public static int daysCompare(String start,String end,String dateFormat) throws ParseException
	{
		SimpleDateFormat sdft = new SimpleDateFormat(dateFormat);
		Date startDate =  sdft.parse(start);
		Date endDate =  sdft.parse(end);
		return startDate.compareTo(endDate);
	}
	
	/**  
	 * getWeek
	 * 根据日期计算周几
	 */ 
	public static String getWeek(String date)
	{
		SimpleDateFormat f = new SimpleDateFormat(FORMAT_LONOGRAM);
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(date);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];

	}
	
	/**
     * 判断字符串是否为合法的日期格式
     */
    public static boolean isValidDate(String dateStr){
        //判断结果 默认为true
        boolean judgeresult=true;
        //1、首先使用SimpleDateFormat初步进行判断，过滤掉注入 yyyy-01-32 或yyyy-00-0x等格式
        //此处可根据实际需求进行调整，如需判断yyyy/MM/dd格式将参数改掉即可
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            //增加强判断条件，否则 诸如2022-02-29也可判断出去
            format.setLenient(false);
            Date date =format.parse(dateStr);
        }catch(Exception e){
            judgeresult=false;
        }
        //由于上述方法只能验证正常的日期格式，像诸如 0001-01-01、11-01-01，10001-01-01等无法校验，此处再添加校验年费是否合法
        String yearStr=dateStr.split("-")[0];
        if(yearStr.startsWith("0")||yearStr.length()!=4){
            judgeresult=false;
        }
        return judgeresult;
    }
    /**
     * 判断字符串是否为合法的日期格式
     */
    public static boolean isValidDate(String dateStr,String dateFormat){
        //判断结果 默认为true
        boolean judgeresult=true;
        //1、首先使用SimpleDateFormat初步进行判断，过滤掉注入 yyyy-01-32 或yyyy-00-0x等格式
        //此处可根据实际需求进行调整，如需判断yyyy/MM/dd格式将参数改掉即可
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try{
            //增加强判断条件，否则 诸如2022-02-29也可判断出去
            format.setLenient(false);
            Date date =format.parse(dateStr);
        }catch(Exception e){
            judgeresult=false;
        }
        //由于上述方法只能验证正常的日期格式，像诸如 0001-01-01、11-01-01，10001-01-01等无法校验，此处再添加校验年费是否合法
        String yearStr=dateStr.split("-")[0];
        if(yearStr.startsWith("0")||yearStr.length()!=4){
            judgeresult=false;
        }
        return judgeresult;
    }	
	 public static String getNetworkTime() {
	        try {
	            URL url = new URL("http://www.baidu.com");
	            URLConnection conn = url.openConnection();
	            conn.connect();
	            long dateL = conn.getDate();
	            Date date = new Date(dateL);
	            SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.FORMAT_DATEHOUR);
	            return dateFormat.format(date);
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        return "";
	 }  
	 public static String ExcelDoubleToDate(String strDate) {
	        if (strDate.length() == 5) {
	            try {
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	                Date tDate = DoubleToDate(Double.parseDouble(strDate));
	                return sdf.format(tDate);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return strDate;
	            }
	        }
	        return strDate;
	 }
	 
	//解析Excel日期格式
	    public static Date DoubleToDate(Double dVal) {
	        Date tDate = new Date();
	        Calendar calendar = Calendar.getInstance();
	        long localOffset = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000); //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
	        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));

	        return tDate;
	    }
	    
	    public static java.sql.Date string2SqlDate(String dateString,String format)
	    {
	    	String s = DateUtil.date2String(DateUtil.string2Date(dateString, format),DateUtil.FORMAT_LONOGRAM);
	        java.sql.Date date = java.sql.Date.valueOf(s);
	        return date;
	    }
	    
	    public static java.sql.Timestamp string2SqlTimestamp(String dateString,String format)
	    {
	    	SimpleDateFormat dateFormatHiddenHour = new SimpleDateFormat(format);
	    	java.sql.Timestamp date = null;
	        try {
	            String s = dateFormatHiddenHour.format(dateFormatHiddenHour.parse(dateString));
	            date = java.sql.Timestamp.valueOf(s);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return date;
	    }
	    
	    public static java.sql.Date string2SqlTimeStamp(String dateString,String format)
	    {
	    	SimpleDateFormat dateFormatHiddenHour = new SimpleDateFormat(format);
	    	java.sql.Date date = null;
	        try {
	            String s = dateFormatHiddenHour.format(dateFormatHiddenHour.parse(dateString));
	            date = java.sql.Date.valueOf(s);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return date;
	    }
	    
	    public static String getDefaultDate(String defaultValue,String dateFormat) throws ParseException {
	    	String result = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
	    	if(Constants.CURRENT_DATE.equals(defaultValue.toLowerCase())) {
	    		//当前日期
	    		result = DateUtil.getNow(StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
	    	}else {
	    		if(CheckUtil.isNumber(defaultValue))
				{
					int days = Double.valueOf(defaultValue).intValue();
					if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
					{
						result = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
					}else if(DateUtil.FORMAT_YEAR.equals(dateFormat))
					{
						result = DateUtil.addYear(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEAR);
					}else {
						result = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
					}
				}else {
					if(StringUtil.isNullOrEmpty(dateFormat)) {
						dateFormat = DateUtil.FORMAT_LONOGRAM;
					}
					if(!DateUtil.isValidDate(defaultValue,dateFormat))
					{
						result = "";;
					}
				}
	    	}
	    	
	    	return result;
	    }
	 
	/**  
	 * getLastSixDigits
	 * 获取时间戳后六位
	 */
	public static String getLastSixDigits() {
		String timeStampStr = DateUtil.getTimeStamp();
    	String lastSixDigits = timeStampStr.substring(timeStampStr.length() - 6);
    	return lastSixDigits;
	}
	    
    public static void main(String[] args) throws ParseException {
    	String timeStampStr = DateUtil.getTimeStamp();
    	String lastSixDigits = timeStampStr.substring(timeStampStr.length() - 6);
    	System.out.println(lastSixDigits);
    }
}

package com.appfuxion.selenium.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.appfuxion.selenium.model.ParamsObject;

public class Util {

	public static String getCurrentTimeFormat(String format){
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
	
	public static String getCurrentTimeFormat(String format, Date date){
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String getCurrentDateFormat(String format, String dateInString, String dateFormat) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(dateInString);
		return new SimpleDateFormat(dateFormat).format(date);
	}
	
	public static String getCurrentTimeFormat(String format, String dateInString, String timeFormat) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(dateInString);
		return new SimpleDateFormat(timeFormat).format(date);
	}

	public static String getDurationFormat(String format, String startDateInString, String endDateInString) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date startDate = formatter.parse(startDateInString);
        Date endDate = formatter.parse(endDateInString);
        long diff = endDate.getTime() - startDate.getTime();
        long diffSeconds = diff / 1000 % 60;         
        long diffMinutes = diff / (60 * 1000) % 60;         
        long diffHours = diff / (60 * 60 * 1000) % 24;
		return (diffHours < 10 ? "0" + diffHours : diffHours)
				+ ParamsObject.CONFIG_REPORT_RPT_DURATION_HOURS 
				+ (diffMinutes < 10 ? "0" + diffMinutes : diffMinutes) 
				+ ParamsObject.CONFIG_REPORT_RPT_DURATION_MINUTES
				+ (diffSeconds < 10 ? "0" + diffSeconds : diffSeconds)
				+ ParamsObject.CONFIG_REPORT_RPT_DURATION_SECONDS
				;
	}

	
	public static String getDurationFormatSeconds(String format, String startDateInString, String endDateInString) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date startDate = formatter.parse(startDateInString);
        Date endDate = formatter.parse(endDateInString);
        long diff = endDate.getTime() - startDate.getTime();
        long diffSeconds = diff / 1000; 
		return diffSeconds
				+ ParamsObject.PARAM_HTM_SECONDS
				;
	}
	
}

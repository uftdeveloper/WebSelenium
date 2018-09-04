package com.appfuxion.selenium.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.appfuxion.selenium.model.ParamsObject;

public class MyTraceLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
            SimpleDateFormat logTime = new SimpleDateFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS);
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(record.getMillis());
            String myLog = logTime.format(cal.getTime())
                    + " - ["
                    + record.getLevel()
            		+ "] - ["
                    + record.getSourceClassName().substring(
                            record.getSourceClassName().lastIndexOf(".")+1,
                            record.getSourceClassName().length())
                    + "."
                    + record.getSourceMethodName()
                    + "()] - "
                    + record.getMessage() + ParamsObject.PARAM_NEW_LINE;
            System.out.print(myLog);
            return myLog;
	}

}

package com.appfuxion.selenium.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;
import com.appfuxion.selenium.view.Main;

public class Log {
	static private FileHandler fileTraceLog;
    static private MyTraceLogFormatter formatterLog;
    
    static private Date currLogDate;

    //static private FileHandler fileHTML;
    //static private MyHtmlFormatter formatterHTML;
    
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);	   
	
    static public void setup(String logFileName) throws IOException {
    	    	
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        
        File logDir = new File(ParamsObject.PARAM_LOG_FOLDER);
	  	if(!logDir.exists()) {
	  	   logger.info(MessageObject.MSG0005);
	  	   logDir.mkdir();
	  	}        

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        fileTraceLog = new FileHandler(logFileName + ParamsObject.PARAM_LOG_EXTENSIONS);
        //fileHTML = new FileHandler(logFileName + ParamsObject.PARAM_HTML_EXTENSIONS);

        // create a TXT formatter
        formatterLog = new MyTraceLogFormatter();
        fileTraceLog.setFormatter(formatterLog);
        logger.addHandler(fileTraceLog);

        // create an HTML formatter
        /*
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
        */
    }
    
    static public void doMyLog(String myLogFileNamePath, String myLogRecord) {
    	BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(myLogFileNamePath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			String myData = Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS, getCurrLogDate())
					+ " : " +
					myLogRecord
					+ ParamsObject.PARAM_NEW_LINE;
			
			bw.write(myData);
			//System.out.print(myData);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
    
    static public void doLogger(Level level, String traceLog, String myLog) throws IOException, Throwable{
    	setCurrLogDate(Calendar.getInstance().getTime());
    	//TRACE_LOG
    	if(Main.configurationObject.getTracelog().toUpperCase().equals(ParamsObject.PARAM_TRACE_LOG) ){
    		if (level != null || !traceLog.equals("")){
		        if(level == Level.INFO){
			        LOGGER.info(traceLog);		        	
		        }else if(level == Level.SEVERE){
			        LOGGER.severe(traceLog);		        	
		        }else if(level == Level.WARNING){
			        LOGGER.warning(traceLog);		        	
		        }
	        }
    	}
    	//ENABLE_LOG each test script
    	if(Main.configurationObject.getEnablelog().toUpperCase().equals(ParamsObject.PARAM_ENABLE_LOG)){
    		if(!myLog.equals("")){
    			doMyLog(Main.configurationObject.getString_currentLogPath(), myLog);  			
    		}
    	}
    }

	public static Date getCurrLogDate() {
		return currLogDate;
	}

	public static void setCurrLogDate(Date currLogDate) {
		Log.currLogDate = currLogDate;
	}
}

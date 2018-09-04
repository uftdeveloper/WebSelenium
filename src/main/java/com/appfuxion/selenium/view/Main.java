package com.appfuxion.selenium.view;

import java.io.IOException;
import java.util.logging.Level;

import org.ini4j.InvalidFileFormatException;

import com.appfuxion.selenium.controller.About;
import com.appfuxion.selenium.controller.AppiumAndroidAutomation;
import com.appfuxion.selenium.controller.Configuration;
import com.appfuxion.selenium.controller.Log;
import com.appfuxion.selenium.controller.TestScript;
import com.appfuxion.selenium.controller.Util;
import com.appfuxion.selenium.controller.WebAutomation;
import com.appfuxion.selenium.model.ConfigurationObject;
import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;

public class Main {

	public static ConfigurationObject configurationObject = null;
	public static MainDialog mainDialog;
	/*
	private static String[] testArgs1 = {
			"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\2. Agency_FF_iBig v0.2.xlsx",
			"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\Test.xlsx",
			"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\IPP_CMFFSI_v3.6c.xlsx" };

	private static String[] testArgs2 = { "C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\IPP_CMFFSI_v3.6c.xlsx" };

	private static String[] testArgs3 = { "about" };
	*/
	
	//private static String[] testArgs4 = {"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\TestVerifyXlsDocxTxt.xlsx","C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\TestVerifyXlsDocxTxt_2.xlsx"};	
	//private static String[] testArgs4 = {"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\TestVerifyXlsDocxTxt.xlsx"};	
	//private static String[] testArgs4 = {"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\Test.xlsx"};//,"C:\\Wihemdra\\Project\\APP\\04 DEVELOPMENT\\TestScript\\TestSel25IFrame.xlsx"};	
	
	private static void initMain() throws IOException, Throwable {
		initiateConfig();
		initiateLog();
		Log.doLogger(Level.INFO, MessageObject.MSG0002, "");
	}

	private static void initiateConfig() throws InvalidFileFormatException,
			IOException {
		configurationObject = (new Configuration()).readConfig();
		configurationObject
				.setWebtracelogfilename(configurationObject
						.getWebtracelogfilename()
						+ Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYYMMDDHHMMSS));
	}

	private static void initiateLog() throws Throwable {
		try {
			Log.setup(configurationObject.getWebtracelogfilename());
			Log.doLogger(Level.INFO, MessageObject.MSG0004, "");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(MessageObject.MSG0001);
		}
	}
	
	public static void seleniumMain(String[] args) throws IOException, Throwable{
		initMain();
		TestScript tS = new TestScript();
		Log.doLogger(Level.INFO, MessageObject.MSG0006 + " - " + args.length
				+ ParamsObject.PARAM_UNIT_TOTAL_TS_FILE, "");
		tS.prepareTestScript(args);
		
		Log.doLogger(Level.INFO, MessageObject.MSG0014, "");
		Main.mainDialog.getProgressLabel().setText(ParamsObject.PARAM_GUI_RUN_TESTSCRIPT_LABEL);
		Main.mainDialog.getProgressBar().setMinimum(0);
		Main.mainDialog.getProgressBar().setMaximum(tS.getFinalTestScripts().size() - 1);
		Main.mainDialog.getTotalLabel().setText(ParamsObject.PARAM_GUI_TOTAL_LABEL + tS.getFinalTestScripts().size());
		Main.configurationObject.setPassed(0);
		Main.configurationObject.setFailed(0);
		
		for(int i =0; i < tS.getFinalTestScripts().size(); i++){
			if(tS.getFinalTestScripts().get(i).get(0).getDriver()!=null && tS.getFinalTestScripts().get(i).get(0).getDriver().toLowerCase().equals(ParamsObject.DRV_APPIUM_ANDROID.toLowerCase())){			
				AppiumAndroidAutomation aa = new AppiumAndroidAutomation();
				aa.runSingleAppiumAndroidAutomation(tS.getFinalTestScripts().get(i), i+1, tS.getFinalTestScripts().size());				
			}
			else{
				WebAutomation wa = new WebAutomation();
				wa.runSingleWebAutomation(tS.getFinalTestScripts().get(i), i+1, tS.getFinalTestScripts().size());
			}
			Main.mainDialog.getProgressBar().setValue(i);		
		}
		
		Log.doLogger(Level.INFO, MessageObject.MSG0015, "");		
		Log.doLogger(Level.INFO, MessageObject.MSG0003, "");		
		System.exit(0);
	}
	
	public static void main(String[] args) throws Throwable {
		//args = testArgs4;
		new About(args);
		mainDialog = new MainDialog();
		seleniumMain(args);
	}
}

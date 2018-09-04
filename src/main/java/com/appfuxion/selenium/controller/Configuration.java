package com.appfuxion.selenium.controller;

import java.io.File;
import java.io.IOException;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.appfuxion.selenium.model.ConfigurationObject;
import com.appfuxion.selenium.model.ParamsObject;

public class Configuration {
	
	public ConfigurationObject readConfig() throws InvalidFileFormatException, IOException{
		ConfigurationObject configurationObject = new ConfigurationObject();
		Wini ini = new Wini(new File(configurationObject.getString_configpath()));
		configurationObject.setAbout(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_ABOUT) );
		configurationObject.setDefaultpath(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_DEFAULTPATH ));
		configurationObject.setRecursive(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_RECURSIVE ));
		configurationObject.setProjectname(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_PROJECT_NAME ));
		configurationObject.setPlatform(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_PLATFORM ));
		configurationObject.setWaittime(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_WAIT_TIME));
		configurationObject.setSynctimeout(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_SYNC_TIMEOUT));
		configurationObject.setOutputpath(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_OUTPUT_PATH));
		configurationObject.setTracelog(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_TRACE_LOG));
		configurationObject.setGetnow(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_GET_NOW ));
		configurationObject.setTemplate_doc(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_TEMPLATE_DOC ));
		configurationObject.setTemplate_html(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_TEMPLATE_HTML ));		
		configurationObject.setHighlight_verify_xls(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_HIGHLIGHT_VERIFY_XLS ));		
		configurationObject.setClose_browser_after_run(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_CLOSE_BROWSER_AFTER_RUN ));		
		configurationObject.setWaitobject(ini.get(ParamsObject.CONFIG_General, ParamsObject.CONFIG_WAIT_OBJECT ));
		configurationObject.setWeb(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_WEB));
		configurationObject.setMobile(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_MOBILE));
		configurationObject.setWin(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_WIN));
		configurationObject.setTe(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_TE));
		configurationObject.setUia(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_UIA));
		configurationObject.setVb(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_VB));
		configurationObject.setNet(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_NET));
		configurationObject.setSap(ini.get(ParamsObject.CONFIG_Addins, ParamsObject.CONFIG_SAP));
		configurationObject.setWord(ini.get(ParamsObject.CONFIG_Report, ParamsObject.CONFIG_WORD));
		configurationObject.setPdf(ini.get(ParamsObject.CONFIG_Report, ParamsObject.CONFIG_PDF));
		configurationObject.setExcel(ini.get(ParamsObject.CONFIG_Report, ParamsObject.CONFIG_EXCEL));
		configurationObject.setImage(ini.get(ParamsObject.CONFIG_Report, ParamsObject.CONFIG_IMAGE ));
		configurationObject.setAs400makerid(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_AS400_Maker_ID ));
		configurationObject.setAs400makerpass(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_AS400_Maker_Pass ));
		configurationObject.setAs400path(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_AS400_PATH ));
		configurationObject.setPackagename(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_Package_Name ));
		configurationObject.setCapturedevice(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_CAPTURE_DEVICE ));
		configurationObject.setEnablelog(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_ENABLE_LOG ));
		configurationObject.setForcescreenshot(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_FORCE_SCREENSHOT ));
		configurationObject.setClosebrowser(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_Close_Browser));
		configurationObject.setFieldnotexist(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_FieldNotExist ));
		configurationObject.setImportsheet(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_IMPORT_SHEET ));
		configurationObject.setMessagechecker(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_MessageChecker ));
		configurationObject.setMobilelaunchmethod(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_MobileLaunchMethod ));
		configurationObject.setPdftotextpath(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_PDFTOTEXT_PATH ));
		configurationObject.setReportpath(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_REPORT_PATH ));
		configurationObject.setSmspathfile(ini.get(ParamsObject.CONFIG_Environment, ParamsObject.CONFIG_SMS_Path_File ));
		configurationObject.setString_templateDocPath(configurationObject.getTemplate_doc());
		return configurationObject;
	}
}

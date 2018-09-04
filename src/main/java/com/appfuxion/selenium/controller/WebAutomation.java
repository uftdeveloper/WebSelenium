package com.appfuxion.selenium.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

import com.appfuxion.selenium.model.FinalTestScriptObject;
import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;
import com.appfuxion.selenium.model.WebObject;
import com.appfuxion.selenium.view.Main;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;

public class WebAutomation {
	
	private WebObject wo;

	public WebAutomation(){
		
	}
	
	public boolean waitForJSandJQueryToLoad() {

	    WebDriverWait wait = new WebDriverWait(wo.getWd(), 30);

	    // wait for jQuery to load
	    ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver) {
	        try {
	          return ((Long)((JavascriptExecutor) wo.getWd()).executeScript("return jQuery.active") == 0);
	        }
	        catch (Exception e) {
	          // no jQuery present
	          return true;
	        }
	      }
	    };

	    // wait for Javascript to load
	    ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver) {
	        return ((JavascriptExecutor)wo.getWd()).executeScript("return document.readyState")
	        .toString().equals("complete");
	      }
	    };

	  return wait.until(jQueryLoad) && wait.until(jsLoad);
	}	
	
	@SuppressWarnings("deprecation")
	public void selectDriverAndNavigate(ArrayList<FinalTestScriptObject> fts) throws NumberFormatException, InterruptedException{
		wo = new WebObject();
		File f;
		if(fts.get(0).getApplicationPath()==null) {
			
			return;// skip if there is no application path
		}
		if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_IE)){
				
		        f = new File(ParamsObject.PARAM_IE_EXE_DRIVER_32);		 
		        
		        System.setProperty(ParamsObject.PARAM_IE_SYSTEM_PROPERTIES, f.getAbsolutePath());	
		        
		        DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		        cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		        cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		        //cap.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
		        
		        wo.setWd(new InternetExplorerDriver(cap));
		        wo.getWd().get(fts.get(0).getApplicationPath());
		        waitForJSandJQueryToLoad();
		        wo.getWd().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		        try{
			        wo.getWd().manage().window().maximize();		        	
		        }catch(Exception ex){
		        }
		        Thread.sleep(Integer.parseInt(Main.configurationObject.getSynctimeout()));
		        
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_FIREFOX)){				

		        f = new File(ParamsObject.PARAM_FIREFOX_EXE_DRIVER_32);		 
		        System.setProperty(ParamsObject.PARAM_FIREFOX_SYSTEM_PROPERTIES, f.getAbsolutePath());				
		        
		        DesiredCapabilities cap = DesiredCapabilities.firefox();
		        //cap.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
		        
		        wo.setWd(new FirefoxDriver(cap));
		        wo.getWd().get(fts.get(0).getApplicationPath());
		        waitForJSandJQueryToLoad();
		        wo.getWd().manage().window().maximize();
		        Thread.sleep(Integer.parseInt(Main.configurationObject.getSynctimeout()));
				
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_CHROME)){	
				

		        f = new File(ParamsObject.PARAM_CHROME_EXE_DRIVER);		 
		        System.setProperty(ParamsObject.PARAM_CHROME_SYSTEM_PROPERTIES, f.getAbsolutePath());				
		        DesiredCapabilities cap = DesiredCapabilities.chrome();
		        //cap.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
		        wo.setWd(new ChromeDriver(cap));
		        wo.getWd().get(fts.get(0).getApplicationPath());
		        waitForJSandJQueryToLoad();
		        wo.getWd().manage().window().maximize();
		        Thread.sleep(Integer.parseInt(Main.configurationObject.getSynctimeout()));
		        
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_EDGE)){	

			

		        f = new File(ParamsObject.PARAM_EDGE_EXE_DRIVER);		 
		        System.setProperty(ParamsObject.PARAM_EDGE_SYSTEM_PROPERTIES, f.getAbsolutePath());				
		        DesiredCapabilities cap = DesiredCapabilities.edge();
		        //cap.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
		        wo.setWd(new EdgeDriver(cap));
		        wo.getWd().get(fts.get(0).getApplicationPath());
		        waitForJSandJQueryToLoad();
		        wo.getWd().manage().window().maximize();
		        Thread.sleep(Integer.parseInt(Main.configurationObject.getSynctimeout()));
		        
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_SAFARI)){

			//only applicable for Safari 10 for MAC

		        wo.setWd(new SafariDriver());
		        wo.getWd().get(fts.get(0).getApplicationPath());
		        waitForJSandJQueryToLoad();
		        wo.getWd().manage().window().maximize();
		        Thread.sleep(Integer.parseInt(Main.configurationObject.getSynctimeout()));
				
		}
	}
	
	public void prepareFolderReport(FinalTestScriptObject fts) throws IOException, Throwable{
		try{
			new File(fts.getFolderDoc()).mkdirs();
			new File(fts.getFolderHtm()).mkdirs();
			new File(fts.getFolderLogs()).mkdirs();
			new File(fts.getFolderPdf()).mkdirs();
			new File(fts.getFolderRpt()).mkdirs();
			new File(fts.getFolderSst()).mkdirs();
			new File(fts.getFolderXls()).mkdirs();
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");			
		}
	}
	
	public void captureScreenShot(String imgFile, String isTakeScreenshot) throws IOException, Throwable{
		if(wo.getWd()!=null){
			if(isTakeScreenshot!=null && isTakeScreenshot.trim().toLowerCase().trim().equals(ParamsObject.PARAM_FULL)){
				try{
					Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingStrategy(1000)).takeScreenshot(wo.getWd());
					ImageIO.write(screenshot.getImage(), ParamsObject.PARAM_PNG, new File(imgFile));
					
				}catch(Exception ex){
					Log.doLogger(Level.SEVERE, ex.getMessage(), "");					
				}
			}
			else if(Main.configurationObject.getForcescreenshot().trim().toUpperCase().equals(ParamsObject.PARAM_YES)){
				File src=((TakesScreenshot)wo.getWd()).getScreenshotAs(OutputType.FILE);           
				try {
					FileUtils.copyFile(src, new File(imgFile));    
				} catch (Exception ex){
					Log.doLogger(Level.SEVERE, ex.getMessage(), "");
				}
			}else{
				if(isTakeScreenshot!=null && isTakeScreenshot.trim().toUpperCase().equals(ParamsObject.PARAM_Y) ){
					File src=((TakesScreenshot)wo.getWd()).getScreenshotAs(OutputType.FILE);           
					try {
						FileUtils.copyFile(src, new File(imgFile));    
					} catch (Exception ex){
						Log.doLogger(Level.SEVERE, ex.getMessage(), "");
					}			
				}
			}
		}
	}
	
	public String referenceRptHeaderReport(ArrayList<FinalTestScriptObject> fts){
		String refRptValue = "";
		for(int r= 0; r < fts.size(); r++){
			if(fts.get(r).getReferenceReport()!=null && !fts.get(r).getReferenceReport().equals("")){
				refRptValue += ParamsObject.PARAM_NEW_LINE
						+ ParamsObject.PARAM_REF + fts.get(r).getTestData() + "="
						+ fts.get(r).getReferenceReport();
			}
		}
		
		return refRptValue;	
	}
	
	public String getSummaryStatus(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		try{
			String status = ParamsObject.PARAM_HTM_PASSED_LABEL;
			for(int gss = 0; gss < fts.size(); gss++){
				if(fts.get(gss).getStatus()!=null && fts.get(gss).getStatus().equals(ParamsObject.PARAM_REPORT_FAILED)){
					status = ParamsObject.PARAM_HTM_FAILED_LABEL;
					break;
				}
			}
			return status;
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");
			return "";
		}
	}
	
	public void generateReportRpt(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0029, MessageObject.MSG0029);
    	BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(fts.get(0).getFolderRpt() + "//" + ParamsObject.CONFIG_REPORT_RPT_NAME);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);

			String thisData = ParamsObject.CONFIG_REPORT_RPT_REPORT
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_TESTSCRIPTID + fts.get(0).getTestCaseID()
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_PROJECTNAME + fts.get(0).getProjectName()
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_EXEC_STATUS + getSummaryStatus(fts)
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_PATH + fts.get(0).getReportPath()
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_DATE + Util.getCurrentDateFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_MM_DD_YYYY)
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_START_TIME + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_HH_MM_SS)
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_END_TIME + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(fts.size()-1).getEndTime(), ParamsObject.PARAM_DATE_HH_MM_SS)
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_DURATION + Util.getDurationFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), fts.get(fts.size()-1).getEndTime())
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_PLATFORM + fts.get(0).getPlatform()
					+ ParamsObject.PARAM_NEW_LINE
					+ ParamsObject.CONFIG_REPORT_RPT_VERSION + fts.get(0).getAppVersion()
					+ referenceRptHeaderReport(fts);
					;
			
			bw.write(thisData);
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
	
	public String checkString(String pStr){
		if(pStr == null ) pStr = "";
		return pStr;
	}
	
	public void generateReportXls(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0030, MessageObject.MSG0030);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheetGlobal = workbook.createSheet(ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL);
        Object[][] datatypesGlobal = {
                {ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_LABEL, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_VALUE, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_START_TIME, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_OUTPUT_PATH},
                {ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_PROJECT_NAME, fts.get(0).getProjectName(), fts.get(0).getStartTime(), fts.get(0).getOutputPath()},
                {ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_TEST_SCRIPT_ID, fts.get(0).getTestCaseID(), "",""},
                {ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_PLATFORM, fts.get(0).getPlatform(), "",""},
                {ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_APP_VERSION, fts.get(0).getAppVersion(), "", ""}
        };

        int rowNumGlobal = 0;

        for (Object[] datatype : datatypesGlobal) {
            XSSFRow rowGlobal = sheetGlobal.createRow(rowNumGlobal++);
            int colNumGlobal = 0;
            for (Object field : datatype) {
                XSSFCell cell = rowGlobal.createCell(colNumGlobal++);
                cell.setCellValue((String) field);
            }
        }
        
        XSSFSheet sheetAction1 = workbook.createSheet(ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1);
        int rowNumAction1 = 0;
        int colNumAction1 = 0;
        XSSFRow rowAction1 = sheetAction1.createRow(rowNumAction1++);
        //Create Header for Sheet Action1
        XSSFCell cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_STEP_NAME);
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_TYPE);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_ID);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_FRAME);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_ACTION);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_TEST_DATA);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_KEYWORD);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_APPLICATION_PATH);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_DRIVER);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_ENVIRONMENT);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_DESCRIPTION);        
        cellAction1 = rowAction1.createCell(colNumAction1++);
        cellAction1.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_EXPECTED_RESULT);        
        
        XSSFSheet sheetReport = workbook.createSheet(ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT);
        int rowNumReport = 0;
        int colNumReport = 0;
        XSSFRow rowReport = sheetReport.createRow(rowNumReport++);
        //Create Header for Sheet Report
        XSSFCell cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_NO);
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TESTSTEP);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TESTDATA);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_EXPECTEDRESULT);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_KEYWORD);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_STATUS);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TIME);        
        cellReport = rowReport.createCell(colNumReport++);
        cellReport.setCellValue((String) ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_INDEX);                
        
        for(int z=0; z < fts.size(); z++){
        	
        	if(!fts.get(z).isWriteToReport()) continue;
        	
        	rowAction1 = sheetAction1.createRow(rowNumAction1++);
        	colNumAction1 = 0;
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getStepName()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_STEP_NAME);
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getObjectType()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_TYPE);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getObjectID()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_ID);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getObjectFrame()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_OBJECT_FRAME);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getAction()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_ACTION);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getTestData()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_TEST_DATA);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getKeyword()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_KEYWORD);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getApplicationPath()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_APPLICATION_PATH);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getDriver()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_DRIVER);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getEnvironment()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_ENVIRONMENT);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getDescription()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_DESCRIPTION);        
            cellAction1 = rowAction1.createCell(colNumAction1++);
            cellAction1.setCellValue((String) checkString(fts.get(z).getExpectedResult()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_ACTION1_EXPECTED_RESULT);        
            
            rowReport = sheetReport.createRow(rowNumReport++);
            colNumReport = 0;
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getNo()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_NO);
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getStepName()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TESTSTEP);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getTestData()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TESTDATA);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getExpectedResult()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_EXPECTEDRESULT);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getKeyword()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_KEYWORD);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getStatus()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_STATUS);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getEndTime()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_TIME);        
            cellReport = rowReport.createCell(colNumReport++);
            cellReport.setCellValue((String) checkString(fts.get(z).getIndex()));//ParamsObject.CONFIG_REPORT_XLS_SHEET_REPORT_INDEX);  
        }

        try {
            OutputStream outputStream = new FileOutputStream(fts.get(0).getFolderXls() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_XLS_EXTENSIONS);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public void generateReportHtm(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0031, MessageObject.MSG0031);
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(fts.get(0).getFolderHtm() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_HTML_EXTENSIONS);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);
			
			StringBuilder htmData = new StringBuilder();
			
			String strColorNavigation = "#222";
			if(Main.configurationObject.getTemplate_html().toLowerCase().trim().equals(ParamsObject.PARAM_TEMPLATE_NH2)) strColorNavigation = "white";
			
			htmData.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><meta name=\"description\" content=\"\"><meta name=\"author\" content=\"\"><title>Test Execution Summary</title>");
			htmData.append("<script type='text/javascript'>    function expand_collapse(id) {       var e = document.getElementById(id);       var f = document.getElementById(id+'_arrows');       if(e.style.display == 'none'){          e.style.display = 'block';          f.innerHTML = '&#9650';       }       else {          e.style.display = 'none';          f.innerHTML = '&#9660';       }    }</script>");
			htmData.append("<Style> .arrows{text-decoration:none;color:silver;}   .col-sm-4{word-wrap: break-word; }.label{white-space: pre-wrap !Important;white-space: -moz-pre-wrap;  white-space: -o-pre-wrap;word-wrap: break-word;  }.panel-heading strong{font-size:16px;}.table a{text-decoration: underline;}.navbar-header img{padding:10px 0;}.panel .panel-body .col-sm-4{padding:3px 0;}.label,sub,sup{vertical-align:baseline}hr,img{border:0}body,figure{margin:0}.btn-group>.btn-group,.btn-toolbar .btn,.btn-toolbar .btn-group,.btn-toolbar .input-group,.col-xs-1,.col-xs-10,.col-xs-11,.col-xs-12,.col-xs-2,.col-xs-3,.col-xs-4,.col-xs-5,.col-xs-6,.col-xs-7,.col-xs-8,.col-xs-9,");
			htmData.append(".dropdown-menu{float:left}.navbar-fixed-bottom .navbar-collapse,.navbar-fixed-top .navbar-collapse,.pre-scrollable{max-height:340px}body{padding-top:50px;");
			htmData.append("padding-bottom:20px}html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}article,aside,details,figcaption,figure,footer,");
			htmData.append("header,hgroup,main,menu,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block;vertical-align:baseline}audio:not([controls])");
			htmData.append("{display:none;height:0}[hidden],template{display:none}a{background-color:transparent}a:active,a:hover{outline:0}b,optgroup,strong{font-weight:700}");
			htmData.append("dfn{font-style:italic}h1{margin:.67em 0}mark{color:#000;background:#ff0}sub,sup{position:relative;font-size:75%;line-height:0}sup{top:-.5em}sub{bottom:-.25em}");
			htmData.append("img{vertical-align:middle}svg:not(:root){overflow:hidden}hr{height:0;-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box}");
			htmData.append("*,:after,:before,input[type=checkbox],input[type=radio]{-webkit-box-sizing:border-box;-moz-box-sizing:border-box}pre,textarea{overflow:auto}");
			htmData.append("code,kbd,pre,samp{font-size:1em}button,input,optgroup,select,textarea{margin:0;font:inherit;color:inherit}.glyphicon,address{font-style:normal}");
			htmData.append("button{overflow:visible}button,select{text-transform:none}button,html input[type=button],input[type=reset],input[type=submit]{-webkit-appearance:button;cursor");
			htmData.append(":pointer}button[disabled],html input[disabled]{cursor:default}button::-moz-focus-inner,input::-moz-focus-inner{padding:0;border:0}input[type=checkbox]");
			htmData.append(",input[type=radio]{box-sizing:border-box;padding:0}input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{height:auto}");
			htmData.append("input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}table{border-spacing:0;border-collapse:");
			htmData.append("collapse}td,th{padding:0}/*! Source: https://github.com/h5bp/html5-boilerplate/blob/master/src/css/main.css */@media print{blockquote,img,pre,tr{page-break-in");
			htmData.append("side:avoid}*,:after,:before{color:#000!important;text-shadow:none!important;background:0 0!important;-webkit-box-shadow:none!important;box-shadow:none!importa");
			htmData.append("nt}a,a:visited{text-decoration:underline}a[href]:after{content:' (' attr(href) ')'}abbr[title]:after{content:' (' attr(title) ')'}a[href^='javascript:']:after,");
			htmData.append("a[href^='#']:after{content:''}blockquote,pre{border:1px solid #999}thead{display:table-header-group}img{max-width:100%!important}h2,h3,p{orphans:3;widows:3}h2,");
			htmData.append("h3{page-break-after:avoid}.navbar{display:none}.btn>.caret,.dropup>.btn>.caret{border-top-color:#000!important}.label{border:1px solid #000}.table{border-colla");
			htmData.append("pse:collapse!important}.table td,.table th{background-color:#fff!important}.table-bordered td,.table-bordered th{border:1px solid #ddd!important}}.dropdown-men");
			htmData.append("u,.modal-content{-webkit-background-clip:padding-box}.btn,.btn-danger.active,.btn-danger:active,.btn-default.active,.btn-default:active,.btn-info.active,.btn-in");
			htmData.append("fo:active,.btn-primary.active,.btn-primary:active,.btn-warning.active,.btn-warning:active,.btn.active,.btn:active,.dropdown-menu>.disabled>a:focus,.dropdown-men");
			htmData.append("u>.disabled>a:hover,.form-control,.navbar-toggle,.open>.dropdown-toggle.btn-danger,.open>.dropdown-toggle.btn-default,.open>.dropdown-toggle.btn-info,.open>.dro");
			htmData.append("pdown-toggle.btn-primary,.open>.dropdown-toggle.btn-warning{background-image:none}.img-thumbnail,body{background-color:#fff}@font-face{font-family:'Glyphicons H");
			htmData.append("alflings';src:url(../fonts/glyphicons-halflings-regular.eot);src:url(../fonts/glyphicons-halflings-regular.eot?#iefix) format('embedded-opentype'),url(../fonts/");
			htmData.append("glyphicons-halflings-regular.woff2) format('woff2'),url(../fonts/glyphicons-halflings-regular.woff) format('woff'),url(../fonts/glyphicons-halflings-regular.ttf");
			htmData.append(") format('truetype'),url(../fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular) format('svg')}.glyphicon{position:relative;top:1px;display:inlin");
			htmData.append("e-block;font-family:'Glyphicons Halflings';font-weight:400;line-height:1;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}.glyphicon-asteris");
			htmData.append("k:before{content:'\\002a'}.glyphicon-plus:before{content:'\\002b'}.glyphicon-eur:before,.glyphicon-euro:before{content:'\\20ac'}.glyphicon-minus:before{content:'\\2");
			htmData.append("212'}.glyphicon-cloud:before{content:'\\2601'}.glyphicon-envelope:before{content:'\\2709'}.glyphicon-pencil:before{content:'\\270f'}.glyphicon-glass:before{content");
			htmData.append(":'\\e001'}.glyphicon-music:before{content:'\\e002'}.glyphicon-search:before{content:'\\e003'}.glyphicon-heart:before{content:'\\e005'}.glyphicon-star:before{content");
			htmData.append(":'\\e006'}.glyphicon-star-empty:before{content:'\\e007'}.glyphicon-user:before{content:'\\e008'}.glyphicon-film:before{content:'\\e009'}.glyphicon-th-large:before{c");
			htmData.append("ontent:'\\e010'}.glyphicon-th:before{content:'\\e011'}.glyphicon-th-list:before{content:'\\e012'}.glyphicon-ok:before{content:'\\e013'}.glyphicon-remove:before{cont");
			htmData.append("ent:'\\e014'}.glyphicon-zoom-in:before{content:'\\e015'}.glyphicon-zoom-out:before{content:'\\e016'}.glyphicon-off:before{content:'\\e017'}.glyphicon-signal:before{");
			htmData.append("content:'\\e018'}.glyphicon-cog:before{content:'\\e019'}.glyphicon-trash:before{content:'\\e020'}.glyphicon-home:before{content:'\\e021'}.glyphicon-file:before{cont");
			htmData.append("ent:'\\e022'}.glyphicon-time:before{content:'\\e023'}.glyphicon-road:before{content:'\\e024'}.glyphicon-download-alt:before{content:'\\e025'}.glyphicon-download:bef");
			htmData.append("ore{content:'\\e026'}.glyphicon-upload:before{content:'\\e027'}.glyphicon-inbox:before{content:'\\e028'}.glyphicon-play-circle:before{content:'\\e029'}.glyphicon-re");
			htmData.append("peat:before{content:'\\e030'}.glyphicon-refresh:before{content:'\\e031'}.glyphicon-list-alt:before{content:'\\e032'}.glyphicon-lock:before{content:'\\e033'}.glyphic");
			htmData.append("on-flag:before{content:'\\e034'}.glyphicon-headphones:before{content:'\\e035'}.glyphicon-volume-off:before{content:'\\e036'}.glyphicon-volume-down:before{content:'");
			htmData.append("\\e037'}.glyphicon-volume-up:before{content:'\\e038'}.glyphicon-qrcode:before{content:'\\e039'}.glyphicon-barcode:before{content:'\\e040'}.glyphicon-tag:before{cont");
			htmData.append("ent:'\\e041'}.glyphicon-tags:before{content:'\\e042'}.glyphicon-book:before{content:'\\e043'}.glyphicon-bookmark:before{content:'\\e044'}.glyphicon-print:before{con");
			htmData.append("tent:'\\e045'}.glyphicon-camera:before{content:'\\e046'}.glyphicon-font:before{content:'\\e047'}.glyphicon-bold:before{content:'\\e048'}.glyphicon-italic:before{con");
			htmData.append("tent:'\\e049'}.glyphicon-text-height:before{content:'\\e050'}.glyphicon-text-width:before{content:'\\e051'}.glyphicon-align-left:before{content:'\\e052'}.glyphicon-");
			htmData.append("align-center:before{content:'\\e053'}.glyphicon-align-right:before{content:'\\e054'}.glyphicon-align-justify:before{content:'\\e055'}.glyphicon-list:before{content");
			htmData.append(":'\\e056'}.glyphicon-indent-left:before{content:'\\e057'}.glyphicon-indent-right:before{content:'\\e058'}.glyphicon-facetime-video:before{content:'\\e059'}.glyphico");
			htmData.append("n-picture:before{content:'\\e060'}.glyphicon-map-marker:before{content:'\\e062'}.glyphicon-adjust:before{content:'\\e063'}.glyphicon-tint:before{content:'\\e064'}.g");
			htmData.append("lyphicon-edit:before{content:'\\e065'}.glyphicon-share:before{content:'\\e066'}.glyphicon-check:before{content:'\\e067'}.glyphicon-move:before{content:'\\e068'}.gly");
			htmData.append("phicon-step-backward:before{content:'\\e069'}.glyphicon-fast-backward:before{content:'\\e070'}.glyphicon-backward:before{content:'\\e071'}.glyphicon-play:before{co");
			htmData.append("ntent:'\\e072'}.glyphicon-pause:before{content:'\\e073'}.glyphicon-stop:before{content:'\\e074'}.glyphicon-forward:before{content:'\\e075'}.glyphicon-fast-forward:b");
			htmData.append("efore{content:'\\e076'}.glyphicon-step-forward:before{content:'\\e077'}.glyphicon-eject:before{content:'\\e078'}.glyphicon-chevron-left:before{content:'\\e079'}.gly");
			htmData.append("phicon-chevron-right:before{content:'\\e080'}.glyphicon-plus-sign:before{content:'\\e081'}.glyphicon-minus-sign:before{content:'\\e082'}.glyphicon-remove-sign:befo");
			htmData.append("re{content:'\\e083'}.glyphicon-ok-sign:before{content:'\\e084'}.glyphicon-question-sign:before{content:'\\e085'}.glyphicon-info-sign:before{content:'\\e086'}.glyphi");
			htmData.append("con-screenshot:before{content:'\\e087'}.glyphicon-remove-circle:before{content:'\\e088'}.glyphicon-ok-circle:before{content:'\\e089'}.glyphicon-ban-circle:before{c");
			htmData.append("ontent:'\\e090'}.glyphicon-arrow-left:before{content:'\\e091'}.glyphicon-arrow-right:before{content:'\\e092'}.glyphicon-arrow-up:before{content:'\\e093'}.glyphicon-");
			htmData.append("arrow-down:before{content:'\\e094'}.glyphicon-share-alt:before{content:'\\e095'}.glyphicon-resize-full:before{content:'\\e096'}.glyphicon-resize-small:before{conte");
			htmData.append("nt:'\\e097'}.glyphicon-exclamation-sign:before{content:'\\e101'}.glyphicon-gift:before{content:'\\e102'}.glyphicon-leaf:before{content:'\\e103'}.glyphicon-fire:befo");
			htmData.append("re{content:'\\e104'}.glyphicon-eye-open:before{content:'\\e105'}.glyphicon-eye-close:before{content:'\\e106'}.glyphicon-warning-sign:before{content:'\\e107'}.glyphi");
			htmData.append("con-plane:before{content:'\\e108'}.glyphicon-calendar:before{content:'\\e109'}.glyphicon-random:before{content:'\\e110'}.glyphicon-comment:before{content:'\\e111'}.");
			htmData.append("glyphicon-magnet:before{content:'\\e112'}.glyphicon-chevron-up:before{content:'\\e113'}.glyphicon-chevron-down:before{content:'\\e114'}.glyphicon-retweet:before{co");
			htmData.append("ntent:'\\e115'}.glyphicon-shopping-cart:before{content:'\\e116'}.glyphicon-folder-close:before{content:'\\e117'}.glyphicon-folder-open:before{content:'\\e118'}.glyp");
			htmData.append("hicon-resize-vertical:before{content:'\\e119'}.glyphicon-resize-horizontal:before{content:'\\e120'}.glyphicon-hdd:before{content:'\\e121'}.glyphicon-bullhorn:befor");
			htmData.append("e{content:'\\e122'}.glyphicon-bell:before{content:'\\e123'}.glyphicon-certificate:before{content:'\\e124'}.glyphicon-thumbs-up:before{content:'\\e125'}.glyphicon-th");
			htmData.append("umbs-down:before{content:'\\e126'}.glyphicon-hand-right:before{content:'\\e127'}.glyphicon-hand-left:before{content:'\\e128'}.glyphicon-hand-up:before{content:'\\e1");
			htmData.append("29'}.glyphicon-hand-down:before{content:'\\e130'}.glyphicon-circle-arrow-right:before{content:'\\e131'}.glyphicon-circle-arrow-left:before{content:'\\e132'}.glyphi");
			htmData.append("con-circle-arrow-up:before{content:'\\e133'}.glyphicon-circle-arrow-down:before{content:'\\e134'}.glyphicon-globe:before{content:'\\e135'}.glyphicon-wrench:before{");
			htmData.append("content:'\\e136'}.glyphicon-tasks:before{content:'\\e137'}.glyphicon-filter:before{content:'\\e138'}.glyphicon-briefcase:before{content:'\\e139'}.glyphicon-fullscre");
			htmData.append("en:before{content:'\\e140'}.glyphicon-dashboard:before{content:'\\e141'}.glyphicon-paperclip:before{content:'\\e142'}.glyphicon-heart-empty:before{content:'\\e143'}");
			htmData.append(".glyphicon-link:before{content:'\\e144'}.glyphicon-phone:before{content:'\\e145'}.glyphicon-pushpin:before{content:'\\e146'}.glyphicon-usd:before{content:'\\e148'}.");
			htmData.append("glyphicon-gbp:before{content:'\\e149'}.glyphicon-sort:before{content:'\\e150'}.glyphicon-sort-by-alphabet:before{content:'\\e151'}.glyphicon-sort-by-alphabet-alt:b");
			htmData.append("efore{content:'\\e152'}.glyphicon-sort-by-order:before{content:'\\e153'}.glyphicon-sort-by-order-alt:before{content:'\\e154'}.glyphicon-sort-by-attributes:before{c");
			htmData.append("ontent:'\\e155'}.glyphicon-sort-by-attributes-alt:before{content:'\\e156'}.glyphicon-unchecked:before{content:'\\e157'}.glyphicon-expand:before{content:'\\e158'}.gl");
			htmData.append("yphicon-collapse-down:before{content:'\\e159'}.glyphicon-collapse-up:before{content:'\\e160'}.glyphicon-log-in:before{content:'\\e161'}.glyphicon-flash:before{cont");
			htmData.append("ent:'\\e162'}.glyphicon-log-out:before{content:'\\e163'}.glyphicon-new-window:before{content:'\\e164'}.glyphicon-record:before{content:'\\e165'}.glyphicon-save:befo");
			htmData.append("re{content:'\\e166'}.glyphicon-open:before{content:'\\e167'}.glyphicon-saved:before{content:'\\e168'}.glyphicon-import:before{content:'\\e169'}.glyphicon-export:bef");
			htmData.append("ore{content:'\\e170'}.glyphicon-send:before{content:'\\e171'}.glyphicon-floppy-disk:before{content:'\\e172'}.glyphicon-floppy-saved:before{content:'\\e173'}.glyphic");
			htmData.append("on-floppy-remove:before{content:'\\e174'}.glyphicon-floppy-save:before{content:'\\e175'}.glyphicon-floppy-open:before{content:'\\e176'}.glyphicon-credit-card:befor");
			htmData.append("e{content:'\\e177'}.glyphicon-transfer:before{content:'\\e178'}.glyphicon-cutlery:before{content:'\\e179'}.glyphicon-header:before{content:'\\e180'}.glyphicon-compr");
			htmData.append("essed:before{content:'\\e181'}.glyphicon-earphone:before{content:'\\e182'}.glyphicon-phone-alt:before{content:'\\e183'}.glyphicon-tower:before{content:'\\e184'}.gly");
			htmData.append("phicon-stats:before{content:'\\e185'}.glyphicon-sd-video:before{content:'\\e186'}.glyphicon-hd-video:before{content:'\\e187'}.glyphicon-subtitles:before{content:'\\");
			htmData.append("e188'}.glyphicon-sound-stereo:before{content:'\\e189'}.glyphicon-sound-dolby:before{content:'\\e190'}.glyphicon-sound-5-1:before{content:'\\e191'}.glyphicon-sound-");
			htmData.append("6-1:before{content:'\\e192'}.glyphicon-sound-7-1:before{content:'\\e193'}.glyphicon-copyright-mark:before{content:'\\e194'}.glyphicon-registration-mark:before{cont");
			htmData.append("ent:'\\e195'}.glyphicon-cloud-download:before{content:'\\e197'}.glyphicon-cloud-upload:before{content:'\\e198'}.glyphicon-tree-conifer:before{content:'\\e199'}.glyp");
			htmData.append("hicon-tree-deciduous:before{content:'\\e200'}.glyphicon-cd:before{content:'\\e201'}.glyphicon-save-file:before{content:'\\e202'}.glyphicon-open-file:before{content");
			htmData.append(":'\\e203'}.glyphicon-level-up:before{content:'\\e204'}.glyphicon-copy:before{content:'\\e205'}.glyphicon-paste:before{content:'\\e206'}.glyphicon-alert:before{conte");
			htmData.append("nt:'\\e209'}.glyphicon-equalizer:before{content:'\\e210'}.glyphicon-king:before{content:'\\e211'}.glyphicon-queen:before{content:'\\e212'}.glyphicon-pawn:before{con");
			htmData.append("tent:'\\e213'}.glyphicon-bishop:before{content:'\\e214'}.glyphicon-knight:before{content:'\\e215'}.glyphicon-baby-formula:before{content:'\\e216'}.glyphicon-tent:be");
			htmData.append("fore{content:'\\26fa'}.glyphicon-blackboard:before{content:'\\e218'}.glyphicon-bed:before{content:'\\e219'}.glyphicon-apple:before{content:'\\f8ff'}.glyphicon-erase");
			htmData.append(":before{content:'\\e221'}.glyphicon-hourglass:before{content:'\\231b'}.glyphicon-lamp:before{content:'\\e223'}.glyphicon-duplicate:before{content:'\\e224'}.glyphico");
			htmData.append("n-piggy-bank:before{content:'\\e225'}.glyphicon-scissors:before{content:'\\e226'}.glyphicon-bitcoin:before,.glyphicon-btc:before,.glyphicon-xbt:before{content:'\\e");
			htmData.append("227'}.glyphicon-jpy:before,.glyphicon-yen:before{content:'\\00a5'}.glyphicon-rub:before,.glyphicon-ruble:before{content:'\\20bd'}.glyphicon-scale:before{content:'");
			htmData.append("\\e230'}.glyphicon-ice-lolly:before{content:'\\e231'}.glyphicon-ice-lolly-tasted:before{content:'\\e232'}.glyphicon-education:before{content:'\\e233'}.glyphicon-opt");
			htmData.append("ion-horizontal:before{content:'\\e234'}.glyphicon-option-vertical:before{content:'\\e235'}.glyphicon-menu-hamburger:before{content:'\\e236'}.glyphicon-modal-window");
			htmData.append(":before{content:'\\e237'}.glyphicon-oil:before{content:'\\e238'}.glyphicon-grain:before{content:'\\e239'}.glyphicon-sunglasses:before{content:'\\e240'}.glyphicon-te");
			htmData.append("xt-size:before{content:'\\e241'}.glyphicon-text-color:before{content:'\\e242'}.glyphicon-text-background:before{content:'\\e243'}.glyphicon-object-align-top:before");
			htmData.append("{content:'\\e244'}.glyphicon-object-align-bottom:before{content:'\\e245'}.glyphicon-object-align-horizontal:before{content:'\\e246'}.glyphicon-object-align-left:be");
			htmData.append("fore{content:'\\e247'}.glyphicon-object-align-vertical:before{content:'\\e248'}.glyphicon-object-align-right:before{content:'\\e249'}.glyphicon-triangle-right:befo");
			htmData.append("re{content:'\\e250'}.glyphicon-triangle-left:before{content:'\\e251'}.glyphicon-triangle-bottom:before{content:'\\e252'}.glyphicon-triangle-top:before{content:'\\e2");
			htmData.append("53'}.glyphicon-console:before{content:'\\e254'}.glyphicon-superscript:before{content:'\\e255'}.glyphicon-subscript:before{content:'\\e256'}.glyphicon-menu-left:bef");
			htmData.append("ore{content:'\\e257'}.glyphicon-menu-right:before{content:'\\e258'}.glyphicon-menu-down:before{content:'\\e259'}.glyphicon-menu-up:before{content:'\\e260'}*,:after,");
			htmData.append(":before{box-sizing:border-box}html{font-size:10px;-webkit-tap-highlight-color:transparent}body{font-family:'Helvetica Neue',Helvetica,Arial,sans-serif;font-size");
			htmData.append(":14px;line-height:1.42857143;color:#333}button,input,select,textarea{font-family:inherit;font-size:inherit;line-height:inherit}a{color:#337ab7;text-decoration:n");
			htmData.append("one}a:focus,a:hover{color:#23527c;text-decoration:underline}a:focus{outline:-webkit-focus-ring-color auto 5px;outline-offset:-2px}.carousel-inner>.item>a>img,.c");
			htmData.append("arousel-inner>.item>img,.img-responsive,.thumbnail a>img,.thumbnail>img{display:block;max-width:100%;height:auto}.img-rounded{border-radius:6px}.img-thumbnail{d");
			htmData.append("isplay:inline-block;max-width:100%;height:auto;padding:4px;line-height:1.42857143;border:1px solid #ddd;border-radius:4px;-webkit-transition:all .2s ease-in-out");
			htmData.append(";-o-transition:all .2s ease-in-out;transition:all .2s ease-in-out}.img-circle{border-radius:50%}hr{margin-top:20px;margin-bottom:20px;border-top:1px solid #eee}");
			htmData.append(".sr-only{position:absolute;width:1px;height:1px;padding:0;margin:-1px;overflow:hidden;clip:rect(0,0,0,0);border:0}.sr-only-focusable:active,.sr-only-focusable:f");
			htmData.append("ocus{position:static;width:auto;height:auto;margin:0;overflow:visible;clip:auto}[role=button]{cursor:pointer}.h1,.h2,.h3,.h4,.h5,.h6,h1,h2,h3,h4,h5,h6{font-fami");
			htmData.append("ly:inherit;font-weight:500;line-height:1.1;color:inherit}.h1 .small,.h1 small,.h2 .small,.h2 small,.h3 .small,.h3 small,.h4 .small,.h4 small,.h5 .small,.h5 smal");
			htmData.append("l,.h6 .small,.h6 small,h1 .small,h1 small,h2 .small,h2 small,h3 .small,h3 small,h4 .small,h4 small,h5 .small,h5 small,h6 .small,h6 small{font-weight:400;line-he");
			htmData.append("ight:1;color:#777}.h1,.h2,.h3,h1,h2,h3{margin-top:20px;margin-bottom:10px}.h1 .small,.h1 small,.h2 .small,.h2 small,.h3 .small,.h3 small,h1 .small,h1 small,h2 .");
			htmData.append("small,h2 small,h3 .small,h3 small{font-size:65%}.h4,.h5,.h6,h4,h5,h6{margin-top:10px;margin-bottom:10px}.h4 .small,.h4 small,.h5 .small,.h5 small,.h6 .small,.h6");
			htmData.append(" small,h4 .small,h4 small,h5 .small,h5 small,h6 .small,h6 small{font-size:75%}.h1,h1{font-size:36px}.h2,h2{font-size:30px}.h3,h3{font-size:24px}.h4,h4{font-size");
			htmData.append(":18px}.h5,h5{font-size:14px}.h6,h6{font-size:12px}p{margin:0 0 10px}.lead{margin-bottom:20px;font-size:16px;font-weight:300;line-height:1.4}dt,kbd kbd,label{fon");
			htmData.append("t-weight:700}address,blockquote .small,blockquote footer,blockquote small,dd,dt,pre{line-height:1.42857143}@media (min-width:768px){.lead{font-size:21px}}.small");
			htmData.append(",small{font-size:85%}.mark,mark{padding:.2em;background-color:#fcf8e3}.list-inline,.list-unstyled{padding-left:0;list-style:none}.text-left{text-align:left}.tex");
			htmData.append("t-right{text-align:right}.text-center{text-align:center}.text-justify{text-align:justify}.text-nowrap{white-space:nowrap}.text-lowercase{text-transform:lowercas");
			htmData.append("e}.text-uppercase{text-transform:uppercase}.text-capitalize{text-transform:capitalize}.text-muted{color:#777}.text-primary{color:#337ab7}a.text-primary:focus,a.");
			htmData.append("text-primary:hover{color:#286090}.text-success{color:#3c763d}a.text-success:focus,a.text-success:hover{color:#2b542c}.text-info{color:#31708f}a.text-info:focus,");
			htmData.append("a.text-info:hover{color:#245269}.text-warning{color:#8a6d3b}a.text-warning:focus,a.text-warning:hover{color:#66512c}.text-danger{color:#a94442}a.text-danger:foc");
			htmData.append("us,a.text-danger:hover{color:#843534}.bg-primary{color:#fff;background-color:#337ab7}a.bg-primary:focus,a.bg-primary:hover{background-color:#286090}.bg-success{");
			htmData.append("background-color:#dff0d8}a.bg-success:focus,a.bg-success:hover{background-color:#c1e2b3}.bg-info{background-color:#d9edf7}a.bg-info:focus,a.bg-info:hover{backgr");
			htmData.append("ound-color:#afd9ee}.bg-warning{background-color:#fcf8e3}a.bg-warning:focus,a.bg-warning:hover{background-color:#f7ecb5}.bg-danger{background-color:#f2dede}a.bg-");
			htmData.append("danger:focus,a.bg-danger:hover{background-color:#e4b9b9}pre code,table{background-color:transparent}.page-header{padding-bottom:9px;margin:40px 0 20px;border-bo");
			htmData.append("ttom:1px solid #eee}dl,ol,ul{margin-top:0}blockquote ol:last-child,blockquote p:last-child,blockquote ul:last-child,ol ol,ol ul,ul ol,ul ul{margin-bottom:0}addr");
			htmData.append("ess,dl{margin-bottom:20px}ol,ul{margin-bottom:10px}.list-inline{margin-left:-5px}.list-inline>li{display:inline-block;padding-right:5px;padding-left:5px}dd{marg");
			htmData.append("in-left:0}@media (min-width:768px){.dl-horizontal dt{float:left;width:160px;overflow:hidden;clear:left;text-align:right;text-overflow:ellipsis;white-space:nowra");
			htmData.append("p}.dl-horizontal dd{margin-left:180px}.container{width:750px}}abbr[data-original-title],abbr[title]{cursor:help;border-bottom:1px dotted #777}.initialism{font-s");
			htmData.append("ize:90%;text-transform:uppercase}blockquote{padding:10px 20px;margin:0 0 20px;font-size:17.5px;border-left:5px solid #eee}blockquote .small,blockquote footer,bl");
			htmData.append("ockquote small{display:block;font-size:80%;color:#777}legend,pre{display:block;color:#333}blockquote .small:before,blockquote footer:before,blockquote small:bef");
			htmData.append("ore{content:'\\2014 \\00A0'}.blockquote-reverse,blockquote.pull-right{padding-right:15px;padding-left:0;text-align:right;border-right:5px solid #eee;border-left:0");
			htmData.append("}code,kbd{padding:2px 4px;font-size:90%}caption,th{text-align:left}.blockquote-reverse .small:before,.blockquote-reverse footer:before,.blockquote-reverse small");
			htmData.append(":before,blockquote.pull-right .small:before,blockquote.pull-right footer:before,blockquote.pull-right small:before{content:''}.blockquote-reverse .small:after,.");
			htmData.append("blockquote-reverse footer:after,.blockquote-reverse small:after,blockquote.pull-right .small:after,blockquote.pull-right footer:after,blockquote.pull-right smal");
			htmData.append("l:after{content:'\\00A0 \\2014'}code,kbd,pre,samp{font-family:Menlo,Monaco,Consolas,'Courier New',monospace}code{color:#c7254e;background-color:#f9f2f4;border-rad");
			htmData.append("ius:4px}kbd{color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)}kbd k");
			htmData.append("bd{padding:0;font-size:100%;-webkit-box-shadow:none;box-shadow:none}pre{padding:9.5px;margin:0 0 10px;font-size:13px;word-break:break-all;word-wrap:break-word;b");
			htmData.append("ackground-color:#f5f5f5;border:1px solid #ccc;border-radius:4px}.container,.container-fluid{margin-right:auto;margin-left:auto}pre code{padding:0;font-size:inhe");
			htmData.append("rit;color:inherit;white-space:pre-wrap;border-radius:0}.container,.container-fluid{padding-right:15px;padding-left:15px}.pre-scrollable{overflow-y:scroll}@media");
			htmData.append(" (min-width:992px){.container{width:970px}}@media (min-width:1200px){.container{width:1170px}}.row{margin-right:-15px;margin-left:-15px}.col-lg-1,.col-lg-10,.co");
			htmData.append("l-lg-11,.col-lg-12,.col-lg-2,.col-lg-3,.col-lg-4,.col-lg-5,.col-lg-6,.col-lg-7,.col-lg-8,.col-lg-9,.col-md-1,.col-md-10,.col-md-11,.col-md-12,.col-md-2,.col-md-");
			htmData.append("3,.col-md-4,.col-md-5,.col-md-6,.col-md-7,.col-md-8,.col-md-9,.col-sm-1,.col-sm-10,.col-sm-11,.col-sm-12,.col-sm-2,.col-sm-3,.col-sm-4,.col-sm-5,.col-sm-6,.col-");
			htmData.append("sm-7,.col-sm-8,.col-sm-9,.col-xs-1,.col-xs-10,.col-xs-11,.col-xs-12,.col-xs-2,.col-xs-3,.col-xs-4,.col-xs-5,.col-xs-6,.col-xs-7,.col-xs-8,.col-xs-9{position:rel");
			htmData.append("ative;min-height:1px;padding-right:15px;padding-left:15px}.col-xs-12{width:100%}.col-xs-11{width:91.66666667%}.col-xs-10{width:83.33333333%}.col-xs-9{width:75%}");
			htmData.append(".col-xs-8{width:66.66666667%}.col-xs-7{width:58.33333333%}.col-xs-6{width:50%}.col-xs-5{width:41.66666667%}.col-xs-4{width:33.33333333%}.col-xs-3{width:25%}.col");
			htmData.append("-xs-2{width:16.66666667%}.col-xs-1{width:8.33333333%}.col-xs-pull-12{right:100%}.col-xs-pull-11{right:91.66666667%}.col-xs-pull-10{right:83.33333333%}.col-xs-pu");
			htmData.append("ll-9{right:75%}.col-xs-pull-8{right:66.66666667%}.col-xs-pull-7{right:58.33333333%}.col-xs-pull-6{right:50%}.col-xs-pull-5{right:41.66666667%}.col-xs-pull-4{rig");
			htmData.append("ht:33.33333333%}.col-xs-pull-3{right:25%}.col-xs-pull-2{right:16.66666667%}.col-xs-pull-1{right:8.33333333%}.col-xs-pull-0{right:auto}.col-xs-push-12{left:100%}");
			htmData.append(".col-xs-push-11{left:91.66666667%}.col-xs-push-10{left:83.33333333%}.col-xs-push-9{left:75%}.col-xs-push-8{left:66.66666667%}.col-xs-push-7{left:58.33333333%}.c");
			htmData.append("ol-xs-push-6{left:50%}.col-xs-push-5{left:41.66666667%}.col-xs-push-4{left:33.33333333%}.col-xs-push-3{left:25%}.col-xs-push-2{left:16.66666667%}.col-xs-push-1{");
			htmData.append("left:8.33333333%}.col-xs-push-0{left:auto}.col-xs-offset-12{margin-left:100%}.col-xs-offset-11{margin-left:91.66666667%}.col-xs-offset-10{margin-left:83.3333333");
			htmData.append("3%}.col-xs-offset-9{margin-left:75%}.col-xs-offset-8{margin-left:66.66666667%}.col-xs-offset-7{margin-left:58.33333333%}.col-xs-offset-6{margin-left:50%}.col-xs");
			htmData.append("-offset-5{margin-left:41.66666667%}.col-xs-offset-4{margin-left:33.33333333%}.col-xs-offset-3{margin-left:25%}.col-xs-offset-2{margin-left:16.66666667%}.col-xs-");
			htmData.append("offset-1{margin-left:8.33333333%}.col-xs-offset-0{margin-left:0}@media (min-width:768px){.col-sm-1,.col-sm-10,.col-sm-11,.col-sm-12,.col-sm-2,.col-sm-3,.col-sm-");
			htmData.append("4,.col-sm-5,.col-sm-6,.col-sm-7,.col-sm-8,.col-sm-9{float:left}.col-sm-12{width:100%}.col-sm-11{width:91.66666667%}.col-sm-10{width:83.33333333%}.col-sm-9{width");
			htmData.append(":75%}.col-sm-8{width:66.66666667%}.col-sm-7{width:58.33333333%}.col-sm-6{width:50%}.col-sm-5{width:41.66666667%}.col-sm-4{width:33.33333333%}.col-sm-3{width:25%}");
			htmData.append(".col-sm-2{width:16.66666667%}.col-sm-1{width:8.33333333%}.col-sm-pull-12{right:100%}.col-sm-pull-11{right:91.66666667%}.col-sm-pull-10{right:83.33333333%}.col-sm");
			htmData.append("-pull-9{right:75%}.col-sm-pull-8{right:66.66666667%}.col-sm-pull-7{right:58.33333333%}.col-sm-pull-6{right:50%}.col-sm-pull-5{right:41.66666667%}.col-sm-pull-4{r");
			htmData.append("ight:33.33333333%}.col-sm-pull-3{right:25%}.col-sm-pull-2{right:16.66666667%}.col-sm-pull-1{right:8.33333333%}.col-sm-pull-0{right:auto}.col-sm-push-12{left:100%");
			htmData.append("}.col-sm-push-11{left:91.66666667%}.col-sm-push-10{left:83.33333333%}.col-sm-push-9{left:75%}.col-sm-push-8{left:66.66666667%}.col-sm-push-7{left:58.33333333%}.c");
			htmData.append("ol-sm-push-6{left:50%}.col-sm-push-5{left:41.66666667%}.col-sm-push-4{left:33.33333333%}.col-sm-push-3{left:25%}.col-sm-push-2{left:16.66666667%}.col-sm-push-1{l");
			htmData.append("eft:8.33333333%}.col-sm-push-0{left:auto}.col-sm-offset-12{margin-left:100%}.col-sm-offset-11{margin-left:91.66666667%}.col-sm-offset-10{margin-left:83.33333333%");
			htmData.append("}.col-sm-offset-9{margin-left:75%}.col-sm-offset-8{margin-left:66.66666667%}.col-sm-offset-7{margin-left:58.33333333%}.col-sm-offset-6{margin-left:50%}.col-sm-of");
			htmData.append("fset-5{margin-left:41.66666667%}.col-sm-offset-4{margin-left:33.33333333%}.col-sm-offset-3{margin-left:25%}.col-sm-offset-2{margin-left:16.66666667%}.col-sm-offs");
			htmData.append("et-1{margin-left:8.33333333%}.col-sm-offset-0{margin-left:0}}@media (min-width:992px){.col-md-1,.col-md-10,.col-md-11,.col-md-12,.col-md-2,.col-md-3,.col-md-4,.c");
			htmData.append("ol-md-5,.col-md-6,.col-md-7,.col-md-8,.col-md-9{float:left}.col-md-12{width:100%}.col-md-11{width:91.66666667%}.col-md-10{width:83.33333333%}.col-md-9{width:75%}");
			htmData.append(".col-md-8{width:66.66666667%}.col-md-7{width:58.33333333%}.col-md-6{width:50%}.col-md-5{width:41.66666667%}.col-md-4{width:33.33333333%}.col-md-3{width:25%}.col-");
			htmData.append("md-2{width:16.66666667%}.col-md-1{width:8.33333333%}.col-md-pull-12{right:100%}.col-md-pull-11{right:91.66666667%}.col-md-pull-10{right:83.33333333%}.col-md-pull");
			htmData.append("-9{right:75%}.col-md-pull-8{right:66.66666667%}.col-md-pull-7{right:58.33333333%}.col-md-pull-6{right:50%}.col-md-pull-5{right:41.66666667%}.col-md-pull-4{right:");
			htmData.append("33.33333333%}.col-md-pull-3{right:25%}.col-md-pull-2{right:16.66666667%}.col-md-pull-1{right:8.33333333%}.col-md-pull-0{right:auto}.col-md-push-12{left:100%}.col");
			htmData.append("-md-push-11{left:91.66666667%}.col-md-push-10{left:83.33333333%}.col-md-push-9{left:75%}.col-md-push-8{left:66.66666667%}.col-md-push-7{left:58.33333333%}.col-md");
			htmData.append("-push-6{left:50%}.col-md-push-5{left:41.66666667%}.col-md-push-4{left:33.33333333%}.col-md-push-3{left:25%}.col-md-push-2{left:16.66666667%}.col-md-push-1{left:8");
			htmData.append(".33333333%}.col-md-push-0{left:auto}.col-md-offset-12{margin-left:100%}.col-md-offset-11{margin-left:91.66666667%}.col-md-offset-10{margin-left:83.33333333%}.col");
			htmData.append("-md-offset-9{margin-left:75%}.col-md-offset-8{margin-left:66.66666667%}.col-md-offset-7{margin-left:58.33333333%}.col-md-offset-6{margin-left:50%}.col-md-offset-");
			htmData.append("5{margin-left:41.66666667%}.col-md-offset-4{margin-left:33.33333333%}.col-md-offset-3{margin-left:25%}.col-md-offset-2{margin-left:16.66666667%}.col-md-offset-1{");
			htmData.append("margin-left:8.33333333%}.col-md-offset-0{margin-left:0}}@media (min-width:1200px){.col-lg-1,.col-lg-10,.col-lg-11,.col-lg-12,.col-lg-2,.col-lg-3,.col-lg-4,.col-l");
			htmData.append("g-5,.col-lg-6,.col-lg-7,.col-lg-8,.col-lg-9{float:left}.col-lg-12{width:100%}.col-lg-11{width:91.66666667%}.col-lg-10{width:83.33333333%}.col-lg-9{width:75%}.col");
			htmData.append("-lg-8{width:66.66666667%}.col-lg-7{width:58.33333333%}.col-lg-6{width:50%}.col-lg-5{width:41.66666667%}.col-lg-4{width:33.33333333%}.col-lg-3{width:25%}.col-lg-2");
			htmData.append("{width:16.66666667%}.col-lg-1{width:8.33333333%}.col-lg-pull-12{right:100%}.col-lg-pull-11{right:91.66666667%}.col-lg-pull-10{right:83.33333333%}.col-lg-pull-9{r");
			htmData.append("ight:75%}.col-lg-pull-8{right:66.66666667%}.col-lg-pull-7{right:58.33333333%}.col-lg-pull-6{right:50%}.col-lg-pull-5{right:41.66666667%}.col-lg-pull-4{right:33.3");
			htmData.append("3333333%}.col-lg-pull-3{right:25%}.col-lg-pull-2{right:16.66666667%}.col-lg-pull-1{right:8.33333333%}.col-lg-pull-0{right:auto}.col-lg-push-12{left:100%}.col-lg-");
			htmData.append("push-11{left:91.66666667%}.col-lg-push-10{left:83.33333333%}.col-lg-push-9{left:75%}.col-lg-push-8{left:66.66666667%}.col-lg-push-7{left:58.33333333%}.col-lg-pus");
			htmData.append("h-6{left:50%}.col-lg-push-5{left:41.66666667%}.col-lg-push-4{left:33.33333333%}.col-lg-push-3{left:25%}.col-lg-push-2{left:16.66666667%}.col-lg-push-1{left:8.333");
			htmData.append("33333%}.col-lg-push-0{left:auto}.col-lg-offset-12{margin-left:100%}.col-lg-offset-11{margin-left:91.66666667%}.col-lg-offset-10{margin-left:83.33333333%}.col-lg-");
			htmData.append("offset-9{margin-left:75%}.col-lg-offset-8{margin-left:66.66666667%}.col-lg-offset-7{margin-left:58.33333333%}.col-lg-offset-6{margin-left:50%}.col-lg-offset-5{ma");
			htmData.append("rgin-left:41.66666667%}.col-lg-offset-4{margin-left:33.33333333%}.col-lg-offset-3{margin-left:25%}.col-lg-offset-2{margin-left:16.66666667%}.col-lg-offset-1{marg");
			htmData.append("in-left:8.33333333%}.col-lg-offset-0{margin-left:0}}caption{padding-top:8px;padding-bottom:8px;color:#777}.table{width:100%;max-width:100%;margin-bottom:20px}");
			htmData.append(".table>tbody>tr>td,.table>tbody>tr>th,.table>tfoot>tr>td,.table>tfoot>tr>th,.table>thead>tr>td,.table>thead>tr>th{padding:8px;line-height:1.42857143;vertical-");
			htmData.append("align:top;border-top:1px solid #ddd}.table>thead>tr>th{vertical-align:bottom;border-bottom:2px solid #ddd}.table>caption+thead>tr:first-child>td,.table>captio");
			htmData.append("n+thead>tr:first-child>th,.table>colgroup+thead>tr:first-child>td,.table>colgroup+thead>tr:first-child>th,.table>thead:first-child>tr:first-child>td,.table>th");
			htmData.append("ead:first-child>tr:first-child>th{border-top:0}.table>tbody+tbody{border-top:2px solid #ddd}.table .table{background-color:#fff}.table-condensed>tbody>tr>td,.");
			htmData.append("table-condensed>tbody>tr>th,.table-condensed>tfoot>tr>td,.table-condensed>tfoot>tr>th,.table-condensed>thead>tr>td,.table-condensed>thead>tr>th{padding:5px}.t");
			htmData.append("able-bordered,.table-bordered>tbody>tr>td,.table-bordered>tbody>tr>th,.table-bordered>tfoot>tr>td,.table-bordered>tfoot>tr>th,.table-bordered>thead>tr>td,.tab");
			htmData.append("le-bordered>thead>tr>th{border:1px solid #ddd}.table-bordered>thead>tr>td,.table-bordered>thead>tr>th{border-bottom-width:2px}.table-striped>tbody>tr:nth-of-t");
			htmData.append("ype(odd){background-color:#f9f9f9}.table-hover>tbody>tr:hover,.table>tbody>tr.active>td,.table>tbody>tr.active>th,.table>tbody>tr>td.active,.table>tbody>tr>th");
			htmData.append(".active,.table>tfoot>tr.active>td,.table>tfoot>tr.active>th,.table>tfoot>tr>td.active,.table>tfoot>tr>th.active,.table>thead>tr.active>td,.table>thead>tr.acti");
			htmData.append("ve>th,.table>thead>tr>td.active,.table>thead>tr>th.active{background-color:#f5f5f5}table col[class*=col-]{position:static;display:table-column;float:none}tabl");
			htmData.append("e td[class*=col-],table th[class*=col-]{position:static;display:table-cell;float:none}.table-hover>tbody>tr.active:hover>td,.table-hover>tbody>tr.active:hover");
			htmData.append(">th,.table-hover>tbody>tr:hover>.active,.table-hover>tbody>tr>td.active:hover,.table-hover>tbody>tr>th.active:hover{background-color:#e8e8e8}.table>tbody>tr.s");
			htmData.append("uccess>td,.table>tbody>tr.success>th,.table>tbody>tr>td.success,.table>tbody>tr>th.success,.table>tfoot>tr.success>td,.table>tfoot>tr.success>th,.table>tfoot>");
			htmData.append("tr>td.success,.table>tfoot>tr>th.success,.table>thead>tr.success>td,.table>thead>tr.success>th,.table>thead>tr>td.success,.table>thead>tr>th.success{backgroun");
			htmData.append("d-color:#dff0d8}.table-hover>tbody>tr.success:hover>td,.table-hover>tbody>tr.success:hover>th,.table-hover>tbody>tr:hover>.success,.table-hover>tbody>tr>td.su");
			htmData.append("ccess:hover,.table-hover>tbody>tr>th.success:hover{background-color:#d0e9c6}.table>tbody>tr.info>td,.table>tbody>tr.info>th,.table>tbody>tr>td.info,.table>tbody");
			htmData.append(">tr>th.info,.table>tfoot>tr.info>td,.table>tfoot>tr.info>th,.table>tfoot>tr>td.info,.table>tfoot>tr>th.info,.table>thead>tr.info>td,.table>thead>tr.info>th,.tab");
			htmData.append("le>thead>tr>td.info,.table>thead>tr>th.info{background-color:#d9edf7}.table-hover>tbody>tr.info:hover>td,.table-hover>tbody>tr.info:hover>th,.table-hover>tbody>");
			htmData.append("tr:hover>.info,.table-hover>tbody>tr>td.info:hover,.table-hover>tbody>tr>th.info:hover{background-color:#c4e3f3}.table>tbody>tr.warning>td,.table>tbody>tr.warni");
			htmData.append("ng>th,.table>tbody>tr>td.warning,.table>tbody>tr>th.warning,.table>tfoot>tr.warning>td,.table>tfoot>tr.warning>th,.table>tfoot>tr>td.warning,.table>tfoot>tr>th.");
			htmData.append("warning,.table>thead>tr.warning>td,.table>thead>tr.warning>th,.table>thead>tr>td.warning,.table>thead>tr>th.warning{background-color:#fcf8e3}.table-hover>tbody>");
			htmData.append("tr.warning:hover>td,.table-hover>tbody>tr.warning:hover>th,.table-hover>tbody>tr:hover>.warning,.table-hover>tbody>tr>td.warning:hover,.table-hover>tbody>tr>th.");
			htmData.append("warning:hover{background-color:#faf2cc}.table>tbody>tr.danger>td,.table>tbody>tr.danger>th,.table>tbody>tr>td.danger,.table>tbody>tr>th.danger,.table>tfoot>tr.d");
			htmData.append("anger>td,.table>tfoot>tr.danger>th,.table>tfoot>tr>td.danger,.table>tfoot>tr>th.danger,.table>thead>tr.danger>td,.table>thead>tr.danger>th,.table>thead>tr>td.da");
			htmData.append("nger,.table>thead>tr>th.danger{background-color:#f2dede}.table-hover>tbody>tr.danger:hover>td,.table-hover>tbody>tr.danger:hover>th,.table-hover>tbody>tr:hover>");
			htmData.append(".danger,.table-hover>tbody>tr>td.danger:hover,.table-hover>tbody>tr>th.danger:hover{background-color:#ebcccc}.table-responsive{min-height:.01%;overflow-x:auto}@");
			htmData.append("media screen and (max-width:767px){.table-responsive{width:100%;margin-bottom:15px;overflow-y:hidden;-ms-overflow-style:-ms-autohiding-scrollbar;border:1px soli");
			htmData.append("d #ddd}.table-responsive>.table{margin-bottom:0}.table-responsive>.table>tbody>tr>td,.table-responsive>.table>tbody>tr>th,.table-responsive>.table>tfoot>tr>td,.");
			htmData.append("table-responsive>.table>tfoot>tr>th,.table-responsive>.table>thead>tr>td,.table-responsive>.table>thead>tr>th{white-space:nowrap}.table-responsive>.table-border");
			htmData.append("ed{border:0}.table-responsive>.table-bordered>tbody>tr>td:first-child,.table-responsive>.table-bordered>tbody>tr>th:first-child,.table-responsive>.table-bordere");
			htmData.append("d>tfoot>tr>td:first-child,.table-responsive>.table-bordered>tfoot>tr>th:first-child,.table-responsive>.table-bordered>thead>tr>td:first-child,.table-responsive>");
			htmData.append(".table-bordered>thead>tr>th:first-child{border-left:0}.table-responsive>.table-bordered>tbody>tr>td:last-child,.table-responsive>.table-bordered>tbody>tr>th:las");
			htmData.append("t-child,.table-responsive>.table-bordered>tfoot>tr>td:last-child,.table-responsive>.table-bordered>tfoot>tr>th:last-child,.table-responsive>.table-bordered>thea");
			htmData.append("d>tr>td:last-child,.table-responsive>.table-bordered>thead>tr>th:last-child{border-right:0}.table-responsive>.table-bordered>tbody>tr:last-child>td,.table-respo");
			htmData.append("nsive>.table-bordered>tbody>tr:last-child>th,.table-responsive>.table-bordered>tfoot>tr:last-child>td,.table-responsive>.table-bordered>tfoot>tr:last-child>th{b");
			htmData.append("order-bottom:0}}fieldset,legend{padding:0;border:0}fieldset{min-width:0;margin:0}legend{width:100%;margin-bottom:20px;font-size:21px;line-height:inherit;border-");
			htmData.append("bottom:1px solid #e5e5e5}label{display:inline-block;max-width:100%;margin-bottom:5px}input[type=search]{-webkit-box-sizing:border-box;-moz-box-sizing:border-box");
			htmData.append(";box-sizing:border-box;-webkit-appearance:none}input[type=checkbox],input[type=radio]{margin:4px 0 0;margin-top:1px\\9;line-height:normal}.form-control,output{fo");
			htmData.append("nt-size:14px;line-height:1.42857143;color:#555;display:block}input[type=file]{display:block}input[type=range]{display:block;width:100%}select[multiple],select[s");
			htmData.append("ize]{height:auto}input[type=checkbox]:focus,input[type=file]:focus,input[type=radio]:focus{outline:-webkit-focus-ring-color auto 5px;outline-offset:-2px}output{");
			htmData.append("padding-top:7px}.form-control{width:100%;height:34px;padding:6px 12px;background-color:#fff;border:1px solid #ccc;border-radius:4px;-webkit-box-shadow:inset 0 1");
			htmData.append("px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075);-webkit-transition:border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;-o-tran");
			htmData.append("sition:border-color ease-in-out .15s,box-shadow ease-in-out .15s;transition:border-color ease-in-out .15s,box-shadow ease-in-out .15s}.form-control:focus{border");
			htmData.append("-color:#66afe9;outline:0;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rg");
			htmData.append("ba(102,175,233,.6)}.form-control::-moz-placeholder{color:#999;opacity:1}.form-control:-ms-input-placeholder{color:#999}.form-control::-webkit-input-placeholder{");
			htmData.append("color:#999}.has-success .checkbox,.has-success .checkbox-inline,.has-success .control-label,.has-success .form-control-feedback,.has-success .help-block,.has-su");
			htmData.append("ccess .radio,.has-success .radio-inline,.has-success.checkbox label,.has-success.checkbox-inline label,.has-success.radio label,.has-success.radio-inline label{");
			htmData.append("color:#3c763d}.form-control::-ms-expand{background-color:transparent;border:0}.form-control[disabled],.form-control[readonly],fieldset[disabled] .form-control{b");
			htmData.append("ackground-color:#eee;opacity:1}.form-control[disabled],fieldset[disabled] .form-control{cursor:not-allowed}textarea.form-control{height:auto}@media screen and (");
			htmData.append("-webkit-min-device-pixel-ratio:0){input[type=date].form-control,input[type=datetime-local].form-control,input[type=month].form-control,input[type=time].form-con");
			htmData.append("trol{line-height:34px}.input-group-sm input[type=date],.input-group-sm input[type=datetime-local],.input-group-sm input[type=month],.input-group-sm input[type=t");
			htmData.append("ime],input[type=date].input-sm,input[type=datetime-local].input-sm,input[type=month].input-sm,input[type=time].input-sm{line-height:30px}.input-group-lg input[t");
			htmData.append("ype=date],.input-group-lg input[type=datetime-local],.input-group-lg input[type=month],.input-group-lg input[type=time],input[type=date].input-lg,input[type=dat");
			htmData.append("etime-local].input-lg,input[type=month].input-lg,input[type=time].input-lg{line-height:46px}}.form-group{margin-bottom:15px}.checkbox,.radio{position:relative;display:block;margin-top:10px;margin-bottom:10px}.checkbox label,.radio label{min-height:20px;padding-left:20px;margin-bottom:0;font-weight:400;cursor:pointer}.checkbox input[type=checkbox],.checkbox-inline input[type=checkbox],.radio input[type=radio],.radio-inline input[type=radio]{position:absolute;margin-top:4px\\9;margin-left:-20px}.checkbox+.checkbox,.radio+.radio{margin-top:-5px}.checkbox-inline,.radio-inline{position:relative;display:inline-block;padding-left:20px;margin-bottom:0;font-weight:400;vertical-align:middle;cursor:pointer}.checkbox-inline+.checkbox-inline,.radio-inline+.radio-inline{margin-top:0;margin-left:10px}.checkbox-inline.disabled,.checkbox.disabled label,.radio-inline.disabled,.radio.disabled label,fieldset[disabled] .checkbox label,fieldset[disabled] .checkbox-inline,fieldset[disabled] .radio label,fieldset[disabled] .radio-inline,fieldset[disabled] input[type=checkbox],fieldset[disabled] input[type=radio],input[type=checkbox].disabled,input[type=checkbox][disabled],input[type=radio].disabled,input[type=radio][disabled]{cursor:not-allowed}.form-control-static{min-height:34px;padding-top:7px;padding-bottom:7px;margin-bottom:0}.form-control-static.input-lg,.form-control-static.input-sm{padding-right:0;padding-left:0}.form-group-sm .form-control,.input-sm{padding:5px 10px;border-radius:3px;font-size:12px}");
			htmData.append(".input-sm{height:30px;line-height:1.5}select.input-sm{height:30px;line-height:30px}select[multiple].input-sm,textarea.input-sm{height:auto}.form-group-sm .form-control{height:30px;line-height:1.5}.form-group-lg .form-control,.input-lg{border-radius:6px;padding:10px 16px;font-size:18px}.form-group-sm select.form-control{height:30px;line-height:30px}.form-group-sm select[multiple].form-control,.form-group-sm textarea.form-control{height:auto}.form-group-sm .form-control-static{height:30px;min-height:32px;padding:6px 10px;font-size:12px;line-height:1.5}.input-lg{height:46px;line-height:1.3333333}select.input-lg{height:46px;line-height:46px}select[multiple].input-lg,textarea.input-lg{height:auto}.form-group-lg .form-control{height:46px;line-height:1.3333333}.form-group-lg select.form-control{height:46px;line-height:46px}.form-group-lg select[multiple].form-control,.form-group-lg textarea.form-control{height:auto}.form-group-lg .form-control-static{height:46px;min-height:38px;padding:11px 16px;font-size:18px;line-height:1.3333333}.has-feedback{position:relative}.has-feedback .form-control{padding-right:42.5px}.form-control-feedback{position:absolute;top:0;right:0;z-index:2;display:block;width:34px;height:34px;line-height:34px;text-align:center;pointer-events:none}.collapsing,.dropdown,.dropup{position:relative}.form-group-lg .form-control+.form-control-feedback,.input-group-lg+.form-control-feedback,.input-lg+.form-control-feedback{width:46px;height:46px;line-height:46px}");
			htmData.append(".form-group-sm .form-control+.form-control-feedback,.input-group-sm+.form-control-feedback,.input-sm+.form-control-feedback{width:30px;height:30px;line-height:30px}.has-success .form-control{border-color:#3c763d;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075)}.has-success .form-control:focus{border-color:#2b542c;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #67b168;box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #67b168}.has-success .input-group-addon{color:#3c763d;background-color:#dff0d8;border-color:#3c763d}.has-warning .checkbox,.has-warning .checkbox-inline,.has-warning .control-label,.has-warning .form-control-feedback,.has-warning .help-block,.has-warning .radio,.has-warning .radio-inline,.has-warning.checkbox label,.has-warning.checkbox-inline label,.has-warning.radio label,.has-warning.radio-inline label{color:#8a6d3b}.has-warning .form-control{border-color:#8a6d3b;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075)}.has-warning .form-control:focus{border-color:#66512c;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #c0a16b;box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #c0a16b}.has-warning .input-group-addon{color:#8a6d3b;background-color:#fcf8e3;border-color:#8a6d3b}.has-error .checkbox,.has-error .checkbox-inline,.has-error .control-label,.has-error .form-control-feedback,.has-error .help-block,.has-error .radio,.has-error ");
			htmData.append(".radio-inline,.has-error.checkbox label,.has-error.checkbox-inline label,.has-error.radio label,.has-error.radio-inline label{color:#a94442}.has-error .form-control{border-color:#a94442;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075)}.has-error .form-control:focus{border-color:#843534;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #ce8483;box-shadow:inset 0 1px 1px rgba(0,0,0,.075),0 0 6px #ce8483}.has-error .input-group-addon{color:#a94442;background-color:#f2dede;border-color:#a94442}.has-feedback label~.form-control-feedback{top:25px}.has-feedback label.sr-only~.form-control-feedback{top:0}.help-block{display:block;margin-top:5px;margin-bottom:10px;color:#737373}@media (min-width:768px){.form-inline .form-control-static,.form-inline .form-group{display:inline-block}.form-inline .control-label,.form-inline .form-group{margin-bottom:0;vertical-align:middle}.form-inline .form-control{display:inline-block;width:auto;vertical-align:middle}.form-inline .input-group{display:inline-table;vertical-align:middle}.form-inline .input-group .form-control,.form-inline .input-group .input-group-addon,.form-inline .input-group .input-group-btn{width:auto}.form-inline .input-group>.form-control{width:100%}.form-inline .checkbox,.form-inline .radio{display:inline-block;margin-top:0;margin-bottom:0;vertical-align:middle}.form-inline .checkbox label,.form-inline .radio label{padding-left:0}.form-inline .checkbox input[type=checkbox]");
			htmData.append(",.form-inline .radio input[type=radio]{position:relative;margin-left:0}.form-inline .has-feedback .form-control-feedback{top:0}.form-horizontal .control-label{padding-top:7px;margin-bottom:0;text-align:right}}.form-horizontal .checkbox,.form-horizontal .checkbox-inline,.form-horizontal .radio,.form-horizontal .radio-inline{padding-top:7px;margin-top:0;margin-bottom:0}.form-horizontal .checkbox,.form-horizontal .radio{min-height:27px}.form-horizontal .form-group{margin-right:-15px;margin-left:-15px}.form-horizontal .has-feedback .form-control-feedback{right:15px}@media (min-width:768px){.form-horizontal .form-group-lg .control-label{padding-top:11px;font-size:18px}.form-horizontal .form-group-sm .control-label{padding-top:6px;font-size:12px}}.btn{display:inline-block;padding:6px 12px;margin-bottom:0;font-size:14px;font-weight:400;line-height:1.42857143;text-align:center;white-space:nowrap;vertical-align:middle;-ms-touch-action:manipulation;touch-action:manipulation;cursor:pointer;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;border:1px solid transparent;border-radius:4px}.btn.active.focus,.btn.active:focus,.btn.focus,.btn:active.focus,.btn:active:focus,.btn:focus{outline:-webkit-focus-ring-color auto 5px;outline-offset:-2px}.btn.focus,.btn:focus,.btn:hover{color:#333;text-decoration:none}.btn.active,.btn:active{outline:0;-webkit-box-shadow:inset 0 3px 5px rgba(0,0,0,.125);box-shadow:inset 0 3px 5px rgba(0,0,0,.125)}");
			htmData.append(".btn.disabled,.btn[disabled],fieldset[disabled] .btn{cursor:not-allowed;filter:alpha(opacity=65);-webkit-box-shadow:none;box-shadow:none;opacity:.65}a.btn.disabled,fieldset[disabled] a.btn{pointer-events:none}.btn-default{color:#333;background-color:#fff;border-color:#ccc}.btn-default.focus,.btn-default:focus{color:#333;background-color:#e6e6e6;border-color:#8c8c8c}.btn-default.active,.btn-default:active,.btn-default:hover,.open>.dropdown-toggle.btn-default{color:#333;background-color:#e6e6e6;border-color:#adadad}.btn-default.active.focus,.btn-default.active:focus,.btn-default.active:hover,.btn-default:active.focus,.btn-default:active:focus,.btn-default:active:hover,.open>.dropdown-toggle.btn-default.focus,.open>.dropdown-toggle.btn-default:focus,.open>.dropdown-toggle.btn-default:hover{color:#333;background-color:#d4d4d4;border-color:#8c8c8c}.btn-default.disabled.focus,.btn-default.disabled:focus,.btn-default.disabled:hover,.btn-default[disabled].focus,.btn-default[disabled]:focus,.btn-default[disabled]:hover,fieldset[disabled] .btn-default.focus,fieldset[disabled] .btn-default:focus,fieldset[disabled] .btn-default:hover{background-color:#fff;border-color:#ccc}.btn-default .badge{color:#fff;background-color:#333}.btn-primary{color:#fff;background-color:#337ab7;border-color:#2e6da4}.btn-primary.focus,.btn-primary:focus{color:#fff;background-color:#286090;border-color:#122b40}.btn-primary.active,.btn-primary:active,.btn-primary:hover,.open>.dropdown-toggle.btn-primary{color:#fff;background-color:#286090;border-color:#204d74}");
			htmData.append(".btn-primary.active.focus,.btn-primary.active:focus,.btn-primary.active:hover,.btn-primary:active.focus,.btn-primary:active:focus,.btn-primary:active:hover,.open>.dropdown-toggle.btn-primary.focus,.open>.dropdown-toggle.btn-primary:focus,.open>.dropdown-toggle.btn-primary:hover{color:#fff;background-color:#204d74;border-color:#122b40}.btn-primary.disabled.focus,.btn-primary.disabled:focus,.btn-primary.disabled:hover,.btn-primary[disabled].focus,.btn-primary[disabled]:focus,.btn-primary[disabled]:hover,fieldset[disabled] .btn-primary.focus,fieldset[disabled] .btn-primary:focus,fieldset[disabled] .btn-primary:hover{background-color:#337ab7;border-color:#2e6da4}.btn-primary .badge{color:#337ab7;background-color:#fff}.btn-success{color:#fff;background-color:#5cb85c;border-color:#4cae4c}.btn-success.focus,.btn-success:focus{color:#fff;background-color:#449d44;border-color:#255625}.btn-success.active,.btn-success:active,.btn-success:hover,.open>.dropdown-toggle.btn-success{color:#fff;background-color:#449d44;border-color:#398439}.btn-success.active.focus,.btn-success.active:focus,.btn-success.active:hover,.btn-success:active.focus,.btn-success:active:focus,.btn-success:active:hover,.open>.dropdown-toggle.btn-success.focus,.open>.dropdown-toggle.btn-success:focus,.open>.dropdown-toggle.btn-success:hover{color:#fff;background-color:#398439;border-color:#255625}.btn-success.active,.btn-success:active,.open>.dropdown-toggle.btn-success{background-image:none}");
			htmData.append(".btn-success.disabled.focus,.btn-success.disabled:focus,.btn-success.disabled:hover,.btn-success[disabled].focus,.btn-success[disabled]:focus,.btn-success[disabled]:hover,fieldset[disabled] .btn-success.focus,fieldset[disabled] .btn-success:focus,fieldset[disabled] .btn-success:hover{background-color:#5cb85c;border-color:#4cae4c}.btn-success .badge{color:#5cb85c;background-color:#fff}.btn-info{color:#fff;background-color:#5bc0de;border-color:#46b8da}.btn-info.focus,.btn-info:focus{color:#fff;background-color:#31b0d5;border-color:#1b6d85}.btn-info.active,.btn-info:active,.btn-info:hover,.open>.dropdown-toggle.btn-info{color:#fff;background-color:#31b0d5;border-color:#269abc}.btn-info.active.focus,.btn-info.active:focus,.btn-info.active:hover,.btn-info:active.focus,.btn-info:active:focus,.btn-info:active:hover,.open>.dropdown-toggle.btn-info.focus,.open>.dropdown-toggle.btn-info:focus,.open>.dropdown-toggle.btn-info:hover{color:#fff;background-color:#269abc;border-color:#1b6d85}.btn-info.disabled.focus,.btn-info.disabled:focus,.btn-info.disabled:hover,.btn-info[disabled].focus,.btn-info[disabled]:focus,.btn-info[disabled]:hover,fieldset[disabled] .btn-info.focus,fieldset[disabled] .btn-info:focus,fieldset[disabled] .btn-info:hover{background-color:#5bc0de;border-color:#46b8da}.btn-info .badge{color:#5bc0de;background-color:#fff}.btn-warning{color:#fff;background-color:#f0ad4e;border-color:#eea236}.btn-warning.focus,.btn-warning:focus{color:#fff;background-color:#ec971f;border-color:#985f0d}");
			htmData.append(".btn-warning.active,.btn-warning:active,.btn-warning:hover,.open>.dropdown-toggle.btn-warning{color:#fff;background-color:#ec971f;border-color:#d58512}.btn-warning.active.focus,.btn-warning.active:focus,.btn-warning.active:hover,.btn-warning:active.focus,.btn-warning:active:focus,.btn-warning:active:hover,.open>.dropdown-toggle.btn-warning.focus,.open>.dropdown-toggle.btn-warning:focus,.open>.dropdown-toggle.btn-warning:hover{color:#fff;background-color:#d58512;border-color:#985f0d}.btn-warning.disabled.focus,.btn-warning.disabled:focus,.btn-warning.disabled:hover,.btn-warning[disabled].focus,.btn-warning[disabled]:focus,.btn-warning[disabled]:hover,fieldset[disabled] .btn-warning.focus,fieldset[disabled] .btn-warning:focus,fieldset[disabled] .btn-warning:hover{background-color:#f0ad4e;border-color:#eea236}.btn-warning .badge{color:#f0ad4e;background-color:#fff}.btn-danger{color:#fff;background-color:#d9534f;border-color:#d43f3a}.btn-danger.focus,.btn-danger:focus{color:#fff;background-color:#c9302c;border-color:#761c19}.btn-danger.active,.btn-danger:active,.btn-danger:hover,.open>.dropdown-toggle.btn-danger{color:#fff;background-color:#c9302c;border-color:#ac2925}.btn-danger.active.focus,.btn-danger.active:focus,.btn-danger.active:hover,.btn-danger:active.focus,.btn-danger:active:focus,.btn-danger:active:hover,.open>.dropdown-toggle.btn-danger.focus,.open>.dropdown-toggle.btn-danger:focus,.open>.dropdown-toggle.btn-danger:hover{color:#fff;background-color:#ac2925;border-color:#761c19}");
			htmData.append(".btn-danger.disabled.focus,.btn-danger.disabled:focus,.btn-danger.disabled:hover,.btn-danger[disabled].focus,.btn-danger[disabled]:focus,.btn-danger[disabled]:hover,fieldset[disabled] .btn-danger.focus,fieldset[disabled] .btn-danger:focus,fieldset[disabled] .btn-danger:hover{background-color:#d9534f;border-color:#d43f3a}.btn-danger .badge{color:#d9534f;background-color:#fff}.btn-link{font-weight:400;color:#337ab7;border-radius:0}.btn-link,.btn-link.active,.btn-link:active,.btn-link[disabled],fieldset[disabled] .btn-link{background-color:transparent;-webkit-box-shadow:none;box-shadow:none}.btn-link,.btn-link:active,.btn-link:focus,.btn-link:hover{border-color:transparent}.btn-link:focus,.btn-link:hover{color:#23527c;text-decoration:underline;background-color:transparent}.btn-link[disabled]:focus,.btn-link[disabled]:hover,fieldset[disabled] .btn-link:focus,fieldset[disabled] .btn-link:hover{color:#777;text-decoration:none}.btn-group-lg>.btn,.btn-lg{padding:10px 16px;font-size:18px;line-height:1.3333333;border-radius:6px}.btn-group-sm>.btn,.btn-sm{padding:5px 10px;font-size:12px;line-height:1.5;border-radius:3px}.btn-group-xs>.btn,.btn-xs{padding:1px 5px;font-size:12px;line-height:1.5;border-radius:3px}.btn-block{display:block;width:100%}.btn-block+.btn-block{margin-top:5px}input[type=button].btn-block,input[type=reset].btn-block,input[type=submit].btn-block{width:100%}.fade{opacity:0;-webkit-transition:opacity .15s linear;-o-transition:opacity .15s linear;transition:opacity .15s linear}");
			htmData.append(".fade.in{opacity:1}.collapse{display:none}.collapse.in{display:block}tr.collapse.in{display:table-row}tbody.collapse.in{display:table-row-group}.collapsing{height:0;overflow:hidden;-webkit-transition-timing-function:ease;-o-transition-timing-function:ease;transition-timing-function:ease;-webkit-transition-duration:.35s;-o-transition-duration:.35s;transition-duration:.35s;-webkit-transition-property:height,visibility;-o-transition-property:height,visibility;transition-property:height,visibility}.caret{display:inline-block;width:0;height:0;margin-left:2px;vertical-align:middle;border-top:4px dashed;border-top:4px solid\\9;border-right:4px solid transparent;border-left:4px solid transparent}.dropdown-toggle:focus{outline:0}.dropdown-menu{position:absolute;top:100%;left:0;z-index:1000;display:none;min-width:160px;padding:5px 0;margin:2px 0 0;font-size:14px;text-align:left;list-style:none;background-color:#fff;background-clip:padding-box;border:1px solid #ccc;border:1px solid rgba(0,0,0,.15);border-radius:4px;-webkit-box-shadow:0 6px 12px rgba(0,0,0,.175);box-shadow:0 6px 12px rgba(0,0,0,.175)}.dropdown-menu-right,.dropdown-menu.pull-right{right:0;left:auto}.dropdown-header,.dropdown-menu>li>a{display:block;padding:3px 20px;line-height:1.42857143;white-space:nowrap}.btn-group>.btn-group:first-child:not(:last-child)>.btn:last-child,.btn-group>.btn-group:first-child:not(:last-child)>.dropdown-toggle,.btn-group>.btn:first-child:not(:last-child):not(.dropdown-toggle){border-top-right-radius:0;");
			htmData.append("border-bottom-right-radius:0}.btn-group>.btn-group:last-child:not(:first-child)>.btn:first-child,.btn-group>.btn:last-child:not(:first-child),.btn-group>.dropdown-toggle:not(:first-child){border-top-left-radius:0;border-bottom-left-radius:0}.btn-group-vertical>.btn:not(:first-child):not(:last-child),.btn-group>.btn-group:not(:first-child):not(:last-child)>.btn,.btn-group>.btn:not(:first-child):not(:last-child):not(.dropdown-toggle){border-radius:0}.dropdown-menu .divider{height:1px;margin:9px 0;overflow:hidden;background-color:#e5e5e5}.dropdown-menu>li>a{clear:both;font-weight:400;color:#333}.dropdown-menu>li>a:focus,.dropdown-menu>li>a:hover{color:#262626;text-decoration:none;background-color:#f5f5f5}.dropdown-menu>.active>a,.dropdown-menu>.active>a:focus,.dropdown-menu>.active>a:hover{color:#fff;text-decoration:none;background-color:#337ab7;outline:0}.dropdown-menu>.disabled>a,.dropdown-menu>.disabled>a:focus,.dropdown-menu>.disabled>a:hover{color:#777}.dropdown-menu>.disabled>a:focus,.dropdown-menu>.disabled>a:hover{text-decoration:none;cursor:not-allowed;background-color:transparent;filter:progid:DXImageTransform.Microsoft.gradient(enabled=false)}.open>.dropdown-menu{display:block}.open>a{outline:0}.dropdown-menu-left{right:auto;left:0}.dropdown-header{font-size:12px;color:#777}.dropdown-backdrop{position:fixed;top:0;right:0;bottom:0;left:0;z-index:990}.nav-justified>.dropdown .dropdown-menu,.nav-tabs.nav-justified>.dropdown .dropdown-menu{top:auto;left:auto}.pull-right>.dropdown-menu{right:0;left:auto}");
			htmData.append(".dropup .caret,.navbar-fixed-bottom .dropdown .caret{content:'';border-top:0;border-bottom:4px dashed;border-bottom:4px solid\\9}.dropup .dropdown-menu,.navbar-fixed-bottom .dropdown .dropdown-menu{top:auto;bottom:100%;margin-bottom:2px}@media (min-width:768px){.navbar-right .dropdown-menu{right:0;left:auto}.navbar-right .dropdown-menu-left{right:auto;left:0}}.btn-group,.btn-group-vertical{position:relative;display:inline-block;vertical-align:middle}.btn-group-vertical>.btn,.btn-group>.btn{position:relative;float:left}.btn-group-vertical>.btn.active,.btn-group-vertical>.btn:active,.btn-group-vertical>.btn:focus,.btn-group-vertical>.btn:hover,.btn-group>.btn.active,.btn-group>.btn:active,.btn-group>.btn:focus,.btn-group>.btn:hover{z-index:2}.btn-group .btn+.btn,.btn-group .btn+.btn-group,.btn-group .btn-group+.btn,.btn-group .btn-group+.btn-group{margin-left:-1px}.btn-toolbar{margin-left:-5px}.btn-toolbar>.btn,.btn-toolbar>.btn-group,.btn-toolbar>.input-group{margin-left:5px}.btn .caret,.btn-group>.btn:first-child{margin-left:0}.btn-group .dropdown-toggle:active,.btn-group.open .dropdown-toggle{outline:0}.btn-group>.btn+.dropdown-toggle{padding-right:8px;padding-left:8px}.btn-group>.btn-lg+.dropdown-toggle{padding-right:12px;padding-left:12px}.btn-group.open .dropdown-toggle{-webkit-box-shadow:inset 0 3px 5px rgba(0,0,0,.125);box-shadow:inset 0 3px 5px rgba(0,0,0,.125)}.btn-group.open .dropdown-toggle.btn-link{-webkit-box-shadow:none;box-shadow:none}.btn-lg .caret{border-width:5px 5px 0}.dropup .btn-lg .caret{border-width:0 5px 5px}");
			htmData.append(".btn-group-vertical>.btn,.btn-group-vertical>.btn-group,.btn-group-vertical>.btn-group>.btn{display:block;float:none;width:100%;max-width:100%}.btn-group-vertical>.btn-group>.btn{float:none}.btn-group-vertical>.btn+.btn,.btn-group-vertical>.btn+.btn-group,.btn-group-vertical>.btn-group+.btn,.btn-group-vertical>.btn-group+.btn-group{margin-top:-1px;margin-left:0}.btn-group-vertical>.btn:first-child:not(:last-child){border-radius:4px 4px 0 0}.btn-group-vertical>.btn:last-child:not(:first-child){border-radius:0 0 4px 4px}.btn-group-vertical>.btn-group:not(:first-child):not(:last-child)>.btn{border-radius:0}.btn-group-vertical>.btn-group:first-child:not(:last-child)>.btn:last-child,.btn-group-vertical>.btn-group:first-child:not(:last-child)>.dropdown-toggle{border-bottom-right-radius:0;border-bottom-left-radius:0}.btn-group-vertical>.btn-group:last-child:not(:first-child)>.btn:first-child{border-top-left-radius:0;border-top-right-radius:0}.btn-group-justified{display:table;width:100%;table-layout:fixed;border-collapse:separate}.btn-group-justified>.btn,.btn-group-justified>.btn-group{display:table-cell;float:none;width:1%}.btn-group-justified>.btn-group .btn{width:100%}.btn-group-justified>.btn-group .dropdown-menu{left:auto}[data-toggle=buttons]>.btn input[type=checkbox],[data-toggle=buttons]>.btn input[type=radio],[data-toggle=buttons]>.btn-group>.btn input[type=checkbox],[data-toggle=buttons]>.btn-group>.btn input[type=radio]{position:absolute;clip:rect(0,0,0,0);pointer-events:none}.input-group{position:relative;display:table;border-collapse:separate}");
			htmData.append(".input-group[class*=col-]{float:none;padding-right:0;padding-left:0}.input-group .form-control{position:relative;z-index:2;float:left;width:100%;margin-bottom:0}.input-group .form-control:focus{z-index:3}.input-group-lg>.form-control,.input-group-lg>.input-group-addon,.input-group-lg>.input-group-btn>.btn{height:46px;padding:10px 16px;font-size:18px;line-height:1.3333333;border-radius:6px}select.input-group-lg>.form-control,select.input-group-lg>.input-group-addon,select.input-group-lg>.input-group-btn>.btn{height:46px;line-height:46px}select[multiple].input-group-lg>.form-control,select[multiple].input-group-lg>.input-group-addon,select[multiple].input-group-lg>.input-group-btn>.btn,textarea.input-group-lg>.form-control,textarea.input-group-lg>.input-group-addon,textarea.input-group-lg>.input-group-btn>.btn{height:auto}.input-group-sm>.form-control,.input-group-sm>.input-group-addon,.input-group-sm>.input-group-btn>.btn{height:30px;padding:5px 10px;font-size:12px;line-height:1.5;border-radius:3px}select.input-group-sm>.form-control,select.input-group-sm>.input-group-addon,select.input-group-sm>.input-group-btn>.btn{height:30px;line-height:30px}select[multiple].input-group-sm>.form-control,select[multiple].input-group-sm>.input-group-addon,select[multiple].input-group-sm>.input-group-btn>.btn,textarea.input-group-sm>.form-control,textarea.input-group-sm>.input-group-addon,textarea.input-group-sm>.input-group-btn>.btn{height:auto}.input-group .form-control,.input-group-addon,.input-group-btn{display:table-cell}");
			htmData.append(".nav>li,.nav>li>a{display:block;position:relative}.input-group .form-control:not(:first-child):not(:last-child),.input-group-addon:not(:first-child):not(:last-child),.input-group-btn:not(:first-child):not(:last-child){border-radius:0}.input-group-addon,.input-group-btn{width:1%;white-space:nowrap;vertical-align:middle}.input-group-addon{padding:6px 12px;font-size:14px;font-weight:400;line-height:1;color:#555;text-align:center;background-color:#eee;border:1px solid #ccc;border-radius:4px}.input-group-addon.input-sm{padding:5px 10px;font-size:12px;border-radius:3px}.input-group-addon.input-lg{padding:10px 16px;font-size:18px;border-radius:6px}.input-group-addon input[type=checkbox],.input-group-addon input[type=radio]{margin-top:0}.input-group .form-control:first-child,.input-group-addon:first-child,.input-group-btn:first-child>.btn,.input-group-btn:first-child>.btn-group>.btn,.input-group-btn:first-child>.dropdown-toggle,.input-group-btn:last-child>.btn-group:not(:last-child)>.btn,.input-group-btn:last-child>.btn:not(:last-child):not(.dropdown-toggle){border-top-right-radius:0;border-bottom-right-radius:0}.input-group-addon:first-child{border-right:0}.input-group .form-control:last-child,.input-group-addon:last-child,.input-group-btn:first-child>.btn-group:not(:first-child)>.btn,.input-group-btn:first-child>.btn:not(:first-child),.input-group-btn:last-child>.btn,.input-group-btn:last-child>.btn-group>.btn,.input-group-btn:last-child>.dropdown-toggle{border-top-left-radius:0;border-bottom-left-radius:0}.input-group-addon:last-child{border-left:0}.input-group-btn{position:relative;font-size:0;white-space:nowrap}.input-group-btn>.btn{position:relative}.input-group-btn>.btn+.btn{margin-left:-1px}.input-group-btn>.btn:active,.input-group-btn>.btn:focus,.input-group-btn>.btn:hover{z-index:2}.input-group-btn:first-child>.btn,.input-group-btn:first-child>.btn-group{margin-right:-1px}.input-group-btn:last-child>.btn,.input-group-btn:last-child>.btn-group{z-index:2;margin-left:-1px}.nav{padding-left:0;margin-bottom:0;list-style:none}.nav>li>a{padding:10px 15px}.nav>li>a:focus,.nav>li>a:hover{text-decoration:none;background-color:#eee}.nav>li.disabled>a{color:#777}.nav>li.disabled>a:focus,.nav>li.disabled>a:hover{color:#777;text-decoration:none;cursor:not-allowed;background-color:transparent}.nav .open>a,.nav .open>a:focus,.nav .open>a:hover{background-color:#eee;border-color:#337ab7}.nav .nav-divider{height:1px;margin:9px 0;overflow:hidden;background-color:#e5e5e5}.nav>li>a>img{max-width:none}.nav-tabs{border-bottom:1px solid #ddd}.nav-tabs>li{float:left;margin-bottom:-1px}.nav-tabs>li>a{margin-right:2px;line-height:1.42857143;border:1px solid transparent;border-radius:4px 4px 0 0}.nav-tabs>li>a:hover{border-color:#eee #eee #ddd}.nav-tabs>li.active>a,.nav-tabs>li.active>a:focus,.nav-tabs>li.active>a:hover{color:#555;cursor:default;background-color:#fff;border:1px solid #ddd;border-bottom-color:transparent}.nav-tabs.nav-justified{width:100%;border-bottom:0}");
			htmData.append(".nav-tabs.nav-justified>li{float:none}.nav-tabs.nav-justified>li>a{margin-bottom:5px;text-align:center;margin-right:0;border-radius:4px}.nav-tabs.nav-justified>.active>a,.nav-tabs.nav-justified>.active>a:focus,.nav-tabs.nav-justified>.active>a:hover{border:1px solid #ddd}@media (min-width:768px){.nav-tabs.nav-justified>li{display:table-cell;width:1%}.nav-tabs.nav-justified>li>a{margin-bottom:0;border-bottom:1px solid #ddd;border-radius:4px 4px 0 0}.nav-tabs.nav-justified>.active>a,.nav-tabs.nav-justified>.active>a:focus,.nav-tabs.nav-justified>.active>a:hover{border-bottom-color:#fff}}.nav-pills>li{float:left}.nav-justified>li,.nav-stacked>li{float:none}.nav-pills>li>a{border-radius:4px}.nav-pills>li+li{margin-left:2px}.nav-pills>li.active>a,.nav-pills>li.active>a:focus,.nav-pills>li.active>a:hover{color:#fff;background-color:#337ab7}.nav-stacked>li+li{margin-top:2px;margin-left:0}.nav-justified{width:100%}.nav-justified>li>a{margin-bottom:5px;text-align:center}.nav-tabs-justified{border-bottom:0}.nav-tabs-justified>li>a{margin-right:0;border-radius:4px}.nav-tabs-justified>.active>a,.nav-tabs-justified>.active>a:focus,.nav-tabs-justified>.active>a:hover{border:1px solid #ddd}@media (min-width:768px){.nav-justified>li{display:table-cell;width:1%}.nav-justified>li>a{margin-bottom:0}.nav-tabs-justified>li>a{border-bottom:1px solid #ddd;border-radius:4px 4px 0 0}.nav-tabs-justified>.active>a,.nav-tabs-justified>.active>a:focus,.nav-tabs-justified>.active>a:hover{border-bottom-color:#fff}}.tab-content>.tab-pane{display:none}.tab-content>.active{display:block}.nav-tabs .dropdown-menu{margin-top:-1px;border-top-left-radius:0;border-top-right-radius:0}.navbar{position:relative;min-height:50px;margin-bottom:20px;border:1px solid transparent}.navbar-collapse{padding-right:15px;padding-left:15px;overflow-x:visible;-webkit-overflow-scrolling:touch;border-top:1px solid transparent;-webkit-box-shadow:inset 0 1px 0 rgba(255,255,255,.1);box-shadow:inset 0 1px 0 rgba(255,255,255,.1)}");
			htmData.append(".navbar-collapse.in{overflow-y:auto}@media (min-width:768px){.navbar{border-radius:4px}.navbar-header{float:left}.navbar-collapse{width:auto;border-top:0;-webkit-box-shadow:none;box-shadow:none}.navbar-collapse.collapse{display:block!important;height:auto!important;padding-bottom:0;overflow:visible!important}.navbar-collapse.in{overflow-y:visible}.navbar-fixed-bottom .navbar-collapse,.navbar-fixed-top .navbar-collapse,.navbar-static-top .navbar-collapse{padding-right:0;padding-left:0}}.carousel-inner,.embed-responsive,.modal,.modal-open,.progress{overflow:hidden}@media (max-device-width:480px) and (orientation:landscape){.navbar-fixed-bottom .navbar-collapse,.navbar-fixed-top .navbar-collapse{max-height:200px}}.container-fluid>.navbar-collapse,.container-fluid>.navbar-header,.container>.navbar-collapse,.container>.navbar-header{margin-right:-15px;margin-left:-15px}.navbar-static-top{z-index:1000;border-width:0 0 1px}.navbar-fixed-bottom,.navbar-fixed-top{position:fixed;right:0;left:0;z-index:1030}.navbar-fixed-top{top:0;border-width:0 0 1px}.navbar-fixed-bottom{bottom:0;margin-bottom:0;border-width:1px 0 0}.navbar-brand{float:left;height:50px;padding:15px;font-size:18px;line-height:20px}.navbar-brand:focus,.navbar-brand:hover{text-decoration:none}.navbar-brand>img{display:block}@media (min-width:768px){.container-fluid>.navbar-collapse,.container-fluid>.navbar-header,.container>.navbar-collapse,.container>.navbar-header{margin-right:0;margin-left:0}.navbar-fixed-bottom,.navbar-fixed-top,.navbar-static-top{border-radius:0}.navbar>.container .navbar-brand,.navbar>.container-fluid .navbar-brand{margin-left:-15px}}.navbar-toggle{position:relative;float:right;padding:9px 10px;margin-top:8px;margin-right:15px;margin-bottom:8px;background-color:transparent;border:1px solid transparent;border-radius:4px}.navbar-toggle:focus{outline:0}.navbar-toggle .icon-bar{display:block;width:22px;height:2px;border-radius:1px}.navbar-toggle .icon-bar+.icon-bar{margin-top:4px}.navbar-nav{margin:7.5px -15px}.navbar-nav>li>a{padding-top:10px;padding-bottom:10px;line-height:20px}@media (max-width:767px){.navbar-nav .open .dropdown-menu{position:static;float:none;width:auto;margin-top:0;background-color:transparent;border:0;-webkit-box-shadow:none;box-shadow:none}.navbar-nav .open .dropdown-menu .dropdown-header,.navbar-nav .open .dropdown-menu>li>a{padding:5px 15px 5px 25px}.navbar-nav .open .dropdown-menu>li>a{line-height:20px}.navbar-nav .open .dropdown-menu>li>a:focus,.navbar-nav .open .dropdown-menu>li>a:hover{background-image:none}}.progress-bar-striped,.progress-striped .progress-bar,.progress-striped .progress-bar-success{background-image:-webkit-linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent);background-image:-o-linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent ");
			htmData.append("75%,transparent)}@media (min-width:768px){.navbar-toggle{display:none}.navbar-nav{float:left;margin:0}.navbar-nav>li{float:left}.navbar-nav>li>a{padding-top:15px;padding-bottom:15px}}.navbar-form{padding:10px 15px;border-top:1px solid transparent;border-bottom:1px solid transparent;-webkit-box-shadow:inset 0 1px 0 rgba(255,255,255,.1),0 1px 0 rgba(255,255,255,.1);box-shadow:inset 0 1px 0 rgba(255,255,255,.1),0 1px 0 rgba(255,255,255,.1);margin:8px -15px}@media (min-width:768px){.navbar-form .form-control-static,.navbar-form .form-group{display:inline-block}.navbar-form .control-label,.navbar-form .form-group{margin-bottom:0;vertical-align:middle}.navbar-form .form-control{display:inline-block;width:auto;vertical-align:middle}.navbar-form .input-group{display:inline-table;vertical-align:middle}.navbar-form .input-group .form-control,.navbar-form .input-group .input-group-addon,.navbar-form .input-group .input-group-btn{width:auto}.navbar-form .input-group>.form-control{width:100%}.navbar-form .checkbox,.navbar-form .radio{display:inline-block;margin-top:0;margin-bottom:0;vertical-align:middle}.navbar-form .checkbox label,.navbar-form .radio label{padding-left:0}.navbar-form .checkbox input[type=checkbox],.navbar-form .radio input[type=radio]{position:relative;margin-left:0}.navbar-form .has-feedback .form-control-feedback{top:0}.navbar-form{width:auto;padding-top:0;padding-bottom:0;margin-right:0;margin-left:0;border:0;-webkit-box-shadow:none;box-shadow:none}}.breadcrumb>li,.pagination{display:inline-block}.btn .badge,.btn .label{top:-1px;position:relative}@media (max-width:767px){.navbar-form .form-group{margin-bottom:5px}.navbar-form .form-group:last-child{margin-bottom:0}}.navbar-nav>li>.dropdown-menu{margin-top:0;border-top-left-radius:0;border-top-right-radius:0}.navbar-fixed-bottom .navbar-nav>li>.dropdown-menu{margin-bottom:0;border-radius:4px 4px 0 0}.navbar-btn{margin-top:8px;margin-bottom:8px}.navbar-btn.btn-sm{margin-top:10px;margin-bottom:10px}.navbar-btn.btn-xs{margin-top:14px;margin-bottom:14px}.navbar-text{margin-top:15px;margin-bottom:15px}@media (min-width:768px){.navbar-text{float:left;margin-right:15px;margin-left:15px}.navbar-left{float:left!important}.navbar-right{float:right!important;margin-right:-15px}.navbar-right~.navbar-right{margin-right:0}}.navbar-default{background-color:#f8f8f8;border-color:#e7e7e7}.navbar-default .navbar-brand{color:#777}.navbar-default .navbar-brand:focus,.navbar-default .navbar-brand:hover{color:#5e5e5e;background-color:transparent}.navbar-default .navbar-nav>li>a,.navbar-default .navbar-text{color:#777}.navbar-default .navbar-nav>li>a:focus,.navbar-default .navbar-nav>li>a:hover{color:#333;background-color:transparent}.navbar-default .navbar-nav>.active>a,.navbar-default .navbar-nav>.active>a:focus,.navbar-default .navbar-nav>.active>a:hover{color:#555;background-color:#e7e7e7}.navbar-default .navbar-nav>.disabled>a,.navbar-default .navbar-nav>.disabled>a:focus,.navbar-default .navbar-nav>.disabled>");
			htmData.append("a:hover{color:#ccc;background-color:transparent}.navbar-default .navbar-toggle{border-color:#ddd}.navbar-default .navbar-toggle:focus,.navbar-default .navbar-toggle:hover{background-color:#ddd}.navbar-default .navbar-toggle .icon-bar{background-color:#888}.navbar-default .navbar-collapse,.navbar-default .navbar-form{border-color:#e7e7e7}.navbar-default .navbar-nav>.open>a,.navbar-default .navbar-nav>.open>a:focus,.navbar-default .navbar-nav>.open>a:hover{color:#555;background-color:#e7e7e7}@media (max-width:767px){.navbar-default .navbar-nav .open .dropdown-menu>li>a{color:#777}.navbar-default .navbar-nav .open .dropdown-menu>li>a:focus,.navbar-default .navbar-nav .open .dropdown-menu>li>a:hover{color:#333;background-color:transparent}.navbar-default .navbar-nav .open .dropdown-menu>.active>a,.navbar-default .navbar-nav .open .dropdown-menu>.active>a:focus,.navbar-default .navbar-nav .open .dropdown-menu>.active>a:hover{color:#555;background-color:#e7e7e7}.navbar-default .navbar-nav .open .dropdown-menu>.disabled>a,.navbar-default .navbar-nav .open .dropdown-menu>.disabled>a:focus,.navbar-default .navbar-nav .open .dropdown-menu>.disabled>a:hover{color:#ccc;background-color:transparent}}.navbar-default .navbar-link{color:#777}.navbar-default .navbar-link:hover{color:#333}.navbar-default .btn-link{color:#777}.navbar-default .btn-link:focus,.navbar-default .btn-link:hover{color:#333}.navbar-default .btn-link[disabled]:focus,.navbar-default .btn-link[disabled]:hover,fieldset[disabled] .navbar-default .btn-link:focus,fieldset[disabled] .navbar-default .btn-link:hover{color:#ccc}.navbar-inverse{background-color:"+
			strColorNavigation
			+";border-color:#080808}.navbar-inverse .navbar-brand{color:#9d9d9d}.navbar-inverse .navbar-brand:focus,.navbar-inverse .navbar-brand:hover{color:#fff;background-color:transparent}.navbar-inverse .navbar-nav>li>a,.navbar-inverse .navbar-text{color:#9d9d9d}.navbar-inverse .navbar-nav>li>a:focus,.navbar-inverse .navbar-nav>li>a:hover{color:#fff;background-color:transparent}.navbar-inverse .navbar-nav>.active>a,.navbar-inverse .navbar-nav>.active>a:focus,.navbar-inverse .navbar-nav>.active>a:hover{color:#fff;background-color:#080808}.navbar-inverse .navbar-nav>.disabled>a,.navbar-inverse .navbar-nav>.disabled>a:focus,.navbar-inverse .navbar-nav>.disabled>a:hover{color:#444;background-color:transparent}.navbar-inverse .navbar-toggle{border-color:#333}.navbar-inverse .navbar-toggle:focus,.navbar-inverse .navbar-toggle:hover{background-color:#333}.navbar-inverse .navbar-toggle .icon-bar{background-color:#fff}.navbar-inverse .navbar-collapse,.navbar-inverse .navbar-form{border-color:#101010}.navbar-inverse .navbar-nav>.open>a,.navbar-inverse .navbar-nav>.open>a:focus,.navbar-inverse .navbar-nav>.open>a:hover{color:#fff;background-color:#080808}@media (max-width:767px){.navbar-inverse .navbar-nav .open .dropdown-menu>.dropdown-header{border-color:#080808}.navbar-inverse .navbar-nav .open .dropdown-menu .divider{background-color:#080808}.navbar-inverse ");
			htmData.append(".navbar-nav .open .dropdown-menu>li>a{color:#9d9d9d}.navbar-inverse .navbar-nav .open .dropdown-menu>li>a:focus,.navbar-inverse .navbar-nav .open .dropdown-menu>li>a:hover{color:#fff;background-color:transparent}.navbar-inverse .navbar-nav .open .dropdown-menu>.active>a,.navbar-inverse .navbar-nav .open .dropdown-menu>.active>a:focus,.navbar-inverse .navbar-nav .open .dropdown-menu>.active>a:hover{color:#fff;background-color:#080808}.navbar-inverse .navbar-nav .open .dropdown-menu>.disabled>a,.navbar-inverse .navbar-nav .open .dropdown-menu>.disabled>a:focus,.navbar-inverse .navbar-nav .open .dropdown-menu>.disabled>a:hover{color:#444;background-color:transparent}}.navbar-inverse .navbar-link{color:#9d9d9d}.navbar-inverse .navbar-link:hover{color:#fff}.navbar-inverse .btn-link{color:#9d9d9d}.navbar-inverse .btn-link:focus,.navbar-inverse .btn-link:hover{color:#fff}.navbar-inverse .btn-link[disabled]:focus,.navbar-inverse .btn-link[disabled]:hover,fieldset[disabled] .navbar-inverse .btn-link:focus,fieldset[disabled] .navbar-inverse .btn-link:hover{color:#444}.breadcrumb{padding:8px 15px;margin-bottom:20px;list-style:none;background-color:#f5f5f5;border-radius:4px}.breadcrumb>li+li:before{padding:0 5px;color:#ccc;content:'/\\00a0'}.breadcrumb>.active{color:#777}.pagination{padding-left:0;margin:20px 0;border-radius:4px}.pager li,.pagination>li{display:inline}.pagination>li>a,.pagination>li>span{position:relative;float:left;padding:6px 12px;margin-left:-1px;line-height:1.42857143;color:#337ab7;text-decoration:none;background-color:#fff;border:1px solid #ddd}.pagination>li:first-child>a,.pagination>li:first-child>span{margin-left:0;border-top-left-radius:4px;border-bottom-left-radius:4px}.pagination>li:last-child>a,.pagination>li:last-child>span{border-top-right-radius:4px;border-bottom-right-radius:4px}.pagination>li>a:focus,.pagination>li>a:hover,.pagination>li>span:focus,.pagination>li>span:hover{z-index:2;color:#23527c;background-color:#eee;border-color:#ddd}.pagination>.active>a,.pagination>.active>a:focus,.pagination>.active>a:hover,.pagination>.active>span,.pagination>.active>span:focus,.pagination>.active>span:hover{z-index:3;color:#fff;cursor:default;background-color:#337ab7;border-color:#337ab7}.pagination>.disabled>a,.pagination>.disabled>a:focus,.pagination>.disabled>a:hover,.pagination>.disabled>span,.pagination>.disabled>span:focus,.pagination>.disabled>span:hover{color:#777;cursor:not-allowed;background-color:#fff;border-color:#ddd}.pagination-lg>li>a,.pagination-lg>li>span{padding:10px 16px;font-size:18px;line-height:1.3333333}.pagination-lg>li:first-child>a,.pagination-lg>li:first-child>span{border-top-left-radius:6px;border-bottom-left-radius:6px}.pagination-lg>li:last-child>a,.pagination-lg>li:last-child>span{border-top-right-radius:6px;border-bottom-right-radius:6px}.pagination-sm>li>a,.pagination-sm>li>span{padding:5px 10px;font-size:12px;line-height:1.5}.badge,.label{font-weight:700;line-height:1;white-space:nowrap;text-align:center}");
			htmData.append(".pagination-sm>li:first-child>a,.pagination-sm>li:first-child>span{border-top-left-radius:3px;border-bottom-left-radius:3px}.pagination-sm>li:last-child>a,.pagination-sm>li:last-child>span{border-top-right-radius:3px;border-bottom-right-radius:3px}.pager{padding-left:0;margin:20px 0;text-align:center;list-style:none}.pager li>a,.pager li>span{display:inline-block;padding:5px 14px;background-color:#fff;border:1px solid #ddd;border-radius:15px}.pager li>a:focus,.pager li>a:hover{text-decoration:none;background-color:#eee}.pager .next>a,.pager .next>span{float:right}.pager .previous>a,.pager .previous>span{float:left}.pager .disabled>a,.pager .disabled>a:focus,.pager .disabled>a:hover,.pager .disabled>span{color:#777;cursor:not-allowed;background-color:#fff}.label{display:inline;padding:.3em .6em .3em;font-size:85%;color:#fff;border-radius:.25em}a.label:focus,a.label:hover{color:#fff;text-decoration:none;cursor:pointer}.label:empty{display:none}.label-default{background-color:#777}.label-default[href]:focus,.label-default[href]:hover{background-color:#5e5e5e}.label-primary{background-color:#337ab7}.label-primary[href]:focus,.label-primary[href]:hover{background-color:#286090}.label-success{background-color:#5cb85c}.label-success[href]:focus,.label-success[href]:hover{background-color:#449d44}.label-info{background-color:#5bc0de}.label-info[href]:focus,.label-info[href]:hover{background-color:#31b0d5}.label-warning{background-color:#f0ad4e}.label-warning[href]:focus,.label-warning[href]:hover{background-color:#ec971f}.label-danger{background-color:#d9534f}.label-danger[href]:focus,.label-danger[href]:hover{background-color:#c9302c}.badge{display:inline-block;min-width:10px;padding:3px 7px;color:#fff;vertical-align:middle;background-color:#777;border-radius:1em}.badge:empty{display:none}.media-object,.thumbnail{display:block}.btn-group-xs>.btn .badge,.btn-xs .badge{top:0;padding:1px 5px}a.badge:focus,a.badge:hover{color:#fff;text-decoration:none;cursor:pointer}.list-group-item.active>.badge,.nav-pills>.active>a>.badge{color:#337ab7;background-color:#fff}.jumbotron,.jumbotron .h1,.jumbotron h1{color:inherit}.list-group-item>.badge{float:right}.list-group-item>.badge+.badge{margin-right:5px}.nav-pills>li>a>.badge{margin-left:3px}.jumbotron{padding-top:30px;padding-bottom:30px;margin-bottom:30px;background-color:#eee}.jumbotron p{margin-bottom:15px;font-size:21px;font-weight:200}.alert,.thumbnail{margin-bottom:20px}.alert .alert-link,.close{font-weight:700}.jumbotron>hr{border-top-color:#d5d5d5}.container .jumbotron,.container-fluid .jumbotron{padding-right:15px;padding-left:15px;border-radius:6px}.jumbotron .container{max-width:100%}@media screen and (min-width:768px){.jumbotron{padding-top:48px;padding-bottom:48px}.container .jumbotron,.container-fluid .jumbotron{padding-right:60px;padding-left:60px}.jumbotron .h1,.jumbotron h1{font-size:63px}}.thumbnail{padding:4px;line-height:1.42857143;background-color:#fff;border:1px solid #ddd;border-radi");
			htmData.append("us:4px;-webkit-transition:border .2s ease-in-out;-o-transition:border .2s ease-in-out;transition:border .2s ease-in-out}.thumbnail a>img,.thumbnail>img{margin-right:auto;margin-left:auto}a.thumbnail.active,a.thumbnail:focus,a.thumbnail:hover{border-color:#337ab7}.thumbnail .caption{padding:9px;color:#333}.alert{padding:15px;border:1px solid transparent;border-radius:4px}.alert h4{margin-top:0;color:inherit}.alert>p,.alert>ul{margin-bottom:0}.alert>p+p{margin-top:5px}.alert-dismissable,.alert-dismissible{padding-right:35px}.alert-dismissable .close,.alert-dismissible .close{position:relative;top:-2px;right:-21px;color:inherit}.modal,.modal-backdrop{top:0;right:0;bottom:0;left:0}.alert-success{color:#3c763d;background-color:#dff0d8;border-color:#d6e9c6}.alert-success hr{border-top-color:#c9e2b3}.alert-success .alert-link{color:#2b542c}.alert-info{color:#31708f;background-color:#d9edf7;border-color:#bce8f1}.alert-info hr{border-top-color:#a6e1ec}.alert-info .alert-link{color:#245269}.alert-warning{color:#8a6d3b;background-color:#fcf8e3;border-color:#faebcc}.alert-warning hr{border-top-color:#f7e1b5}.alert-warning .alert-link{color:#66512c}.alert-danger{color:#a94442;background-color:#f2dede;border-color:#ebccd1}.alert-danger hr{border-top-color:#e4b9c0}.alert-danger .alert-link{color:#843534}@-webkit-keyframes progress-bar-stripes{from{background-position:40px 0}to{background-position:0 0}}@-o-keyframes progress-bar-stripes{from{background-position:40px 0}to{background-position:0 0}}@keyframes progress-bar-stripes{from{background-position:40px 0}to{background-position:0 0}}.progress{height:20px;margin-bottom:20px;background-color:#f5f5f5;border-radius:4px;-webkit-box-shadow:inset 0 1px 2px rgba(0,0,0,.1);box-shadow:inset 0 1px 2px rgba(0,0,0,.1)}.progress-bar{float:left;width:0;height:100%;font-size:12px;line-height:20px;color:#fff;text-align:center;background-color:#337ab7;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.15);box-shadow:inset 0 -1px 0 rgba(0,0,0,.15);-webkit-transition:width .6s ease;-o-transition:width .6s ease;transition:width .6s ease}.progress-bar-striped,.progress-striped .progress-bar{background-image:linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent);-webkit-background-size:40px 40px;background-size:40px 40px}.progress-bar.active,.progress.active .progress-bar{-webkit-animation:progress-bar-stripes 2s linear infinite;-o-animation:progress-bar-stripes 2s linear infinite;animation:progress-bar-stripes 2s linear infinite}.progress-bar-success{background-color:#5cb85c}.progress-striped .progress-bar-success{background-image:linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent)}.progress-striped .progress-bar-info,.progress-striped .progress-bar-warning{background-image:-webkit-linear-gradient(45deg,rgba(255,255,255,.15) 25%,");
			htmData.append("transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent);background-image:-o-linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent)}.progress-bar-info{background-color:#5bc0de}.progress-striped .progress-bar-info{background-image:linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent)}.progress-bar-warning{background-color:#f0ad4e}.progress-striped .progress-bar-warning{background-image:linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent)}.progress-bar-danger{background-color:#d9534f}.progress-striped .progress-bar-danger{background-image:-webkit-linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent);background-image:-o-linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent);background-image:linear-gradient(45deg,rgba(255,255,255,.15) 25%,transparent 25%,transparent 50%,rgba(255,255,255,.15) 50%,rgba(255,255,255,.15) 75%,transparent 75%,transparent)}.media{margin-top:15px}.media:first-child{margin-top:0}.media,.media-body{overflow:hidden;zoom:1}.media-body{width:10000px}.media-object.img-thumbnail{max-width:none}.media-right,.media>.pull-right{padding-left:10px}.media-left,.media>.pull-left{padding-right:10px}.media-body,.media-left,.media-right{display:table-cell;vertical-align:top}.media-middle{vertical-align:middle}.media-bottom{vertical-align:bottom}.media-heading{margin-top:0;margin-bottom:5px}.media-list{padding-left:0;list-style:none}.list-group{padding-left:0;margin-bottom:20px}.list-group-item{position:relative;display:block;padding:10px 15px;margin-bottom:-1px;background-color:#fff;border:1px solid #ddd}.list-group-item:first-child{border-top-left-radius:4px;border-top-right-radius:4px}.list-group-item:last-child{margin-bottom:0;border-bottom-right-radius:4px;border-bottom-left-radius:4px}a.list-group-item,button.list-group-item{color:#555}a.list-group-item .list-group-item-heading,button.list-group-item .list-group-item-heading{color:#333}a.list-group-item:focus,a.list-group-item:hover,button.list-group-item:focus,button.list-group-item:hover{color:#555;text-decoration:none;background-color:#f5f5f5}button.list-group-item{width:100%;text-align:left}.list-group-item.disabled,.list-group-item.disabled:focus,.list-group-item.disabled:hover{color:#777;cursor:not-allowed;background-color:#eee}.list-group-item.disabled .list-group-item-heading,.list-group-item.disabled:focus .list-group-item-heading,.list-group-item.disabled:hover .list-group-item-heading{color:inherit}");
			htmData.append(".list-group-item.disabled .list-group-item-text,.list-group-item.disabled:focus .list-group-item-text,.list-group-item.disabled:hover .list-group-item-text{color:#777}.list-group-item.active,.list-group-item.active:focus,.list-group-item.active:hover{z-index:2;color:#fff;background-color:#337ab7;border-color:#337ab7}.list-group-item.active .list-group-item-heading,.list-group-item.active .list-group-item-heading>.small,.list-group-item.active .list-group-item-heading>small,.list-group-item.active:focus .list-group-item-heading,.list-group-item.active:focus .list-group-item-heading>.small,.list-group-item.active:focus .list-group-item-heading>small,.list-group-item.active:hover .list-group-item-heading,.list-group-item.active:hover .list-group-item-heading>.small,.list-group-item.active:hover .list-group-item-heading>small{color:inherit}.list-group-item.active .list-group-item-text,.list-group-item.active:focus .list-group-item-text,.list-group-item.active:hover .list-group-item-text{color:#c7ddef}.list-group-item-success{color:#3c763d;background-color:#dff0d8}a.list-group-item-success,button.list-group-item-success{color:#3c763d}a.list-group-item-success .list-group-item-heading,button.list-group-item-success .list-group-item-heading{color:inherit}a.list-group-item-success:focus,a.list-group-item-success:hover,button.list-group-item-success:focus,button.list-group-item-success:hover{color:#3c763d;background-color:#d0e9c6}a.list-group-item-success.active,a.list-group-item-success.active:focus,a.list-group-item-success.active:hover,button.list-group-item-success.active,button.list-group-item-success.active:focus,button.list-group-item-success.active:hover{color:#fff;background-color:#3c763d;border-color:#3c763d}.list-group-item-info{color:#31708f;background-color:#d9edf7}a.list-group-item-info,button.list-group-item-info{color:#31708f}a.list-group-item-info .list-group-item-heading,button.list-group-item-info .list-group-item-heading{color:inherit}a.list-group-item-info:focus,a.list-group-item-info:hover,button.list-group-item-info:focus,button.list-group-item-info:hover{color:#31708f;background-color:#c4e3f3}a.list-group-item-info.active,a.list-group-item-info.active:focus,a.list-group-item-info.active:hover,button.list-group-item-info.active,button.list-group-item-info.active:focus,button.list-group-item-info.active:hover{color:#fff;background-color:#31708f;border-color:#31708f}.list-group-item-warning{color:#8a6d3b;background-color:#fcf8e3}a.list-group-item-warning,button.list-group-item-warning{color:#8a6d3b}a.list-group-item-warning .list-group-item-heading,button.list-group-item-warning .list-group-item-heading{color:inherit}a.list-group-item-warning:focus,a.list-group-item-warning:hover,button.list-group-item-warning:focus,button.list-group-item-warning:hover{color:#8a6d3b;background-color:#faf2cc}a.list-group-item-warning.active,a.list-group-item-warning.active:focus,a.list-group-item-warning.active:hover,button.list-group-item-warning.active,");
			htmData.append("button.list-group-item-warning.active:focus,button.list-group-item-warning.active:hover{color:#fff;background-color:#8a6d3b;border-color:#8a6d3b}.list-group-item-danger{color:#a94442;background-color:#f2dede}a.list-group-item-danger,button.list-group-item-danger{color:#a94442}a.list-group-item-danger .list-group-item-heading,button.list-group-item-danger .list-group-item-heading{color:inherit}a.list-group-item-danger:focus,a.list-group-item-danger:hover,button.list-group-item-danger:focus,button.list-group-item-danger:hover{color:#a94442;background-color:#ebcccc}a.list-group-item-danger.active,a.list-group-item-danger.active:focus,a.list-group-item-danger.active:hover,button.list-group-item-danger.active,button.list-group-item-danger.active:focus,button.list-group-item-danger.active:hover{color:#fff;background-color:#a94442;border-color:#a94442}.panel-heading>.dropdown .dropdown-toggle,.panel-title,.panel-title>.small,.panel-title>.small>a,.panel-title>a,.panel-title>small,.panel-title>small>a{color:inherit}.list-group-item-heading{margin-top:0;margin-bottom:5px}.list-group-item-text{margin-bottom:0;line-height:1.3}.panel{margin-bottom:20px;background-color:#fff;border:1px solid transparent;border-radius:4px;-webkit-box-shadow:0 1px 1px rgba(0,0,0,.05);box-shadow:0 1px 1px rgba(0,0,0,.05)}.panel-title,.panel>.list-group,.panel>.panel-collapse>.list-group,.panel>.panel-collapse>.table,.panel>.table,.panel>.table-responsive>.table{margin-bottom:0}.panel-body{padding:15px}.panel-heading{padding:10px 15px;border-bottom:1px solid transparent;border-top-left-radius:3px;border-top-right-radius:3px}.panel-title{margin-top:0;font-size:16px}.panel-footer{padding:10px 15px;background-color:#f5f5f5;border-top:1px solid #ddd;border-bottom-right-radius:3px;border-bottom-left-radius:3px}.panel>.list-group .list-group-item,.panel>.panel-collapse>.list-group .list-group-item{border-width:1px 0;border-radius:0}.panel-group .panel-heading,.panel>.table-bordered>tbody>tr:first-child>td,.panel>.table-bordered>tbody>tr:first-child>th,.panel>.table-bordered>tbody>tr:last-child>td,.panel>.table-bordered>tbody>tr:last-child>th,.panel>.table-bordered>tfoot>tr:last-child>td,.panel>.table-bordered>tfoot>tr:last-child>th,.panel>.table-bordered>thead>tr:first-child>td,.panel>.table-bordered>thead>tr:first-child>th,.panel>.table-responsive>.table-bordered>tbody>tr:first-child>td,.panel>.table-responsive>.table-bordered>tbody>tr:first-child>th,.panel>.table-responsive>.table-bordered>tbody>tr:last-child>td,.panel>.table-responsive>.table-bordered>tbody>tr:last-child>th,.panel>.table-responsive>.table-bordered>tfoot>tr:last-child>td,.panel>.table-responsive>.table-bordered>tfoot>tr:last-child>th,.panel>.table-responsive>.table-bordered>thead>tr:first-child>td,.panel>.table-responsive>.table-bordered>thead>tr:first-child>th{border-bottom:0}.panel>.list-group:first-child .list-group-item:first-child,.panel>.panel-collapse>.list-group:first-child .list-group-item:first-child{border-top:0;");
			htmData.append("border-top-left-radius:3px;border-top-right-radius:3px}.panel>.list-group:last-child .list-group-item:last-child,.panel>.panel-collapse>.list-group:last-child .list-group-item:last-child{border-bottom:0;border-bottom-right-radius:3px;border-bottom-left-radius:3px}.panel>.panel-heading+.panel-collapse>.list-group .list-group-item:first-child{border-top-left-radius:0;border-top-right-radius:0}.list-group+.panel-footer,.panel-heading+.list-group .list-group-item:first-child{border-top-width:0}.panel>.panel-collapse>.table caption,.panel>.table caption,.panel>.table-responsive>.table caption{padding-right:15px;padding-left:15px}.panel>.table-responsive:first-child>.table:first-child,.panel>.table-responsive:first-child>.table:first-child>tbody:first-child>tr:first-child,.panel>.table-responsive:first-child>.table:first-child>thead:first-child>tr:first-child,.panel>.table:first-child,.panel>.table:first-child>tbody:first-child>tr:first-child,.panel>.table:first-child>thead:first-child>tr:first-child{border-top-left-radius:3px;border-top-right-radius:3px}.panel>.table-responsive:first-child>.table:first-child>tbody:first-child>tr:first-child td:first-child,.panel>.table-responsive:first-child>.table:first-child>tbody:first-child>tr:first-child th:first-child,.panel>.table-responsive:first-child>.table:first-child>thead:first-child>tr:first-child td:first-child,.panel>.table-responsive:first-child>.table:first-child>thead:first-child>tr:first-child th:first-child,.panel>.table:first-child>tbody:first-child>tr:first-child td:first-child,.panel>.table:first-child>tbody:first-child>tr:first-child th:first-child,.panel>.table:first-child>thead:first-child>tr:first-child td:first-child,.panel>.table:first-child>thead:first-child>tr:first-child th:first-child{border-top-left-radius:3px}.panel>.table-responsive:first-child>.table:first-child>tbody:first-child>tr:first-child td:last-child,.panel>.table-responsive:first-child>.table:first-child>tbody:first-child>tr:first-child th:last-child,.panel>.table-responsive:first-child>.table:first-child>thead:first-child>tr:first-child td:last-child,.panel>.table-responsive:first-child>.table:first-child>thead:first-child>tr:first-child th:last-child,.panel>.table:first-child>tbody:first-child>tr:first-child td:last-child,.panel>.table:first-child>tbody:first-child>tr:first-child th:last-child,.panel>.table:first-child>thead:first-child>tr:first-child td:last-child,.panel>.table:first-child>thead:first-child>tr:first-child th:last-child{border-top-right-radius:3px}.panel>.table-responsive:last-child>.table:last-child,.panel>.table-responsive:last-child>.table:last-child>tbody:last-child>tr:last-child,.panel>.table-responsive:last-child>.table:last-child>tfoot:last-child>tr:last-child,.panel>.table:last-child,.panel>.table:last-child>tbody:last-child>tr:last-child,.panel>.table:last-child>tfoot:last-child>tr:last-child{border-bottom-right-radius:3px;border-bottom-left-radius:3px}.panel>.table-responsive:last-child>.table:last-child>");
			htmData.append("tbody:last-child>tr:last-child td:first-child,.panel>.table-responsive:last-child>.table:last-child>tbody:last-child>tr:last-child th:first-child,.panel>.table-responsive:last-child>.table:last-child>tfoot:last-child>tr:last-child td:first-child,.panel>.table-responsive:last-child>.table:last-child>tfoot:last-child>tr:last-child th:first-child,.panel>.table:last-child>tbody:last-child>tr:last-child td:first-child,.panel>.table:last-child>tbody:last-child>tr:last-child th:first-child,.panel>.table:last-child>tfoot:last-child>tr:last-child td:first-child,.panel>.table:last-child>tfoot:last-child>tr:last-child th:first-child{border-bottom-left-radius:3px}.panel>.table-responsive:last-child>.table:last-child>tbody:last-child>tr:last-child td:last-child,.panel>.table-responsive:last-child>.table:last-child>tbody:last-child>tr:last-child th:last-child,.panel>.table-responsive:last-child>.table:last-child>tfoot:last-child>tr:last-child td:last-child,.panel>.table-responsive:last-child>.table:last-child>tfoot:last-child>tr:last-child th:last-child,.panel>.table:last-child>tbody:last-child>tr:last-child td:last-child,.panel>.table:last-child>tbody:last-child>tr:last-child th:last-child,.panel>.table:last-child>tfoot:last-child>tr:last-child td:last-child,.panel>.table:last-child>tfoot:last-child>tr:last-child th:last-child{border-bottom-right-radius:3px}.panel>.panel-body+.table,.panel>.panel-body+.table-responsive,.panel>.table+.panel-body,.panel>.table-responsive+.panel-body{border-top:1px solid #ddd}.panel>.table>tbody:first-child>tr:first-child td,.panel>.table>tbody:first-child>tr:first-child th{border-top:0}.panel>.table-bordered,.panel>.table-responsive>.table-bordered{border:0}.panel>.table-bordered>tbody>tr>td:first-child,.panel>.table-bordered>tbody>tr>th:first-child,.panel>.table-bordered>tfoot>tr>td:first-child,.panel>.table-bordered>tfoot>tr>th:first-child,.panel>.table-bordered>thead>tr>td:first-child,.panel>.table-bordered>thead>tr>th:first-child,.panel>.table-responsive>.table-bordered>tbody>tr>td:first-child,.panel>.table-responsive>.table-bordered>tbody>tr>th:first-child,.panel>.table-responsive>.table-bordered>tfoot>tr>td:first-child,.panel>.table-responsive>.table-bordered>tfoot>tr>th:first-child,.panel>.table-responsive>.table-bordered>thead>tr>td:first-child,.panel>.table-responsive>.table-bordered>thead>tr>th:first-child{border-left:0}.panel>.table-bordered>tbody>tr>td:last-child,.panel>.table-bordered>tbody>tr>th:last-child,.panel>.table-bordered>tfoot>tr>td:last-child,.panel>.table-bordered>tfoot>tr>th:last-child,.panel>.table-bordered>thead>tr>td:last-child,.panel>.table-bordered>thead>tr>th:last-child,.panel>.table-responsive>.table-bordered>tbody>tr>td:last-child,.panel>.table-responsive>.table-bordered>tbody>tr>th:last-child,.panel>.table-responsive>.table-bordered>tfoot>tr>td:last-child,.panel>.table-responsive>.table-bordered>tfoot>tr>th:last-child,.panel>.table-responsive>.table-bordered>thead>tr>td:last-child,.panel>.table-responsive>");
			htmData.append(".table-bordered>thead>tr>th:last-child{border-right:0}.panel>.table-responsive{margin-bottom:0;border:0}.panel-group{margin-bottom:20px}.panel-group .panel{margin-bottom:0;border-radius:4px}.panel-group .panel+.panel{margin-top:5px}.panel-group .panel-heading+.panel-collapse>.list-group,.panel-group .panel-heading+.panel-collapse>.panel-body{border-top:1px solid #ddd}.panel-group .panel-footer{border-top:0}.panel-group .panel-footer+.panel-collapse .panel-body{border-bottom:1px solid #ddd}.panel-default{border-color:#ddd}.panel-default>.panel-heading{color:#333;background-color:#f5f5f5;border-color:#ddd}.panel-default>.panel-heading+.panel-collapse>.panel-body{border-top-color:#ddd}.panel-default>.panel-heading .badge{color:#f5f5f5;background-color:#333}.panel-default>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#ddd}.panel-primary{border-color:#337ab7}.panel-primary>.panel-heading{color:#fff;background-color:#337ab7;border-color:#337ab7}.panel-primary>.panel-heading+.panel-collapse>.panel-body{border-top-color:#337ab7}.panel-primary>.panel-heading .badge{color:#337ab7;background-color:#fff}.panel-primary>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#337ab7}.panel-success{border-color:#d6e9c6}.panel-success>.panel-heading{color:#3c763d;background-color:#dff0d8;border-color:#d6e9c6}.panel-success>.panel-heading+.panel-collapse>.panel-body{border-top-color:#d6e9c6}.panel-success>.panel-heading .badge{color:#dff0d8;background-color:#3c763d}.panel-success>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#d6e9c6}.panel-info{border-color:#bce8f1}.panel-info>.panel-heading{color:#31708f;background-color:#d9edf7;border-color:#bce8f1}.panel-info>.panel-heading+.panel-collapse>.panel-body{border-top-color:#bce8f1}.panel-info>.panel-heading .badge{color:#d9edf7;background-color:#31708f}.panel-info>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#bce8f1}.panel-warning{border-color:#faebcc}.panel-warning>.panel-heading{color:#8a6d3b;background-color:#fcf8e3;border-color:#faebcc}.panel-warning>.panel-heading+.panel-collapse>.panel-body{border-top-color:#faebcc}.panel-warning>.panel-heading .badge{color:#fcf8e3;background-color:#8a6d3b}.panel-warning>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#faebcc}.panel-danger{border-color:#ebccd1}.panel-danger>.panel-heading{color:#a94442;background-color:#f2dede;border-color:#ebccd1}.panel-danger>.panel-heading+.panel-collapse>.panel-body{border-top-color:#ebccd1}.panel-danger>.panel-heading .badge{color:#f2dede;background-color:#a94442}.panel-danger>.panel-footer+.panel-collapse>.panel-body{border-bottom-color:#ebccd1}.embed-responsive{position:relative;display:block;height:0;padding:0}.embed-responsive .embed-responsive-item,.embed-responsive embed,.embed-responsive iframe,.embed-responsive object,.embed-responsive video{position:absolute;top:0;bottom:0;left:0;width:100%;height:100%;border:0}.embed-responsive-16by9{padding-bottom:56.25%}");
			htmData.append(".embed-responsive-4by3{padding-bottom:75%}.well{min-height:20px;padding:19px;margin-bottom:20px;background-color:#f5f5f5;border:1px solid #e3e3e3;border-radius:4px;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.05);box-shadow:inset 0 1px 1px rgba(0,0,0,.05)}.well blockquote{border-color:#ddd;border-color:rgba(0,0,0,.15)}.well-lg{padding:24px;border-radius:6px}.well-sm{padding:9px;border-radius:3px}.close{float:right;font-size:21px;line-height:1;color:#000;text-shadow:0 1px 0 #fff;filter:alpha(opacity=20);opacity:.2}.popover,.tooltip{font-family:'Helvetica Neue',Helvetica,Arial,sans-serif;font-style:normal;font-weight:400;line-height:1.42857143;text-shadow:none;text-transform:none;letter-spacing:normal;word-break:normal;word-spacing:normal;word-wrap:normal;white-space:normal;line-break:auto;text-decoration:none}.close:focus,.close:hover{color:#000;text-decoration:none;cursor:pointer;filter:alpha(opacity=50);opacity:.5}button.close{-webkit-appearance:none;padding:0;cursor:pointer;background:0 0;border:0}.modal{position:fixed;z-index:1050;display:none;-webkit-overflow-scrolling:touch;outline:0}.modal.fade .modal-dialog{-webkit-transition:-webkit-transform .3s ease-out;-o-transition:-o-transform .3s ease-out;transition:transform .3s ease-out;-webkit-transform:translate(0,-25%);-ms-transform:translate(0,-25%);-o-transform:translate(0,-25%);transform:translate(0,-25%)}.modal.in .modal-dialog{-webkit-transform:translate(0,0);-ms-transform:translate(0,0);-o-transform:translate(0,0);transform:translate(0,0)}.modal-open .modal{overflow-x:hidden;overflow-y:auto}.modal-dialog{position:relative;width:auto;margin:10px}.modal-content{position:relative;background-color:#fff;background-clip:padding-box;border:1px solid #999;border:1px solid rgba(0,0,0,.2);border-radius:6px;outline:0;-webkit-box-shadow:0 3px 9px rgba(0,0,0,.5);box-shadow:0 3px 9px rgba(0,0,0,.5)}.modal-backdrop{position:fixed;z-index:1040;background-color:#000}.modal-backdrop.fade{filter:alpha(opacity=0);opacity:0}.carousel-control,.modal-backdrop.in{filter:alpha(opacity=50);opacity:.5}.modal-header{padding:15px;border-bottom:1px solid #e5e5e5}.modal-header .close{margin-top:-2px}.modal-title{margin:0;line-height:1.42857143}.modal-body{position:relative;padding:15px}.modal-footer{padding:15px;text-align:right;border-top:1px solid #e5e5e5}.modal-footer .btn+.btn{margin-bottom:0;margin-left:5px}.modal-footer .btn-group .btn+.btn{margin-left:-1px}.modal-footer .btn-block+.btn-block{margin-left:0}.modal-scrollbar-measure{position:absolute;top:-9999px;width:50px;height:50px;overflow:scroll}@media (min-width:768px){.modal-dialog{width:600px;margin:30px auto}.modal-content{-webkit-box-shadow:0 5px 15px rgba(0,0,0,.5);box-shadow:0 5px 15px rgba(0,0,0,.5)}.modal-sm{width:300px}}.tooltip.top-left .tooltip-arrow,.tooltip.top-right .tooltip-arrow{bottom:0;margin-bottom:-5px;border-width:5px 5px 0;border-top-color:#000}@media (min-width:992px){.modal-lg{width:900px}}.tooltip{position:absolute;z-index:1070;");
			htmData.append("display:block;font-size:12px;text-align:left;text-align:start;filter:alpha(opacity=0);opacity:0}.tooltip.in{filter:alpha(opacity=90);opacity:.9}.tooltip.top{padding:5px 0;margin-top:-3px}.tooltip.right{padding:0 5px;margin-left:3px}.tooltip.bottom{padding:5px 0;margin-top:3px}.tooltip.left{padding:0 5px;margin-left:-3px}.tooltip-inner{max-width:200px;padding:3px 8px;color:#fff;text-align:center;background-color:#000;border-radius:4px}.tooltip-arrow{position:absolute;width:0;height:0;border-color:transparent;border-style:solid}.tooltip.top .tooltip-arrow{bottom:0;left:50%;margin-left:-5px;border-width:5px 5px 0;border-top-color:#000}.tooltip.top-left .tooltip-arrow{right:5px}.tooltip.top-right .tooltip-arrow{left:5px}.tooltip.right .tooltip-arrow{top:50%;left:0;margin-top:-5px;border-width:5px 5px 5px 0;border-right-color:#000}.tooltip.left .tooltip-arrow{top:50%;right:0;margin-top:-5px;border-width:5px 0 5px 5px;border-left-color:#000}.tooltip.bottom .tooltip-arrow,.tooltip.bottom-left .tooltip-arrow,.tooltip.bottom-right .tooltip-arrow{border-width:0 5px 5px;border-bottom-color:#000;top:0}.tooltip.bottom .tooltip-arrow{left:50%;margin-left:-5px}.tooltip.bottom-left .tooltip-arrow{right:5px;margin-top:-5px}.tooltip.bottom-right .tooltip-arrow{left:5px;margin-top:-5px}.popover{position:absolute;top:0;left:0;z-index:1060;display:none;max-width:276px;padding:1px;font-size:14px;text-align:left;text-align:start;background-color:#fff;-webkit-background-clip:padding-box;background-clip:padding-box;border:1px solid #ccc;border:1px solid rgba(0,0,0,.2);border-radius:6px;-webkit-box-shadow:0 5px 10px rgba(0,0,0,.2);box-shadow:0 5px 10px rgba(0,0,0,.2)}.carousel-caption,.carousel-control{color:#fff;text-align:center;text-shadow:0 1px 2px rgba(0,0,0,.6)}.popover.top{margin-top:-10px}.popover.right{margin-left:10px}.popover.bottom{margin-top:10px}.popover.left{margin-left:-10px}.popover-title{padding:8px 14px;margin:0;font-size:14px;background-color:#f7f7f7;border-bottom:1px solid #ebebeb;border-radius:5px 5px 0 0}.popover-content{padding:9px 14px}.popover>.arrow,.popover>.arrow:after{position:absolute;display:block;width:0;height:0;border-color:transparent;border-style:solid}.carousel,.carousel-inner{position:relative}.popover>.arrow{border-width:11px}.popover>.arrow:after{content:'';border-width:10px}.popover.top>.arrow{bottom:-11px;left:50%;margin-left:-11px;border-top-color:#999;border-top-color:rgba(0,0,0,.25);border-bottom-width:0}.popover.top>.arrow:after{bottom:1px;margin-left:-10px;content:' ';border-top-color:#fff;border-bottom-width:0}.popover.left>.arrow:after,.popover.right>.arrow:after{bottom:-10px;content:' '}.popover.right>.arrow{top:50%;left:-11px;margin-top:-11px;border-right-color:#999;border-right-color:rgba(0,0,0,.25);border-left-width:0}.popover.right>.arrow:after{left:1px;border-right-color:#fff;border-left-width:0}.popover.bottom>.arrow{top:-11px;left:50%;margin-left:-11px;border-top-width:0;border-bottom-color:#999;border-bottom-color:");
			htmData.append("rgba(0,0,0,.25)}.popover.bottom>.arrow:after{top:1px;margin-left:-10px;content:' ';border-top-width:0;border-bottom-color:#fff}.popover.left>.arrow{top:50%;right:-11px;margin-top:-11px;border-right-width:0;border-left-color:#999;border-left-color:rgba(0,0,0,.25)}.popover.left>.arrow:after{right:1px;border-right-width:0;border-left-color:#fff}.carousel-inner{width:100%}.carousel-inner>.item{position:relative;display:none;-webkit-transition:.6s ease-in-out left;-o-transition:.6s ease-in-out left;transition:.6s ease-in-out left}.carousel-inner>.item>a>img,.carousel-inner>.item>img{line-height:1}@media all and (transform-3d),(-webkit-transform-3d){.carousel-inner>.item{-webkit-transition:-webkit-transform .6s ease-in-out;-o-transition:-o-transform .6s ease-in-out;transition:transform .6s ease-in-out;-webkit-backface-visibility:hidden;backface-visibility:hidden;-webkit-perspective:1000px;perspective:1000px}.carousel-inner>.item.active.right,.carousel-inner>.item.next{left:0;-webkit-transform:translate3d(100%,0,0);transform:translate3d(100%,0,0)}.carousel-inner>.item.active.left,.carousel-inner>.item.prev{left:0;-webkit-transform:translate3d(-100%,0,0);transform:translate3d(-100%,0,0)}.carousel-inner>.item.active,.carousel-inner>.item.next.left,.carousel-inner>.item.prev.right{left:0;-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0)}}.carousel-inner>.active,.carousel-inner>.next,.carousel-inner>.prev{display:block}.carousel-inner>.active{left:0}.carousel-inner>.next,.carousel-inner>.prev{position:absolute;top:0;width:100%}.carousel-inner>.next{left:100%}.carousel-inner>.prev{left:-100%}.carousel-inner>.next.left,.carousel-inner>.prev.right{left:0}.carousel-inner>.active.left{left:-100%}.carousel-inner>.active.right{left:100%}.carousel-control{position:absolute;top:0;bottom:0;left:0;width:15%;font-size:20px;background-color:rgba(0,0,0,0)}.carousel-control.left{background-image:-webkit-linear-gradient(left,rgba(0,0,0,.5) 0,rgba(0,0,0,.0001) 100%);background-image:-o-linear-gradient(left,rgba(0,0,0,.5) 0,rgba(0,0,0,.0001) 100%);background-image:-webkit-gradient(linear,left top,right top,from(rgba(0,0,0,.5)),to(rgba(0,0,0,.0001)));background-image:linear-gradient(to right,rgba(0,0,0,.5) 0,rgba(0,0,0,.0001) 100%);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#80000000', endColorstr='#00000000', GradientType=1);background-repeat:repeat-x}.carousel-control.right{right:0;left:auto;background-image:-webkit-linear-gradient(left,rgba(0,0,0,.0001) 0,rgba(0,0,0,.5) 100%);background-image:-o-linear-gradient(left,rgba(0,0,0,.0001) 0,rgba(0,0,0,.5) 100%);background-image:-webkit-gradient(linear,left top,right top,from(rgba(0,0,0,.0001)),to(rgba(0,0,0,.5)));background-image:linear-gradient(to right,rgba(0,0,0,.0001) 0,rgba(0,0,0,.5) 100%);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#00000000', endColorstr='#80000000', GradientType=1);background-repeat:repeat-x}.carousel-control:focus,.carousel-control:hover{color:#fff;");
			htmData.append("text-decoration:none;filter:alpha(opacity=90);outline:0;opacity:.9}.carousel-control .glyphicon-chevron-left,.carousel-control .glyphicon-chevron-right,.carousel-control .icon-next,.carousel-control .icon-prev{position:absolute;top:50%;z-index:5;display:inline-block;margin-top:-10px}.carousel-control .glyphicon-chevron-left,.carousel-control .icon-prev{left:50%;margin-left:-10px}.carousel-control .glyphicon-chevron-right,.carousel-control .icon-next{right:50%;margin-right:-10px}.carousel-control .icon-next,.carousel-control .icon-prev{width:20px;height:20px;font-family:serif;line-height:1}.carousel-control .icon-prev:before{content:'\\2039'}.carousel-control .icon-next:before{content:'\\203a'}.carousel-indicators{position:absolute;bottom:10px;left:50%;z-index:15;width:60%;padding-left:0;margin-left:-30%;text-align:center;list-style:none}.carousel-indicators li{display:inline-block;width:10px;height:10px;margin:1px;text-indent:-999px;cursor:pointer;background-color:#000\\9;background-color:rgba(0,0,0,0);border:1px solid #fff;border-radius:10px}.carousel-indicators .active{width:12px;height:12px;margin:0;background-color:#fff}.carousel-caption{position:absolute;right:15%;bottom:20px;left:15%;z-index:10;padding-top:20px;padding-bottom:20px}.carousel-caption .btn,.text-hide{text-shadow:none}@media screen and (min-width:768px){.carousel-control .glyphicon-chevron-left,.carousel-control .glyphicon-chevron-right,.carousel-control .icon-next,.carousel-control .icon-prev{width:30px;height:30px;margin-top:-10px;font-size:30px}.carousel-control .glyphicon-chevron-left,.carousel-control .icon-prev{margin-left:-10px}.carousel-control .glyphicon-chevron-right,.carousel-control .icon-next{margin-right:-10px}.carousel-caption{right:20%;left:20%;padding-bottom:30px}.carousel-indicators{bottom:20px}}.btn-group-vertical>.btn-group:after,.btn-group-vertical>.btn-group:before,.btn-toolbar:after,.btn-toolbar:before,.clearfix:after,.clearfix:before,.container-fluid:after,.container-fluid:before,.container:after,.container:before,.dl-horizontal dd:after,.dl-horizontal dd:before,.form-horizontal .form-group:after,.form-horizontal .form-group:before,.modal-footer:after,.modal-footer:before,.modal-header:after,.modal-header:before,.nav:after,.nav:before,.navbar-collapse:after,.navbar-collapse:before,.navbar-header:after,.navbar-header:before,.navbar:after,.navbar:before,.pager:after,.pager:before,.panel-body:after,.panel-body:before,.row:after,.row:before{display:table;content:' '}.btn-group-vertical>.btn-group:after,.btn-toolbar:after,.clearfix:after,.container-fluid:after,.container:after,.dl-horizontal dd:after,.form-horizontal .form-group:after,.modal-footer:after,.modal-header:after,.nav:after,.navbar-collapse:after,.navbar-header:after,.navbar:after,.pager:after,.panel-body:after,.row:after{clear:both}.center-block{display:block;margin-right:auto;margin-left:auto}.pull-right{float:right!important}.pull-left{float:left!important}.hide{display:none!important}.show{display:block!");
			htmData.append("important}.hidden,.visible-lg,.visible-lg-block,.visible-lg-inline,.visible-lg-inline-block,.visible-md,.visible-md-block,.visible-md-inline,.visible-md-inline-block,.visible-sm,.visible-sm-block,.visible-sm-inline,.visible-sm-inline-block,.visible-xs,.visible-xs-block,.visible-xs-inline,.visible-xs-inline-block{display:none!important}.invisible{visibility:hidden}.text-hide{font:0/0 a;color:transparent;background-color:transparent;border:0}.affix{position:fixed}@-ms-viewport{width:device-width}@media (max-width:767px){.visible-xs{display:block!important}table.visible-xs{display:table!important}tr.visible-xs{display:table-row!important}td.visible-xs,th.visible-xs{display:table-cell!important}.visible-xs-block{display:block!important}.visible-xs-inline{display:inline!important}.visible-xs-inline-block{display:inline-block!important}}@media (min-width:768px) and (max-width:991px){.visible-sm{display:block!important}table.visible-sm{display:table!important}tr.visible-sm{display:table-row!important}td.visible-sm,th.visible-sm{display:table-cell!important}.visible-sm-block{display:block!important}.visible-sm-inline{display:inline!important}.visible-sm-inline-block{display:inline-block!important}}@media (min-width:992px) and (max-width:1199px){.visible-md{display:block!important}table.visible-md{display:table!important}tr.visible-md{display:table-row!important}td.visible-md,th.visible-md{display:table-cell!important}.visible-md-block{display:block!important}.visible-md-inline{display:inline!important}.visible-md-inline-block{display:inline-block!important}}@media (min-width:1200px){.visible-lg{display:block!important}table.visible-lg{display:table!important}tr.visible-lg{display:table-row!important}td.visible-lg,th.visible-lg{display:table-cell!important}.visible-lg-block{display:block!important}.visible-lg-inline{display:inline!important}.visible-lg-inline-block{display:inline-block!important}.hidden-lg{display:none!important}}@media (max-width:767px){.hidden-xs{display:none!important}}@media (min-width:768px) and (max-width:991px){.hidden-sm{display:none!important}}@media (min-width:992px) and (max-width:1199px){.hidden-md{display:none!important}}.visible-print{display:none!important}@media print{.visible-print{display:block!important}table.visible-print{display:table!important}tr.visible-print{display:table-row!important}td.visible-print,th.visible-print{display:table-cell!important}}.visible-print-block{display:none!important}@media print{.visible-print-block{display:block!important}}.visible-print-inline{display:none!important}@media print{.visible-print-inline{display:inline!important}}.visible-print-inline-block{display:none!important}@media print{.visible-print-inline-block{display:inline-block!important}.hidden-print{display:none!important}}@media screen and (max-width:568px){h3 .label{font-size:50%!important}}</style>");
			htmData.append("</head>");
			htmData.append("<body><nav class='navbar navbar-inverse navbar-fixed-top'><div class='container'><div class='navbar-header' align='center'>");
			
			if(Main.configurationObject.getTemplate_html().toLowerCase().trim().equals(ParamsObject.PARAM_TEMPLATE_APPFUXION)){
				htmData.append("<img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMIAAABPCAYAAAC5zxo3AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAABmJLR0QA/wD/AP+gvaeTAAAQcklEQVR42u2deZQc1XWHv6sN1EIgYtYYSWwx4GBHhsaGsMlAQIAVAWZRIGDsAEfEJqGDjGUfMGCQ");
				htmData.append("LZBNKyxChjjIbMZBFmuwgjEGwnHi0BhsCJshiC0ISaCRRFoSYvjlj/ta09N0z1TV9EzPjN53zpzpevXqVr3q/tXb7rtlkohENnaGtPoCIpH+QBRCJEIUQiQCwDAAM2v1dQxKysX8ZKAI7AI8B/x9rlD6Rauva7CTpd9rkqIQeoFyMX8KcBOda931wPG5QumeVl/fYCaLEGLTqBeoEcGHwANh13BgQbmY/8tWX2OkM1EITaaOCE4DJgHzQpYohn5IbBp1gaSPA2c");
				htmData.append("Dm4SkG8zsxUb5y8X82cA1uAjWA6fkCqU7wj4D5gLTQvY1wBdyhdJDrS7nYCP2EXoBSfOBk4BNgdeBI8zsudp8QQRzw2bdvkAQw/XAGSEpiqEXiH2EXsDMTgd+CJSBscAiSXtU50kiAoBcoSS8RrgtJI0E7isX84e0upwbO1EICTCzc4Eb8Cf4OOD+ihiSiqBCrlBqx/sNUQz9iCiEhAQx/Ah4D9gRWNT+9rMHAN8KWdaQcGg0iqH/EYWQAjM7B2/jrwbGDdlmj5+");
				htmData.append("MOPyiacDTeFs/8fxAF2LYv9Xl3BiJneUMaP26mQwbcQ4wGniLD9Yda8M3/U0WW+Vifig+3HpySFoFHJ4rlDLZi8TOcp9QLuZnrLlm//20duU1eDNpe4ZtskDSPlnsVdUMPwtJmwMPlIv5z7W6rBsTUQgpKBfzFwDfAz6/dt5heWAOLoYdgIWSDs5iN4jhZODekBTF0MdEISSkXMzPAi4Nm6uAi83sQuAKoA0Xw62SDs1iP1covQ8cTxRDS4h9hAQEEXwjbH6kDS/pG");
				htmData.append("8AMYAywFDjZzH6Z8VwjgAXA5Ebni3RNnFnuBboTQQVJBeDbuBjeAk5tohhWAAfnCqWnW30/BgKxs9xkkooAwMyKeNNpJbA9cLOkI7Oct04zaUvgoXIx/6lW35PBSqwRGlAu5qcDs8PmcuDIXKFU6u44SecCFwAfA5YAZ5nZvd0d1+AaRgA/ByoTbcuBCblC6c1W35/+TKwRmsuM8L8NOCSJCADMbA5wEfAOsB3wz5KOy3IBoWaYDPwqJG0F/F2rb8xgJAqhMZtnPdDM");
				htmData.append("rgW+jothK2BeD8RQBi6vStq+1TdmMBKF0Jhbw/8xZGifm9mNuBhWAFsD10n6YtqLCP5Hd1YlPdrqGzMYiUJozFfp3CTJKobzgGXANsBcSacmPb5czB8N3If7IYHPPt/Y6hszGIlCaEBoknyB5olhKS6GKyV9ubvjysX8MXhNUBHBbcBJYRY60mSiELqgiWK4GV/yuTTY+H5XYgjrnn+Gr28G93g9LYqg94hC6IYGYvhFuZjfNY0dM1uIi2E58EfA7HpiKBfzZ9E5DMzV");
				htmData.append("wLQogt4lziMkpFzM5/D2+udD0pvAxFyh9FIaO2H0aB7egX4HuMTMrg7n+Br+w69wea5QmpHGfiS6WPQ6QQwPAvuFpKximIw3d7bDxTBzzZx9XgfuqMp2Qa5QmtnqMg9E4oRaLxOaSUcC/xWSPg48nKGZdC++BuEtfAb6opF/+8hOdLhUTI8i6FtijZCBcjG/BR697rMhKWvNcChwC14ztPF+eeaauQc/myuU7m91GQcysWnUhzQQwwG5QmlxwuMNuHD4wee9OewzU2cC2+");
				htmData.append("LuHFeY2fdaXb6BTBRCH1NHDC/jNcMb3Rw3BLgWj3G0fPgB55w/LH/apXhTqw2YY2aXtLp8A5XYR+hjcoXSSuBwOvoMu+B9hh0aHRMW699MR+jHLdY/dvUKPJreG7hLx3RJlxLpM2KN0ATKxfxW+DzDniGpbs0Q3Kp/AlQc8Nbis8X3AIQAAAvxZZ/vAVea2UWtLt9AIzaNWki5mN8aeIgGYghDrz/FJ+egQdzTIIa7cS/T1cBVZnZBq8s3kIhCaDGNxAC8S+fJuFX4Qp9f1");
				htmData.append("7MjKY+7WIzDxXCDmZ3X6vINFKIQ+gF1xPAi/sPPh+3leE3Q5WJ8SXvhzaTxwP8BN4ZIe5FuiELoJ9QRQ4Xl+Gq3RIvwQ6Dh+/FYq2uA60MM1kgX9LoQJI0DHqbjbTATzey1Vhe8L5C0E/AD4M+AoV1k/U8zmxrEsBA4IKQ/AxyXK5T+kPK8e+BDtDvgoelv6G0xlIv56/HRMIBP5wqlVb15vmaTRQjDUub/FrBTzfa0lDYGHJJG4D5GOyfIvhggVygtAw4sF/O7ACOAF3KF0");
				htmData.append("odpz21mz0k6HF/EPx44U9JmZnZGWlsp2CacCzaSIfbEQpC0K/CVmuSvSPruRlAr7EWHCFbjnd9GLKneyBVKL/f05EEMR+DNpJ2Bv5I03My+1EvlXQq8Gj6nFu+AJGk1Imm+Oji36vO8RAYGMJImVpV3TguvY7ykP4TrWCfpplbfm/6IpNR/iYQgaVdJH4QvYFFIWxS2Pwi1xaClvwghXMt4Sf8TrmWtpLiGuYYsQkjUWQ4v1KtUw3kze0LSvsB/hLQfh3eNdWdnGu5pmZX1+B");
				htmData.append("vs7zKzDVW2pBn4y/6y8j7wJLDIzDY8GSRdHD7uWFX+3wCLurC12Mzmh+MnAfuG9PlmtjjNRUkaA5wbNp83s9tD+nh8VGpnYB3eKT+l+toT2L4QOBQwfBb7BDMrh31Tgd1D1llmtrbquMPwoeAs93s1cH/1yxgl7Q5MzWCrmjbgXjN7OdhMb6G7gyTtVlUb3FWz7+dpagVJT6k53CNpSJXdtibZnV9zvVl4uOr4OVXpEzN8NztWHV9778dKejHsWytpgaREw3+SLpG0Khz7umre");
				htmData.append("7SDprqrzjqlKv6oJ9/gDScdX2TymSd/dGrlIM9UISUYELqFjuLDWI7Ly/rCheJjDvmIyHcN7zeRL8omsfo+ZvQ4cBDyPvwd6CnBnd2KQNAsoUHnbDxxnZo93dz5JY4FmTOgNBa7shVuyKZ0DoaWiy1EjSZ8ETgybd5vZk9X7zexJSXfjX8JfS7rCzJ5NcN4ycEqG6z2Eji+jXhNrGXBWBrtTce9PcLeG34bPx4b/e9LxboT78JcKNmJ5hvNnwsyWyF9O8iiwG3A0cIekE+o1kyRdib");
				htmData.append("/jeTTwGvBFM0sUypLOQ8fd3YNGFPFm5tgG+38K3J7B7vX4GvBdst7L7oZPv423IQGWyQPc1rIs/B8a8idp7603s7vSXmx1Nd2Acka7E+qlV2xJaqtKfjnLOXoLM1sq6SDgMeBP8IfSQknHm9mGyBeSrgb+Bo+T9CpeE/w2zal6eg/U0edqxPMZ7c7p6X1sKARJf0pHbQAdb4vvihMlfSdhrRBpEkEM++Oz/p/EPVzvlXS0mUnStcCXcREsBo6q7rBGup41nEnnp0ASDK8VIn2MmS3D");
				htmData.append("PV1/jz/g/gIXw3z8ITYSX/gTRVCHujWCpM/gVSz41P71CWydhUd4OFE+2/z7VhduY8PMlkm6Dvg+MAr3cyrjLh4C/jWKoD6NmkaVVVEfAtOTNHUkvQhMwmuF7wDHtLpwGxuSTsNH9kbhbhJfx+ddKqveTpb0dlz19lE+0jSqqQ1uTtreD/n+JWxOCXYifYQ8fOQc3GFuKVAws5vC0Ohx+FDpaKAg6YpWX29/o14foRJYqh24LKW9i8Nx0FGrRHqZIILZ+LvWlgJnm9ltlf1BDFPwPs");
				htmData.append("JoYFoYSo0EOjWN5G4TlRfg3WJmqQJWmdnzkm7B3RGmSNrbzJ6ok3VU7UxpQsZ1s3+bjHZ3r/rcTG/L9VWfL5OUdo4h18DWBiSdib8E/WP4HMbZIeBwJ8zscUlT8FDz43AxDE+x6m1d1efJknbMcD8qLvwZfCB6GVW5WKgJjnSq46BXta9ZLhbtknarstssF4u1kravU6ZMTneSjm3SdUnSOXXsnyNpedi/RAleTyVpD0mvhGPK9cqjOi4WkjZr4n1+pOpc1S4WF2f8zS0Ox7dVftNp");
				htmData.append("/6r9dfYFjgibN6atDSqE424Jm0cEu82kDTjDzF5ost1l+IvC32qWQTO7E5iFO/VlpR34ITC3OlHSP9BREywBzqxXE9S5pueAo4DX8SHVaZLmJjjuPXyy9H97eFtKwOk9tNF0NnifBuWPCelLK56IWZA0Cp/yBmgzs7aQ/hS+1HE18OkMptcDb5vZBzXnawO2IIRdzGB3XShz3XcQSNqUDpeOVWb2bmLLfvwmeCd2aJrj8CbEUjNbU2NvKD4qV/EA/Z2ZPZPymnYC/jxstuNeoavCvl");
				htmData.append("F0vKRkZY1H7pBQlizepx+5d5JywR5U/VZSlmUHvJn/oZm9pt7wPm0m6mgapS5sN3YrVfbiPitMpN/So6ZRJLIxE4UQiRCFEIkA6cO59JR5eKdzbU8N1TAL77y19XF5IoOEGOkuMujIMgAUm0aRCKFGAHYFpuPL6DYFNsfHsFfh/ik3mNmjSY3KZ33Pw6fUh+P+LUPxaAmr8EgQs5POVchnuf8JWAGcFXzva/NMAialCYco6UB8yehY3GNzFN68WpKhzJcBT5nZggb7Twe2M7NZCe1N");
				htmData.append("AGaYWaYID/IF8l/rJtv8SsSNBPZuBy5IOtEqjywyCbjHzK5skGcOsMDMHktocwjwVXyt9tb499WOx4VdiU/2XU5HcLLEVPoIP8Kdta7C/dfb8B/wGHyC6n5gsxR2FwKPA1eEi3wvpI/A+wiX4JNj301obzPcH+gh4CFJh9QRw3bAhKQXGCaUHgTOB36MR5xeg0/M7QM8IGmbyiRTAvak6/XKO4a/pIyhIxRMFh4Bqn+00/AH0uyqtCUp7O1Lut/A7viD65uSRjR4AEzAV9Ul5Vg8YM");
				htmData.append("R0fPK0jM/aj8ZFcRK+7jn1fasIYW/gwNrF+QCSfomHGNwsTLN3iaSR+HLBo8zs1QZ59gTSummvBU7F30rfSAxp+ATwipn9Y519JXnA45F4DTbgCPdmw/2RtARYa2ZP9eFl/A74JvCg3MGvp6/D2huvYW6tt1PSCyR/uHaiIoRRNIjnGQJpfSKFzTHh/4ou8qzGVZwKM2uXLz5phhj+GxgvaTYeEqUd9zxdFf5fa2ZvZ7QdCQSP5InAw5JGmdmMHpjbki4eTGb2Cv7QTm24evi0WUNH");
				htmData.append("FX+artyZ15MxMl2zxGBmb4Q+whTgc3izrcK2wEGS9krh3NeeMN9Gh5m9VCUGeiCGYdDJUfRTwL/XyTcmi2HwL3GLRpkk3Qycn9Azc3X4P6KLPFvS0W9ITT0xpLUhaRdgNzO7sMH+R/E4SkmFsLpJeQYlTRLDSqp+5Gb2dPW2PAhZpsjsFXW9BBwmqVNnSNJIeeS3qXRemNFVgVfizaIT6tgbIvf3n0jyH1ij87QDp+HxfH6Nr8lNw1jgB5I+8gCQ9Mf4+wHSLKR5Fziw1p4kC2XeL6");
				htmData.append("W9QUcYcZqIB4NLNHpWw4vAZ+WBkGsXlW2OR0BMPWJUMYCkoyU9ExZqSL5ApfJ/qVIGUJJ0oqQXquytk/R++Lxa0q/kgWyT2pvQyLM0/NCuC7YfTmFzuKTbJK0I3qvvyBe5vC3pNUnzJA1PYW8HSY+qI6boe1ULTt6R9G+Stk1hb6KkD8O11fvbIqmtYO/itN9jzfGL1SAQWoP88xsttJEv3noj3JtjUtgcLekO+SKk9TWLfVZLekK+GCpbNOxIZGMnzixHIkQhRCIA/D/6cE11I77f");
				htmData.append("ZgAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNy0xMS0wNlQwODoyMDozMCswMTowMMrouV8AAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTctMTEtMDZUMDg6MjA6MzArMDE6MDC7tQHjAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAABJRU5ErkJggg==' height='80'/>");
				
			}else if(Main.configurationObject.getTemplate_html().toLowerCase().trim().equals(ParamsObject.PARAM_TEMPLATE_NH2)){
				htmData.append("<img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUgAAAA4CAYAAACfQWFGAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAA" );
				htmData.append("AOpgAABdwnLpRPAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAAAZiS0dEAP8A/wD/oL2nkwAAAAl2cEFnAAADwgAAAPoAlkkRHAAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNS0xMC0xNVQxNTo1OTowMiswODowMPYhaTcA" );
				htmData.append("AAAldEVYdGRhdGU6bW9kaWZ5ADIwMTUtMTAtMTVUMTU6NTk6MDIrMDg6MDCHfNGLAAAeTUlEQVR4Xu2dB3wVRdfGn3RCIEBCAOlNIiC9CqIivYiKrwUEVFCkqdh9+bCB2F4pUZFuQRFURBHEUBMQUEERpIVQkxAgQBJC6Cl85zn3bri53IRUEJj/jyF3Z2d3Z2d3nj1nZnbH7bwA" );
				htmData.append("g8FgMFyEu/2vwWAwGJwwAmkwGAxZYATSYDAYssAIpMFgMGSBEUiDwWDIgkv2YnO1m5ub/k5KSsK+fVGIj4/XZX//4ihfvjzKli0LDw8PTUus9NcCl+rkz/G5ym7Op6fDjY8k+S/t5Gkkr9+AxLDVOPnPVpzdfwCpSck4f+6cPLbc4e7jDc9SpVCkakUUb9wA" );
				htmData.append("pdreCr+G9eDmITuQ/TBb+ttgMBQaORrmE3coDj8tXIg1a9bixIkTmUTB08sTVatUQdcundG6dWup2+6ZRDW/JCQkYs3atYiLi0ORIkWQ7iDCaSmpCAgMwO2334YS/v4FdlxrP3v27sWkSZNx9sxZeHl5icZdKKpUOXbZcuUweNBA" );
				htmData.append("BAQEZH9siuN5iqM7UuR8Ds+eh8Pf/IAz0bE5yy/3LeVctE4wyj3WC6V7dJZ9yQMpTfZpRNJgKDQuKZA7dkRi+oxPsWvXLptISHKKIEkXS8aCFmSH9u3Rr1+fjHT5EStr+71792Hy5CnYaT++dUyuS01NxY033oinhg1FxYoVdJ2Vt/xgHXv79giMfmsMzolVZwm/RVpaGqpWrYqXX34RZcuUyfrYsg23Op+SgoTQ5Yh+azzOHjoMN08PXe3m7sYkms72wwGWnwT+OZ9uW0+RLdakA" );
				htmData.append("aq/PRK+NauJSKaJSNr2ZTAYCpZs1eTI0aNYFPoLIiMj4ePjY4+1CaOjOFIYuLwiLAw//PCjxlFgHAUlr6hGiIgQirBjUEHS9bq6wOGx3e3H5rF4Tgz8bYmhxOhfl/D0JX3a8WTEjP0EkU++gHNH4uHu7aX7oeDRCpTCu1gcCeNknabhbz1fNxz//S9s6zMISWv+UHHU9QaDocDJVlqi9kVhy+at8PT0VA" );
				htmData.append("HMSvAssTxz5gxWixv+118bdLkgBJK7sPZDq80x8LhuXJX/w7iEh02n5SZY58/A39Y5O7rdmWC0aCDF8eDUmYgNmQIPP1+bxSh5Z3vkRVA0reAK5iE9TQTWE+fECt39wutI3rhZ3WwjkgZDwZOtQB4/flw7ZHJiDXI9raro6GgsWbpUtk3W5Uttd+1C6zA" );
				htmData.append("Nx1b9hv0hU+Hu6ytlIbGuhFHKia6zLYE9SJmr6+wsllzNtkd3DxHJOOx74z2kJZ+wtUW62rfBYMgz2Qok2/jY/qbuYA5gOlqb27dtR5i429c1UhbsmT4wbab8ln8UuyzEkYJI4XT38oRnCX+4F/W1xYnAZqS5CLaTuuPMzr04MOVze5zBYChIshVIMVaknvL/nGFZkSdPncLq1Wu0k4Oiabmj1wUsL4pbSoq2FR5fux5u7LSyxM4RKRuWT7GGN+PGj99Dg7D5aBj+k4a6cz9D2X4PaHsluK2zSPIYElLFeoxfuA" );
				htmData.append("QpcUdsaYwVaTAUGNkKZF6wRHL3nj1YvGSpWKApunxdudoieinxiUhcvgpu7JBxFjfFTcTPG2UeuAc3z/sCgd06wKd8OXiW9Id3UCCKN66Paq+/hODpIRJXEufFmr9IJCmwYrGnHjuOBDkWua7K2WAoZPIlkLR+KH7OWPGbN29GWHi4Pfb6IjXxGJL//FsF7GLREp/7fDr86tdBldde0BhanJLwQhBLkNv5t2iMyiOfRfqp0zbrUMr2A" );
				htmData.append("nSzxcCUdSc2bNIY12JsMBjyQr5qEyuw9iRnqrS2eApkQkICVq1chaioaE1zvVg3bE88vXsvzu4/eKHzhWXEwGX54xlQEqV7dIJ7ER91v+mG29bZ03l46LaML9GyKYo3qm9LZx92pFh6KuJ6JibWFsdtr5NyNhgKmzwLJIUxOLiWvsXi5WWzkhyFksvssIncuVN7tck1L5J6am5q7Z3eE2XTOvYuq4rZA" );
				htmData.append("y1DETp2xvi3bKabcZuskYdNUV8UvTlY3Gy2Y16cloLM4UTpZ8/ZI67hMjYYLiN5FkhW1Lp16uCRfn3RsEFDpKSkZhJIYgnihg1/a6fNtY8IkxQBhercwTgxEz3FQvRVK9Ex0Cr0KhMEn8oVdatMVqEzskt32Y93YGCWwsetOfTHZUeQwWDIM3kWSL5hwg6Y4sWLo0OH9ggKKq3DgpytSC4fPHgQy5Yvx5EjRzNE85rEfu5eJUug2ktPo+WfK9Bk+Tw0Df9JQxMJjcLmo/HqhQie9IGth5o4lJkrWF7pbKPMCtne3ccHHiK+1rLBYMg/+bA" );
				htmData.append("gpR7at65d+ybc2bYtzkkldiV+6mpH7sTSpct0+ZoWSSJutVupEnC/oQzcywZlBA8JXuXKwLtcWW2DzBFSVrRIz+yNtrnrrpB4r8BSfGrZIwwGQ0GQP4G0t4fxPe3WrVuhSeNGakU692xz+ezZs/hj3Tp1t8k1LZCE58fXFC8K6baQi/PXHvGNm7XjJlO5SfHTWORwIZ+qlTVKXzk0FqTBUCA" );
				htmData.append("UmMlRpUplcbU7wM/Pz6WrzY9LREVFaYfNiZMnVDQLWiS5P+s96fwEK1/nbb0ueUOVy1WQIme4lIhJHnh0dvgcW/GrvnvNIUOSQdt6hZa47M7XF8Ub1bPHFWyZGgzXMwUikJag1K9XD23vuENfT3QFXe1t27Zj2bIV9piChaJM4c1vsMTdvbA" );
				htmData.append("+E5RDmI8zsQdw8PPZ2rlzERRReRh5+BdDiVtsPeKODyaDwZA/CkQBWClpeRUvXgy33dZG2ySdXW2KKJdPnjxpew0xYkfGdvmB0mzXZ/24Br8buXv3bn2TR//mMuzcuUvCTuyS7aNjojPE/7JiP2bK0QTsnzAV5/Yf0Pe0M1mPooPs/fYo6ouSrVrA" );
				htmData.append("q0xp23YOZW4wGPJHth/M5auCEyaEoFixYvaYzPS4qzv69u2jv7kby3pZtmw5pk6bruJHUXQ+BOPbtGmDoUMGuxxDSaw4/WDulCkqXK4/mFsTTz01DBUrVNBOoJAPP4avbxFdn82p5ZscfzA3tzDPEtJOnMT+j6Yh9uPp8PA" );
				htmData.append("ramtbdILn53NDWQRPHQe/enU0jfnCuMFQcBRobbIEqVHjRiKAt7p0tSlcDHwNcUVYwbvasusMrGPlJ1xWLHE8dRoHP/0asSKQ7kVFHNm54witR8mbhzwIAjq3U3GkdWnE0WAoWAq0RlmCEhgQgHZ3tkXlypVddtjQ0uJcM+HhqxA" );
				htmData.append("VXbCvITruhvvMb7hs8FgS0k6fwaHPZiPq3RC4+/llxGdGykssWJ8qlVBh2AB7nMFgKGgKzeQIDg5Gxw7tVWQYnEXS09MDu3btRmjoYo0rSJG86uB5S1DLcepMRI0Zq261FZ8JlqPEsc2x0nOD4RUYYHuDxrQ9GgwFTqHUKgodh/U0b94Mt7RsiZSUlEwCSWyCmI6//96IVat+tcfmEzkE3/Dhvmml5jc457lQ0DZVtjmeQuzEGYh+78OsLUfJD/PkIW53mYd6IqBjW7trbSbtMhgKg0IRSEtYypUrp68hlg4MdOlqc/nQoUP6GuLhw0d0OT9WpOgtOC0s98FOk/yGQrdo5RgcG8n5sKPHTsT+CZPhUSxry5Hlw7GQgd07otLzQzLi843zYHaDwaA" );
				htmData.append("USi82sZY5kRdnOpw1ew6K6rwsmQ/HZfZOd+3SRfb1cEYc4faX7sW+MO0rRZZtmh5i/eWVdFHZ9DRbj3RUdBTmzv1eP8TBZce857sXW8XRHSnxCYh+ewLiZn+vlqHOWeNURhRBLVuxFAO7tEPNkLdt8fZ9GAyGwqHQahcrNA" );
				htmData.append("WFk/23sr+GSFfbWUS4zPh169fjzz//0jhnEc0OSjK1g5QuHYhmTZugsRwrr6FpkyZo1qwpmsp+6tatK/krBPeVVpqc97nDR7F35DuImzMve3FkmYk4lu7eATUmjLHFF5Q4nk1BWtR+pEXH6t/0Q4ftKwwGQ6GaH2r1CFUqV0anjh1RVESA" );
				htmData.append("lpcVTyiGFMmYmBiELl6C5OS8z4bIbRxd5LwG69jpkte85CNbVBzd9NXBPf8dhfifQi9pObp5eiDovu6oMe4tW9kxbX7F0X6sExGRWH/7XVh/Rw+sb3s3tg15UeMNBkMhCySxBKZevZtx551t9aMVrqD7vGPHDiwRtz6vUDworvkNloBz1sA" );
				htmData.append("CxRLHg3HY88ooJC4Og7tvEdt8M1mJo48PyvV7ENXfedUWXxDiSKzj8a+2Pcp+iWmDNBgyKHSBZCWnSPK7kbff1gY33RTs8jVEpjt16hTWrv1N39d2XH9N4GA57h35NhKXroR7ERFHDtFxxi7Q7n5FUXFYf1Qe8axNwA" );
				htmData.append("pKHIn9GOR8pg9cyG+jkQaDcllUyLLIatasiU6dOsHdwyNLV3vvvij8Ehrq8BbONVBbaaVRHA8fxb7RHyA+dLnNcnQljhRASe5ZojgqvTAU5YcM0FcIM9YVMJx10afCDfAuWwbepQPhKyHDmjQYrnMum5lmudqNGjXEHbe3yfI1RI5j3Lp1m77PfU3A" );
				htmData.append("85bzSk1IROyEyTg672d4sDc/K3EUcfIKKIkqI55T15rpdEqGvIoji10s9nMrViOxzd04dldfHOraG5vb9cTO4SPhVycYjdcsQuPfQ9F43RLUmD5BP8BrMBguo0Ba1qLtNcR22b6GeOzYMYSvXIno6Gh4enpliOtVhz3f/PDEoS+/xcEZs+DhJ+LoykJjOVA" );
				htmData.append("cAwNQ5dUXEHR/D7s4yiVyKKNcI3lg+SUdS8K2yEhEbNuOfRE7cHJvFM5Gx2gSDjXa9dxI7Bz2MqKfGYHkUeM03mC43rkipkKtWnS1O4ge2HqMnUWSb+Hs2bNXrUgPsWaY7mqFIpe4fBViPvjE5lazLdIunBnw9M9THEuh8n+Ho/Q9XQpGHB2wdsOytspb9y+kHk9G/IIliF+4BLE/LsKuJdlb73ywLV68FPf2vB8xMdH22A" );
				htmData.append("tw7Ou0adMxaNBQnD592h575Thx4oSO5+Xn8AqDdevWo3//J3D48MVDpL6f94OGwmTBgoXo0LEL2t7ZQcOd7Tri0f6Pa76u5rpjseiXUPz662qctWbtvIxcdoG0CaAnmjVthtatbtF5bBwF0oJxK8LC8eaotxAbe0A" );
				htmData.append("ty6vqYlME5RxORe5C9DvitoqbzOE6LsVR8PT3R/lBj+pwHs5zLaltaXnOOQ05gWWdUd62vzwW2yLpxrv7eMPdyz6ZWBbwGvIzdZUrV8Katb/bYy+QkHAMkXLeHLzPtFcavugwfPgz8JcyLiwCAkrhu7nf68PjcpMqD9PRo97A" );
				htmData.append("8mWLEbZiKZYtDcWzzzyNGZ9+hj179/4rrkFuiI2NxbTpM3A8OVmXu3bprF8H85F783Jz2QXSEsNy5crq2MigwItnQ7RgO2ViYmKW6/+18H6U/LJT5sDkmTgTHauic9E3HeWUKE7u3j4IFKvxhsf72qI5tQLbAWnh5SY4Qks1LVUMU1tbp9XmSffecvHPy3rb3zScP5eiIssJwrKdQdGOt7c3bq5bF4kJiZmsRF6r6Ogo1KhZHYGBgVdd5cwLvDdvuaWljvP944919tgrB42JBg3qo3mzZjhy+Mh1cQ0KC6dadXmwLljwTcHo0KFdlqazle7qEkfm+byKTvL6DTgyb4FaZS7bHSWpdY4nN29HxIBnsP3hJ7Hv0WE49thwHO+fw9DvKZwY8Q7On7GPMeU+2bEjljo/ZEHBLXF7K9T97lPU/vwj1Jr4PmpNHY9qo/+ryb0rVUDtryah9syJCJ48FtXeGqHx2cFrUrJUSRXBzZu32GOh41w3btqEJo0b60gFiyNHjmDU6DFo36Ez2rXvhOHPPq9fcyKcFnjs2A" );
				htmData.append("lYtOgX9LzvfnTs1BWjRo3RlwYIRffX1avxwAO9cUfb9uh+1z348quvJN4m5DzOihVh+M/9D6p7OXTY01j/558ICflI3WpHF5tpJ0+ZivDwlWJVPq95eeSR/ti06R/dF4mPT8Dbb7+r67p0vUstsVXi4s374Ud7ChdIefS89x6EyX5dudqE15rD2Hr17qOu8BMDB+Gffzbruo0bN+Gbb7/LuB8iIyMxaPBQLTfCfH888RMcOHA" );
				htmData.append("gI01O4bXifkaMeFVd8W7de2DON99qUwhhWz8tttDQJej3yGPYsnUr9u/fL9foOS3PTp27YcqUaToMjyxYuFDL+6OPP8FdPe7Va/r++x/g6NGjup4kJCTgjTdG67Xs3KW7XK9ZOpuABQ2ft8e8o+tYziNffV09RX6bYeTI1/HVV1/jsUcH6DxWP8t9sXrNWp02evz4EMlbrH0vtnLhdY6I2KHL7OA" );
				htmData.append("dPGSY3ics5/CVq/RNPcL8jXz1Nc0vw7hxIZrP7LgiAskLxovM2RBbiZvN1wPparsa+8h0ub0hrjhyfhzveHj2D3CTrOvXdrI5h3QRlRN/b9bJuZLWrsehVb8hInwVtoXlIIT/iq0Sdq/7E+mWRZiahpR/tuPUj78gcfEKxP+yDCc2btHecM+SJRDQpR38mzbA" );
				htmData.append("ubijSFi0FEkr14rVmAqPYn4I6NwWJe9orfu5FD5iRTZu3DBDXHid4sRioeVftUoVXea1PiPnN/+nBWjZsoW6f4tDf0afh3tj7vfz9AZPE8t685Yt2jn37TezMf/H71GzZo0MQWIFDlsRjkmTPkJ42DJ8PWumVJZ4Eeatuv6vvzaoezth/FisWL4Eo958HcuWh2HX7t16fGcomKx0I0a8ovl58cXnMHvONxJ/UkVjoghR1WpVsejnn7BwwY86hcjMmV+63JcjJUqUwF3du2XpavNV2kWLQjF+3Fh1hUePehM//Dhfz4+T3kVHRWveCKdJLlWyFGJi9utyXNxhrR+lS5e+ZD7YFEXBjdixA" );
				htmData.append("9XkPPhgGDtuAnr06I5fFi2Q8p2Hs/IwXb58hZY998uvasUnxGPypImofdNN+GrWbNzXsyeWLF6k5cCxshR3XlMvTy8V1Ab162He99/i54XzERQUJGU0S8uQQsjj8cWQhQvm6/b0lHg83hsU2v99MA7NWjTTa82mgdtvu02vA" );
				htmData.append("a3wMWNGoU+f3vjs8xlSLlX0fGlg8FXiQAmH4uIyypeiWqpUKW3OoUh+9933+m0G3idTJk/SdliKJsvk69lz0ObWW/Wa8roHlg7Ab7/9niH8rrgiAkmsi1ypUiV5QnVEMRezIV61yE10cmsEjoWvhRunlHBlPTojNym/CJ7rwIeKWou8lLayS5MbNPrLOdg49EVEDnsFO4e+jMgnX8D2vkOw762xmib5r03Y1usJRHLdkBcR0Xcwdr80SoUyJ7CicDxrmTJlkCyVmlYTxW6PiFKd2rXhTatZ0jA" );
				htmData.append("UkQfh4wP66/dBCTvhdDupuBQkVtAa1avhnnvu1ond+P5+ixbN9YnPfVavXh2vvTZSrVXCSlT+hhsQHROj6+nWPvH4AKkkFXV9QEAA+j7cS4/rCi8vb/Tt87DkIUjvN47PDQoqLdZEvFpoHpKH/9zXUx/gzCs/2XdbmzZ6LpeivoiGK1eb+WRlfPTRvtq8RPiX7WsUzpIlS2pFpwXFesCK30XWcX4kHnerWHUULpaPM56Sx1dfe0MtMVqmtLD58Bk2dLB+UWuLWPhNmjTW7wtwe4Z7771bHyCnTp2U/adrmXXuZHsdmOdcqVJFbbPl22RcHvTkQLRv307LK0Xy99CDD+oDj2/A" );
				htmData.append("sZx69XpI90OBp1WsLn7zpvD29tLry+Pxep0UMaLHEVyrFlq3aqXbEnqSLzz/rB4zq3JmGbZo3lzEf2OGNbpm7VpUk3uH+aaA9+jRDbVq3ajr/P2La/lyrineZ7w/WMaWIfZIv77o1q2rbpsVV0wgiVUQbMvqIIXvamzkpdCJq+QC/ivg+fA" );
				htmData.append("GSkjEsbDV2san7Yk5qFhs/2MbZa4DxVfbG+VvxrNFntfu4lrzl5SNTUhlSfLGeE1hLzNdb79h2Ilk/c4p7ACpX+9mvenZFrk9IkIronOH2rFjSWpZPf3Ms9q7/YVYZOdSbNebY19pfVmVhfj5FVVLhbBi0Ap4/39jMeDxgXjllf+TSviP3uissBTjwMA" );
				htmData.append("ATUsYx4pWTkTUVWUrUsRHJ5izoGDyPChMzDfFkm2sjnDuo5zAY7tytenmHRTxe2Lg4IzeZoYXX3pFv1hF+JYZyzE+Pl52xNdz64pVl4Dk5BPYIS53cHAt3b8zzp00tNjeGv2mWl8kMSkJ06bNyNTT3ePunvht7e8qNOly/1QoX17K/EKZPNy7l16P0NBQ/ChW7rZt2zKsNpYpy8gSGkIh5HU6fjwJSRK++GKmus/W8bp26yFW3SqcFA" );
				htmData.append("E9cvQIatSoruKaG3hteB24D1rUvKcOHTyEalWraF6OyXn+d8Sr6j5bx2UzxYYNG7UZj9eFD1k2D/ALY5s2bcpoZsiKKyqQvNgsbN6cnMSrTp06ciNlfg0xK7gdC4w3crHixTXO1c1zWbEfnxP9J61Zp9aj5FTjLi9iuUnZ8MjaA" );
				htmData.append("aNCqktSbs7WrMTbs+hKTC4FrxUtvA0b/tb2Ir+ifmoNOe6L7V8hH36EunVq48OQ8Zg8eSIee+yRTIKYFayUnJqD3wx9atgQzJg+Fe++OwYNGzXMJMIHDhy0/7KdR6JUHlqDub0nvL19EHcoLlPHE/e3Y0ekfenSOLraaXZR4UO8bNkymPnFZypiVqA" );
				htmData.append("r+LxYTswnrapYOQ8ei00ULEdfX1/9RgGtvpy4166gJf3s8KfVrXQ87pw5s9TC5C7d5SHKB5UjFGxaWLTsU1PTVFQogswD2wMdy59tz7xWfDD5SBkOGTxIm1Ks460MX465c+eoFRdUOkisuj0ZbYM5hbcU77cGDRpgz549YqX/oUJL65cUK+aHsR+8n/GgYFj9a7i67KVKldQ0TN+5cyexaO+R+68IFi78GUkirFlxRQWSWBe8upjJXbp0kqeKhxZ8djcC1zGwgtFFp3v+r8A" );
				htmData.append("uCmei9uPM7n1qnakw8VwKKwj6x/7bxoV1F9JlFj+2KelfiqclmppX28/cwIrPz8J9/fUc/VQcuaCP5zMqQlBQGf1Ly+q7b+fqcKDsrjOhONFSqFihorre9DLWi0vKDgWKDitM82bN9djsWGB6uqcff/yJDo7PDcwL3V4veeiybZCVnnnneFy6rJfKqyM331xXLeBly1donmgt0UX+9rvvtIOCsGLywUHXm2nYvsbhUxzexvrA" );
				htmData.append("41USQeGxbwoOzrXFZcG80KJlxw8FjvWLVtQnn0wWC4ode5nPi+f9mrjsdP0tq5HjkmP2x8q2tnP5atbX+O33P7R8mH7Gp5/Dv4S/ijo/TMNOlQjxJrg9z23lylWYOHGSWsM0hNgRxPZBy2vkVNDvvve+lg3Pm99kvWiyOjv8LCEFcv36P0XEa+t9QerKfkMXL1ZXnjBfbCvlbKe0lN+T/bNzjvGED/So6Bg1yrLiigskYQESfouxW9euGU9v3vwsLMcb01pmwdMtYDsGuZSoXi44r8zpXXttQ2VoCfPcCjMI+sf+28aFdRfS2cvGHu0pD5WA" );
				htmData.append("4Jq4USrPTXUl1K6N6jfWkFS5L0M+qJo1a6KV7WJX1E3bG1u2aIGBTw7SXtF33n1f2/QqlL8BISEf2q0Se3InaDndemsrbNmyRbfted8D2BGxAwP6P4rPP/9C4xs2rI8HH3pALLGXNM0LL76MRg0bqEtq3Vs5hR7JkMFPaucI3VC6hhSTRx/pl6t9Ubzvu+9e+5KNdu3u1PbOgU8OUfev/4CBqCHWN6cm4b3L+52VnD2rZcva2inZWcXefH4YOq/3N63Ex/s/hslTpmmPNA" );
				htmData.append("PF7SEpMzY3OMPrOWjQQO3UYC8+XXOOTOj10IMq4Kx7bK+cJ8LNHnGWEUcUsOONYkVLl23OFFFrewr0f+6/T5s2GJ4d/gx+CV2s7aVsO+WnDh/u3VvbCBmiRbieeGKQiphzudNK5X1R1K+olFOQlhtp3bqV3IfNMHr021q+9/Z8QK1ZDsHyk/t9gOSJDx9eV94n/E3r2LF5xplC+6J4brG25+DQ+fN/krBA" );
				htmData.append("Kw3jHLPIZRXHChXQr18fNGvaNN/Hdsba3/btERj91hh9yvEiOOaDlTrTF8Xlicc0qfIEjBk/CQdnfA3PUiXYQGTfopCQ006XYxStVQN1534Gj6K+SE06juh3QxA381t4lPS35YHpzqWgROsWqP3lJ7ZteT4FWG7/FnidONiYVuVwcS2d2xNzC/f35ZezVHDZeXS989OChTqNCoWdQnUtk61A" );
				htmData.append("Ggz/dvigovv2z+bNeHLgE9pmxymFp8/4VBvv77//P/aUOYNDYsaND8E9d/fQnlh6JhwCw06l114dqZ0T1ztGIA2Gqwi2KXGcJS1Gtuvxtb/evXuJyN0lrm7uKzDbMidM+BB/b9ykngSH7jz91DDxGGy9wtc7RiANBoPB8O/opDEYDIZ/I0YgDQaDIQuMQBoMBoNLgP8H9ROSk82otZEA" );
				htmData.append("AAAASUVORK5CYII=' height='80'>");	
			}
			
			htmData.append("</div> </div></nav>");
			htmData.append("<div class='container'><div class='row'><h2 class='page-header'>Test Execution Summary</h2><div class='panel panel-default'>"); 
			htmData.append("<div class='panel-heading'><strong>Execution Details</strong><div class='input-group pull-right'><span>Total Time: </span>");
			htmData.append("<span class='label label-default'>" + Util.getDurationFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), fts.get(fts.size()-1).getEndTime()) 
							+ "</span></div></div>");
			htmData.append("<div class='panel-body'><div class='col-sm-4'>Project Name: ");
			htmData.append("<span class='label label-default'>" + fts.get(0).getProjectName() + "</span></div>");
			htmData.append("<div class='col-sm-4'>Test Script ID: ");
			htmData.append("<span class='label label-default'>" + fts.get(0).getTestCaseID() + "</span></div><div class='col-sm-4'>Platform: ");
			htmData.append("<span class='label label-default'>" + fts.get(0).getPlatform() + "</span></div><div class='col-sm-4'>App Version: ");
			htmData.append("<span class='label label-default'>" + ( fts.get(0).getAppVersion()!=null ? fts.get(0).getAppVersion() : "" )+ "</span></div><div class='col-sm-4'>Run Date: ");
			htmData.append("<span class='label label-default'>" + Util.getCurrentDateFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_MM_DD_YYYY) 
							+ "</span></div><div class='col-sm-4'>Run Started: ");
			htmData.append("<span class='label label-default'>" + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_HH_MM_SS) 
							+ "</span></div><div class='col-sm-4'>Run Ended: ");
			htmData.append("<span class='label label-default'>" + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(fts.size()-1).getEndTime(), ParamsObject.PARAM_DATE_HH_MM_SS) 
							+ "</span></div><div class='col-sm-4'>Execution Status: ");
			htmData.append("<span class='label label-" + ( fts.get(0).getReportStatus() == ParamsObject.PARAM_HTM_PASSED_LABEL ? ParamsObject.PARAM_HTM_LABEL_SUCCESS : ParamsObject.PARAM_HTM_LABEL_DANGER )
							+ "'>" + fts.get(0).getReportStatus() + "</span></div>");
			htmData.append(referenceHTMLHeaderReport(fts));
			htmData.append("</div></div><div class='col-sm-12'>");
			htmData.append("<h2 class='sub-header'>Detailed Steps</h2><div class='table-responsive'><table class='table table-striped table-hover'><thead><tr><th>#</th><th>Test Step Description</th><th>Test Data</th><th>Expected Result</th><th style='text-align:center'>Result</th><th style='text-align:center'>Elapsed Time</th></tr></thead>");

			int totFailedSteps = 0;
			int totPassedSteps = 0;
			String htmStatus = "";
			String htmLabel = "";
			String htmError = "";
			htmData.append("<tbody>");
			int currRow = 0;
			for(int x =0; x < fts.size(); x++){
				
	        	if(!fts.get(x).isWriteToReport()) continue;
				
				htmData.append("<tr >");
				
				//#
				htmData.append("<td>" + ++currRow + "</td>");
				
				//TestStepDescription
				htmData.append("<td>" + fts.get(x).getStepName() + "</td>");
				
				//TestData
				htmData.append("<td>" + fts.get(x).getTestData() + "</td>");
				
				//ExpectedResult
				htmData.append("<td>" + fts.get(x).getExpectedResult() + "</td>");
				
				//Result
				if(fts.get(x).getStatus().equals(ParamsObject.PARAM_REPORT_PASSED)){
					totPassedSteps++;
					htmStatus = ParamsObject.PARAM_HTM_PASSED;
					htmLabel = ParamsObject.PARAM_HTM_LABEL_SUCCESS;
					htmError = "";
					htmData.append("<td align='center'><span class='label label-" + htmLabel + "'>" + htmStatus + "</span></td>");
				}else{
					htmStatus = ParamsObject.PARAM_HTM_FAILED;
					htmLabel = ParamsObject.PARAM_HTM_LABEL_DANGER;
					htmError = fts.get(x).getErrorMessage();
					totFailedSteps++;					
					htmData.append("<td align='center'><p><a id='"+x+"_arrows' class='arrows' onclick='expand_collapse("+x+");'>&#9660</a><span class='label label-" 
									+ htmLabel + "'>" + htmStatus + "</span>"
									+ "<div id='"+x+"' style='display:none;text-align: left;'>" + htmError + "</div></td>");
				}
				
				//Elapsed Time
				htmData.append("<td align='center'>");
				if(fts.get(x).getScreenshotFile()!=null){
					File checkFile = new File(fts.get(x).getScreenshotFile());
					if(checkFile.exists()){
					htmData.append("<a href='..\\SST\\" 
									+ fts.get(x).getScreenshotName() 
									+ "' target='_blank'>" 
									+ Util.getDurationFormatSeconds(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(x).getStartTime(), fts.get(x).getEndTime())
									+ "</a></td>");				
					}else{
						htmData.append(Util.getDurationFormatSeconds(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(x).getStartTime(), fts.get(x).getEndTime())
								+ "</td>");										
					}
				}
				else{ 
					htmData.append(Util.getDurationFormatSeconds(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(x).getStartTime(), fts.get(x).getEndTime())
							+ "</td>");				
				}

				htmData.append("</tr>");
			}
			htmData.append("</tbody>");
			htmData.append("</table></div></div><div class='col-sm-4 col-xs-6'><h3><span class='label label-danger col-sm-12 col-xs-12'>Total Failed Steps ");
			htmData.append("<span class='badge '>" + totFailedSteps + "</span></span></h3></div><div class='col-sm-4 col-xs-6'><h3><span class='label label-success col-sm-12 col-xs-12'>Total Passed Steps ");
			htmData.append("<span class='badge '>" + totPassedSteps + "</span></span></h3></div><div class='col-sm-4 col-xs-12'><h3><span class='label label-primary col-sm-12 col-xs-12'>Total Steps ");
			htmData.append("<span class='badge '>" + (totFailedSteps + totPassedSteps) + "</span></span></h3></div><div class='row'>&nbsp;</div><div class='row'>&nbsp;</div><div class='row'>&nbsp;</div><div class='row'>&nbsp;</div></div></div></body></html>");

			bw.write(htmData.toString());

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
	
	public String addColon(String p_value){
		return p_value.trim().endsWith(":")!=true ? p_value+": " : p_value+" " ;
	}

	public String removeColon(String p_value){
		return p_value.trim().endsWith(":")!=true ? p_value.trim() : p_value.trim().substring(0, p_value.length()-1);
	}
	
	public String referenceHTMLHeaderReport(ArrayList<FinalTestScriptObject> fts){
		String refHtmlValue = "";
		for(int r= 0; r < fts.size(); r++){
			if(fts.get(r).getReferenceReport()!=null && !fts.get(r).getReferenceReport().equals("")){
				refHtmlValue += "<div class='col-sm-4'>" 
						+ addColon(fts.get(r).getTestData()) 
						+ "<span class='label label-default'>"
						+ fts.get(r).getReferenceReport()
						+ "</span></div>";
			}
		}
		
		return refHtmlValue;
	}
	
	public void setRun(XWPFRun run , String fontFamily , int fontSize , String colorRGB , String text , boolean bold , boolean addBreak) {
        run.setFontFamily(fontFamily);
        run.setFontSize(fontSize);
        run.setColor(colorRGB);
        run.setText(text);
        run.setBold(bold);
        if (addBreak) run.addBreak();
	}
	
	public void setImageRun(XWPFRun imageRun, String strImagePath) throws InvalidFormatException, IOException{
		
		try{
			
			InputStream imgStream = new FileInputStream(strImagePath);
	
	 	    BufferedImage img = ImageIO.read(imgStream);
		    double w = img.getWidth();
		    double h = img.getHeight();
	
		    double scaling = Double.valueOf(Main.configurationObject.getImage().trim().replace("%", "")) / 100;
		    if (w > 72*6) scaling = (72*6)/w; //scale width not to be greater than 6 inches
			
			Path imagePath = Paths.get(strImagePath);
			imageRun.addPicture(Files.newInputStream(imagePath),
			  XWPFDocument.PICTURE_TYPE_PNG, imagePath.getFileName().toString(),
			  Units.toEMU(w*scaling), Units.toEMU(h*scaling));
		    imgStream.close();
		    img.flush();
		    
		}catch(Exception ex){
			
		}
	}
	
	public void referenceDocHeaderReport(ArrayList<FinalTestScriptObject> fts, XWPFParagraph para){
		for(int r= 0; r < fts.size(); r++){
			if(fts.get(r).getReferenceReport()!=null && !fts.get(r).getReferenceReport().equals("")){
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, removeColon(fts.get(r).getTestData()) + "		: ", true, false);
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(r).getReferenceReport(), false, true);								
			}
		}
	}
	
	public void generateReportDoc(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0032, MessageObject.MSG0032);

		try {
			XWPFDocument doc = new XWPFDocument(new FileInputStream(Main.configurationObject.getString_templateDocPath()));
			
			XWPFParagraph para = null;
			
			//Content
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.LEFT);
			
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_PROJECT_NAME + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(0).getProjectName(), false, true);
						
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_TEST_SCRIPT_ID + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(0).getTestCaseID(), false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_PLATFORM + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(0).getPlatform(), false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.CONFIG_REPORT_XLS_SHEET_GLOBAL_APP_VERSION + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(0).getAppVersion(), false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_RUN_DATE + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, Util.getCurrentDateFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_MM_DD_YYYY) , false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_RUN_STARTED + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), ParamsObject.PARAM_DATE_HH_MM_SS), false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_RUN_ENDED + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(fts.size()-1).getEndTime(), ParamsObject.PARAM_DATE_HH_MM_SS) , false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_EXECUTION_TIME + "		: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, Util.getDurationFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, fts.get(0).getStartTime(), fts.get(fts.size()-1).getEndTime()) , false, true);
			
			referenceDocHeaderReport(fts, para);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_EXECUTION_STATUS + "	: ", true, false);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ( fts.get(0).getReportStatus() == ParamsObject.PARAM_HTM_PASSED_LABEL ? ParamsObject.PARAM_DOC_COLOR_GREEN : ParamsObject.PARAM_DOC_COLOR_RED ), fts.get(0).getReportStatus(), false, true);

			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, "" , false, true);

			int currRow = 0;
			for(int v = 0; v < fts.size(); v++){
	        	if(!fts.get(v).isWriteToReport()) continue;

				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, "" , false, true);				
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ParamsObject.PARAM_DOC_TEST_STEP + " " + (++currRow) + "(", true, false);
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ( fts.get(v).getStatus() == ParamsObject.PARAM_REPORT_PASSED ? ParamsObject.PARAM_DOC_COLOR_GREEN : ParamsObject.PARAM_DOC_COLOR_RED ), fts.get(v).getStatus() , true, false);
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, ")	: ", true, false);
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, fts.get(v).getStepName() , false, true);
				
				setImageRun(para.createRun(), fts.get(v).getScreenshotFile());
				
				setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, "" , false, true);				
			}
			
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, "" , false, true);
			setRun(para.createRun(), ParamsObject.PARAM_DOC_FONT_FAMILY_CALIBRI, ParamsObject.PARAM_DOC_FONT_SIZE_11, ParamsObject.PARAM_DOC_COLOR_BLACK, MessageObject.MSG0034, true, false);			
			
			FileOutputStream os = new FileOutputStream(fts.get(0).getFolderDoc() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_DOCX_EXTENSIONS);
		    doc.write(os);
		    doc.close();
			os.close();
		} catch (Exception ex) {
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");
		}
			    
	}
	
	public void generateReportPdf(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0033, MessageObject.MSG0033);
		
		try {
            InputStream doc = new FileInputStream(new File(fts.get(0).getFolderDoc() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_DOCX_EXTENSIONS));
            XWPFDocument document = new XWPFDocument(doc);
            PdfOptions options = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(fts.get(0).getFolderPdf() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_PDF_EXTENSIONS));
            PdfConverter.getInstance().convert(document, out, options);
            doc.close();
            document.close();
            out.close();
        } catch (Exception ex) {
        	//Log.doLogger(Level.SEVERE, ex.getMessage(), "");
        	docToGenerateReportPdf(fts);
        } 
	}
	
	public void docToGenerateReportPdf(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0039, MessageObject.MSG0039);
		try{
			File f = new File(fts.get(0).getFolderPdf() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_PDF_EXTENSIONS);
			if(f.exists() && !f.isDirectory()){
				f.delete();
			}
			
			File doctoFile = new File(ParamsObject.PARAM_DOCTO_EXE);
			if(doctoFile.exists() && doctoFile.isFile()){
				//docto.exe -f "C:\Run\Test_Result\01_Demo_SEL09\DOC\01_Demo_SEL09.docx" -O "C:\Run\Test_Result\01_Demo_SEL09\DOC\01_Demo_SEL09_docto.pdf" -T wdFormatPDF
				String command = "\""+ doctoFile.getAbsoluteFile() +"\" -f \"" 
						+ fts.get(0).getFolderDoc() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_DOCX_EXTENSIONS
						+ "\" -O \""
						+ fts.get(0).getFolderPdf() + "//" + fts.get(0).getTestCaseID() + ParamsObject.PARAM_PDF_EXTENSIONS
						+ "\" -T wdFormatPDF"
						;
				//System.out.println(command);
				Process p = Runtime.getRuntime().exec(command);
				p.waitFor();
			}
			
		}catch(Exception ex){
        	Log.doLogger(Level.SEVERE, ex.getMessage(), "");			
		}
	}
	
	public void generateReport(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0020, MessageObject.MSG0020);
		Log.doLogger(Level.INFO, MessageObject.MSG0027, MessageObject.MSG0027);		
		Log.doLogger(Level.INFO, MessageObject.MSG0020, MessageObject.MSG0020);
		
		generateReportRpt(fts);
		generateReportXls(fts);
		generateReportHtm(fts);
		generateReportDoc(fts);
		generateReportPdf(fts);
	
		Log.doLogger(Level.INFO, MessageObject.MSG0028, MessageObject.MSG0028);		
	}
	
	public String getImageName(){
		String resultImageName = "";
		if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_IE)){
			resultImageName = ParamsObject.CONFIG_IE;		      
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_FIREFOX)){
			resultImageName = ParamsObject.CONFIG_FIREFOX;		      				
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_CHROME)){		
			resultImageName = ParamsObject.CONFIG_CHROME;		      
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_EDGE)){	
			resultImageName = ParamsObject.CONFIG_EDGE;		      
		}else if(Main.configurationObject.getPlatform().toLowerCase().equals(ParamsObject.PARAM_PLATFORM_SAFARI)){	
			resultImageName = ParamsObject.CONFIG_SAFARI;		      
		}else{
			resultImageName = ParamsObject.CONFIG_IE;		      			
		}
		return resultImageName;
	}
	
	public List<String> listRunningProcesses() throws IOException, Throwable {
	    List<String> processes = new ArrayList<String>();
	    try {
	      String line;
	      Process p = Runtime.getRuntime().exec(ParamsObject.CONFIG_TASKLIST_COMMAND + getImageName());
	      BufferedReader input = new BufferedReader
	          (new InputStreamReader(p.getInputStream()));
	      while ((line = input.readLine()) != null) {
	          if (!line.trim().equals("")) {
	              processes.add(line);
	          }

	      }
	      input.close();
	    }
	    catch (Exception ex) {
	      Log.doLogger(Level.SEVERE, ex.getMessage(), "");
	    }
	    return processes;
	}
	
	public void closeAllBrowser(int p) throws IOException, Throwable{
		try{
			List<DesktopWindow> allWindows = WindowUtils.getAllWindows(true);
			for (final DesktopWindow wnd : allWindows) {
			    if(wnd.getFilePath().trim().toLowerCase().endsWith(getImageName())){
				    User32.INSTANCE.PostMessage(wnd.getHWND(), WinUser.WM_CLOSE, null, null);		    		
			    }
			}
		}
		catch(Exception ex){
			Log.doLogger(Level.WARNING, MessageObject.MSG0038, "");
		}
	}
	
	public void closeBrowser() throws Throwable{
		//if have more time, can investigate again about this features... currently only applicable for close all browser and default.
		if(Main.configurationObject.getClosebrowser().trim().toLowerCase().equals(ParamsObject.PARAM_CB_DEFAULT.toLowerCase())){
			//do nothing : 0
		}else if(Main.configurationObject.getClosebrowser().trim().toLowerCase().equals(ParamsObject.PARAM_CB_CLOSE_ALL.toLowerCase())){
			//close all browser : 1
			closeAllBrowser(1);
		}else if(Main.configurationObject.getClosebrowser().trim().toLowerCase().equals(ParamsObject.PARAM_CB_CLOSE_ALL_EXCEPT_FIRST.toLowerCase())){
			//close all browser except first : 2
			closeAllBrowser(2);
		}else if(Main.configurationObject.getClosebrowser().trim().toLowerCase().equals(ParamsObject.PARAM_CB_CLOSE_ALL_EXCEPT_LAST.toLowerCase())){
			//close all browser except last : 3
			closeAllBrowser(3);
		}else if(Main.configurationObject.getClosebrowser().trim().toLowerCase().equals(ParamsObject.PARAM_CB_CLOSE_LATEST.toLowerCase())){
			//close latest browser : 4
			closeAllBrowser(4);
		}
	}
	
	public void initWebAutomation(ArrayList<FinalTestScriptObject> fts) throws IOException, Throwable{
		try{
			Log.doLogger(Level.INFO, MessageObject.MSG0012,"");
			fts.get(0).setProjectName(Main.configurationObject.getProjectname());
			fts.get(0).setPlatform(System.getProperty(ParamsObject.PARAM_OS_NAME));
			fts.get(0).setOutputPath(Main.configurationObject.getOutputpath());
			fts.get(0).setReportPath(Main.configurationObject.getOutputpath() + "\\" + fts.get(0).getTestCaseID());
			fts.get(0).setFolderDoc(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_DOC);
			fts.get(0).setFolderHtm(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_HTM);
			fts.get(0).setFolderLogs(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_LOGS);
			fts.get(0).setFolderPdf(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_PDF);
			fts.get(0).setFolderRpt(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_RPT);
			fts.get(0).setFolderSst(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_SST);
			fts.get(0).setFolderXls(fts.get(0).getReportPath() + "\\" + ParamsObject.PARAM_REPORT_FOLDER_XLS);
			prepareFolderReport(fts.get(0));
	
			closeBrowser();
			
			selectDriverAndNavigate(fts);
			
			if(wo.getWd()!=null){
				fts.get(0).setAppVersion(((RemoteWebDriver) wo.getWd()).getCapabilities().getBrowserName() + " " + ((RemoteWebDriver) wo.getWd()).getCapabilities().getVersion());
				wo.setWait(new WebDriverWait(wo.getWd(), Integer.parseInt(Main.configurationObject.getWaittime())));
			}
			Main.configurationObject.setString_currentLogPath(fts.get(0).getFolderLogs() + "\\" + ParamsObject.CONFIG_REPORT_LOG_NAME);
			Log.doLogger(Level.INFO, MessageObject.MSG0013,"");
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");
		}
	}
	
	protected boolean isAlertPresent() {
        try {
          wo.getWd().switchTo().alert();
          return true;
        } catch (Exception e) {
          return false;
        }
      }
	
	public void runSingleWebAutomation(ArrayList<FinalTestScriptObject> fts, int currentRun, int totalRun) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0016 + " < #" + currentRun + " : " + fts.get(0).getTestCaseID() + MessageObject.MSG0018 + totalRun, "");
		Main.mainDialog.getRunLabel().setText(ParamsObject.PARAM_GUI_RUN_LABEL + currentRun);
		Main.mainDialog.getPassedLabel().setText(ParamsObject.PARAM_GUI_PASSED_LABEL + Main.configurationObject.getPassed());
		Main.mainDialog.getFailedLabel().setText(ParamsObject.PARAM_GUI_FAILED_LABEL + Main.configurationObject.getFailed());
		initWebAutomation(fts);
		//core web automation here
				
		Log.doLogger(Level.INFO, MessageObject.MSG0020, MessageObject.MSG0020);
		Log.doLogger(Level.INFO, MessageObject.MSG0026 + fts.get(0).getTestCaseID(), MessageObject.MSG0021 + fts.get(0).getTestCaseID());		
		Log.doLogger(Level.INFO, MessageObject.MSG0020, MessageObject.MSG0020);
		int flagFailed = 0;
		//iterate all rows of final test script to run automation
		for(int a=0; a < fts.size(); a++){
		
			try{
				//skip if current keywords are cond_endif
				if(fts.get(a).getKeyword()!=null && fts.get(a).getKeyword().toLowerCase().equals(ParamsObject.KW_CONSTANT_END_IF)){
					fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					continue;
				}

				//Carry whether found the statement true from previous if statement
				if(a > 0 && fts.get(a-1).isIfelsestatement()!=null){
					fts.get(a).setIfelsestatement(fts.get(a-1).isIfelsestatement());
				}
				
				//Skip if previous statement are false and no keywords
				if(a > 0 
						&& fts.get(a-1).getIfelsestatement_result()!=null 
						&& fts.get(a-1).getIfelsestatement_result().toLowerCase().equals(ParamsObject.KW_CONSTANT_FALSE)
						&& ( fts.get(a).getKeyword()==null )
						){
					fts.get(a).setIfelsestatement_result(ParamsObject.KW_CONSTANT_FALSE);
					fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					continue;
				}
							
				//Skip if previous statement are false and have keywords but not if or else
				if(a > 0 
						&& fts.get(a-1).getIfelsestatement_result()!=null 
						&& fts.get(a-1).getIfelsestatement_result().toLowerCase().equals(ParamsObject.KW_CONSTANT_FALSE)
						&& (fts.get(a).getKeyword()!=null && (  !fts.get(a).getKeyword().toLowerCase().equals(ParamsObject.KW_CONSTANT_IF)
										&& !fts.get(a).getKeyword().toLowerCase().equals(ParamsObject.KW_CONSTANT_ELSE) ))
						){
					fts.get(a).setIfelsestatement_result(ParamsObject.KW_CONSTANT_FALSE);
					fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					continue;
				}

				//skip other if&else if statement true already found
				if( (fts.get(a).getKeyword()!=null &&
					(  fts.get(a).getKeyword().toLowerCase().equals(ParamsObject.KW_CONSTANT_IF)
						|| fts.get(a).getKeyword().toLowerCase().equals(ParamsObject.KW_CONSTANT_ELSE) ))
						&& fts.get(a-1).isIfelsestatement()!=null
						&& fts.get(a-1).isIfelsestatement().equals(ParamsObject.KW_CONSTANT_TRUE) ){
					fts.get(a).setIfelsestatement_result(ParamsObject.KW_CONSTANT_FALSE);		
					fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					continue;
				}			
				
				Log.doLogger(Level.INFO, MessageObject.MSG0021 + MessageObject.MSG0022 + " " + (a+1) + MessageObject.MSG0021 , MessageObject.MSG0021 + MessageObject.MSG0022 + " " + (a+1) + MessageObject.MSG0021 );
				Log.doLogger(Level.INFO, MessageObject.MSG0023 + fts.get(a).getStepName(), MessageObject.MSG0023 + fts.get(a).getStepName());
				fts.get(a).setStartTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
				fts.get(a).setNo(String.valueOf(a+1));
				fts.get(a).setIndex(String.valueOf(a+1));
				fts.get(a).setWriteToReport(true);			
				
				//Keyword
				if(fts.get(a).getKeyword()!=null && fts.get(a).getKeyword()!=""){

					//if any keywords added then required to add the log, reporting of end time, status, passed/failed for gui
						fts.get(a).setScreenshotFile(fts.get(0).getFolderSst() + "\\" + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
						fts.get(a).setScreenshotName(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
					if(!fts.get(a).getKeyword().trim().toLowerCase().equals(ParamsObject.KW_CONSTANT_IF) 
							|| !fts.get(a).getKeyword().trim().toLowerCase().equals(ParamsObject.KW_CONSTANT_ELSE) ){

						if(!isAlertPresent()){						
							captureScreenShot(fts.get(a).getScreenshotFile(), fts.get(a).getDescription());
						}
					}
					
					
					fts.get(a).updateObject(WebKeywords.switchToKeywords(fts.get(a), wo));
					
					if(fts.get(a).getStatus().equals(ParamsObject.PARAM_REPORT_PASSED)){
						Log.doLogger(Level.INFO, MessageObject.MSG0024 + ParamsObject.PARAM_LOG_PASSED,MessageObject.MSG0024 + ParamsObject.PARAM_LOG_PASSED);
						fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					}else{
						if(fts.get(a).isWriteToReport()) flagFailed++;
						Log.doLogger(Level.INFO, MessageObject.MSG0024 + ParamsObject.PARAM_LOG_FAILED,MessageObject.MSG0024 + ParamsObject.PARAM_LOG_FAILED);
						fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					}
					
				}else
				//Test Data
				{					
					//1. set field element
					wo.setFieldElement(fts.get(a));
					//2. add to test result/image
					fts.get(a).setScreenshotFile(fts.get(0).getFolderSst() + "\\" + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
					fts.get(a).setScreenshotName(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
					captureScreenShot(fts.get(a).getScreenshotFile(), fts.get(a).getDescription());
					//3. set value
					wo.setValueElement(fts.get(a));										
					Log.doLogger(Level.INFO, MessageObject.MSG0024 + ParamsObject.PARAM_LOG_PASSED,MessageObject.MSG0024 + ParamsObject.PARAM_LOG_PASSED);
					fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
					fts.get(a).setStatus(ParamsObject.PARAM_REPORT_PASSED);
				}
			}catch(Exception ex){
				if(fts.get(a).isWriteToReport()) flagFailed++;
				fts.get(a).setScreenshotFile(fts.get(0).getFolderSst() + "\\" + Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
				fts.get(a).setScreenshotName(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()) + "_" + String.valueOf(a+1) + ParamsObject.PARAM_PNG_EXTENSIONS);
				captureScreenShot(fts.get(a).getScreenshotFile(), fts.get(a).getDescription());

				Log.doLogger(Level.INFO, MessageObject.MSG0024 + ParamsObject.PARAM_LOG_FAILED,MessageObject.MSG0024 + ParamsObject.PARAM_LOG_FAILED);
				fts.get(a).setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
				fts.get(a).setStatus(ParamsObject.PARAM_REPORT_FAILED);
				fts.get(a).setErrorMessage(ex.getMessage());
				Log.doLogger(Level.SEVERE, ex.getMessage(), "");
			}
			Log.doLogger(Level.INFO, MessageObject.MSG0025 + fts.get(a).getTestData(),MessageObject.MSG0025 + fts.get(a).getTestData());
		}
		
		if(flagFailed == 0){
			Main.configurationObject.setPassed(Main.configurationObject.getPassed()+1);							
			fts.get(0).setReportStatus(ParamsObject.PARAM_HTM_PASSED_LABEL);
		}else{
			Main.configurationObject.setFailed(Main.configurationObject.getFailed()+1);							
			fts.get(0).setReportStatus(ParamsObject.PARAM_HTM_FAILED_LABEL);
		}

		generateReport(fts);
		
		closeSession();
		
		Main.mainDialog.getPassedLabel().setText(ParamsObject.PARAM_GUI_PASSED_LABEL + Main.configurationObject.getPassed());
		Main.mainDialog.getFailedLabel().setText(ParamsObject.PARAM_GUI_FAILED_LABEL + Main.configurationObject.getFailed());
		Log.doLogger(Level.INFO, MessageObject.MSG0017 + "/#" + currentRun + " : " + fts.get(0).getTestCaseID() + MessageObject.MSG0018 + totalRun + ">", "");
	}
	
	public void closeSession(){
		if(Main.configurationObject.getClose_browser_after_run().toLowerCase().equals(ParamsObject.PARAM_YES.toLowerCase())){
			if(wo.getWd()!=null) wo.getWd().close();	
		}
	}
	
	public void runWebAutomation(TestScript ts) throws IOException, Throwable{
		Log.doLogger(Level.INFO, MessageObject.MSG0014, "");
		Main.mainDialog.getProgressLabel().setText(ParamsObject.PARAM_GUI_RUN_TESTSCRIPT_LABEL);
		Main.mainDialog.getProgressBar().setMinimum(0);
		Main.mainDialog.getProgressBar().setMaximum(ts.getFinalTestScripts().size() - 1);
		Main.mainDialog.getTotalLabel().setText(ParamsObject.PARAM_GUI_TOTAL_LABEL + ts.getFinalTestScripts().size());
		Main.configurationObject.setPassed(0);
		Main.configurationObject.setFailed(0);
		
		for(int i =0; i < ts.getFinalTestScripts().size(); i++){
			runSingleWebAutomation(ts.getFinalTestScripts().get(i), i+1, ts.getFinalTestScripts().size());
			Main.mainDialog.getProgressBar().setValue(i);		
		}
		
		Log.doLogger(Level.INFO, MessageObject.MSG0015, "");
	}
	
}

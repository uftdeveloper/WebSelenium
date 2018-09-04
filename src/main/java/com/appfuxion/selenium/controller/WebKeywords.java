package com.appfuxion.selenium.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import com.appfuxion.selenium.model.FinalTestScriptObject;
import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;
import com.appfuxion.selenium.model.WebObject;
import com.appfuxion.selenium.view.Main;

public class WebKeywords {
	
	private static boolean dataWrite(FinalTestScriptObject ftso, WebObject wo, String result) throws IOException, Throwable{
		
		try{
			
	    	BufferedWriter bw = null;
			FileWriter fw = null;
			
			new File(Main.configurationObject.getString_datawritePath()).mkdirs();

			File file = new File(Main.configurationObject.getString_datawritePath() + "//" + ftso.getExpectedResult() + ParamsObject.PARAM_TXT_EXTENSIONS);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file. false = don't append file.
			fw = new FileWriter(file.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);

			bw.write(result);			
			
			bw.close();
			fw.close();
			return true;
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");					
			return false;
		}
	}
	
	public static String dataRead(FinalTestScriptObject ftso) throws IOException, Throwable{
		try{
			String fileName = "";
			
			if(ftso.getTestData()!=null && !ftso.getTestData().trim().equals(""))
				fileName = ftso.getTestData();
			else
				fileName = ftso.getExpectedResult();
			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader(Main.configurationObject.getString_datawritePath() + "//" + fileName + ParamsObject.PARAM_TXT_EXTENSIONS);
			br = new BufferedReader(fr);

			String sCurrentLine;
			StringBuilder sAllLine = new StringBuilder();

			while ((sCurrentLine = br.readLine()) != null) {
				sAllLine.append(sCurrentLine);
			}

			br.close();
			fr.close();
			
			return sAllLine!=null ? sAllLine.toString() : "";
			
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");					
			return "";			
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void setHighlightXls(FinalTestScriptObject ftso, short indexColor) throws Throwable{
		
		if(!Main.configurationObject.getHighlight_verify_xls().toLowerCase().trim().equals(ParamsObject.PARAM_ON.toLowerCase())) return;
		
        FileInputStream excelFile;
		try {
			excelFile = new FileInputStream(new File(ftso.getTestScriptAbsolutePath()));
	        Workbook workbook = new XSSFWorkbook(excelFile);
	        Sheet datatypeSheet = workbook.getSheetAt(1);
	        Row myRow = datatypeSheet.getRow(ftso.getTestDataRowNumber()); //Get first row
	        Cell myCell = myRow.getCell(ftso.getTestDataColumnNumber());
	        CellStyle style = workbook.createCellStyle();
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		    //style.setFillBackgroundColor(indexColor);
		    style.setFillForegroundColor(indexColor);
		    myCell.setCellStyle(style);
		    workbook.write(new FileOutputStream(new File(ftso.getTestScriptAbsolutePath())));
		    workbook.close();
		    excelFile.close();
		} catch (Exception ex) {
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");					
		}

	}
			
	public static FinalTestScriptObject switchToKeywords(FinalTestScriptObject ftso, WebObject wo) throws IOException, Throwable{

		try{
			String result = "";
			switch(ftso.getKeyword().toLowerCase().trim()){
				//[verify] : Verification Expected Result versus Actual Result
				case ParamsObject.KW_CONSTANT_VERIFY :
					wo.setFieldElement(ftso);
					result = wo.getValueElement(ftso);
					if(result!= null
							&& result.toLowerCase().equals(ftso.getExpectedResult().toLowerCase().trim())){
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);		
						ftso.setErrorMessage(MessageObject.MSG0035 + result);
					}
					break;
				//[data_write] : This is used to store data of actual data in a text file locally. 
				case ParamsObject.KW_CONSTANT_DATA_WRITE :
					wo.setFieldElement(ftso);
					result = wo.getValueElement(ftso);
					if(result!=null){
						boolean flag = dataWrite(ftso, wo, result);
						if(flag) 
							ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
						else 
							ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);		
					}else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);		
						ftso.setErrorMessage(MessageObject.MSG0036);
					}
				break;

				//[data_read] : This is used to retrieve the data in text file. The specified identifier can be placed either in test data or expected_result depending on the test step. 
				//In this sample, the retrieved data is used as test data
				case ParamsObject.KW_CONSTANT_DATA_READ :
					result = dataRead(ftso);
					if(result!=""){
						wo.setFieldElement(ftso);
						ftso.setTestData(result);						
						wo.setValueElement(ftso);
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);								
						ftso.setErrorMessage(MessageObject.MSG0037);
					}
				break;

				//[data_read attr_val] : This is a sample combination of keywords attr_val and data_read.
				//The retrieved data will be substituted in the object id
				case ParamsObject.KW_CONSTANT_DATA_READ_ATT_VAL :
					result = dataRead(ftso);
					if(result!=""){
						String objectID = ftso.getObjectID();
						objectID = objectID.replace(ParamsObject.KW_CONSTANT_ATTR_VAL, result);
						ftso.setObjectID(objectID);
						wo.setFieldElement(ftso);
						wo.setValueElement(ftso);
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);								
						ftso.setErrorMessage(MessageObject.MSG0037);
					}
				break;

				//[addreference] : Framework able capture the dynamic value and display in report.
				case ParamsObject.KW_CONSTANT_ADDREFERENCE :
					ftso.setWriteToReport(false);
					wo.setFieldElement(ftso);
					result = wo.getValueElement(ftso);
					if(result!= null){
						ftso.setReferenceReport(result);
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}
				break;

				
				//[cond_if] 
				case ParamsObject.KW_CONSTANT_IF :
					ftso.setWriteToReport(false);
					boolean flag = false;
					if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_EQUAL)){
						flag = ftso.getTestData().equals(ftso.getExpectedResult());
					}else if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_NOT_EQUAL)){
						flag = !ftso.getTestData().equals(ftso.getExpectedResult());
					}else if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_GREATER_THAN)){
						flag = Integer.valueOf(ftso.getTestData()) > Integer.valueOf(ftso.getExpectedResult());
					}else if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_LESSER_THAN)){
						flag = Integer.valueOf(ftso.getTestData()) < Integer.valueOf(ftso.getExpectedResult());
					}else if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_GREATER_THAN_EQUAL)){
						flag = Integer.valueOf(ftso.getTestData()) >= Integer.valueOf(ftso.getExpectedResult());
					}else if(ftso.getAction().toLowerCase().trim().equals(ParamsObject.KW_CONSTANT_CONDITION_LESSER_THAN_EQUAL)){
						flag = Integer.valueOf(ftso.getTestData()) <= Integer.valueOf(ftso.getExpectedResult());
					}
					ftso.setIfelsestatement_result(String.valueOf(flag));
					ftso.setIfelsestatement(String.valueOf(flag));
					
					if(flag) ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					else ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);	
					
				break;
				
				//[cond_else] 
				case ParamsObject.KW_CONSTANT_ELSE :
					ftso.setWriteToReport(false);
					ftso.setIfelsestatement_result(String.valueOf(true));
					ftso.setIfelsestatement(String.valueOf(true));		
					ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					
				//[verify_xls_docx] 
				case ParamsObject.KW_CONSTANT_VERIFY_XLS_DOCX :
					flag = false;
					
					try{
						String regex = "";
						XWPFDocument doc = new XWPFDocument(OPCPackage.open(ftso.getExpectedResult()));
						for (XWPFParagraph p : doc.getParagraphs()) {
						    List<XWPFRun> runs = p.getRuns();
						    if (runs != null) {
						        for (XWPFRun r : runs) {
						            String text = r.getText(0);
						            if (text != null && text.contains(ftso.getObjectID().trim())) {
					            		regex = ".*" + ftso.getObjectID() + ".*" + ftso.getTestData() + ".*";
										flag = text.matches(regex);
										if(flag){
											ftso.setExpectedResult(text);
											break;
										}
						            }
						        }
						    }
						    if(flag) break;
						}	
						doc.close();
					}
					catch(Exception ex){
					}
					
					if(flag){
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);						
						setHighlightXls(ftso, IndexedColors.GREEN.getIndex());
					}
					else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);	
						ftso.setExpectedResult(MessageObject.MSG0040 + ftso.getTestData());
						setHighlightXls(ftso, IndexedColors.RED.getIndex());
					}
				break;

				//[verify_xls_txt] 
				case ParamsObject.KW_CONSTANT_VERIFY_XLS_TXT :
					flag = false;
					
					try (BufferedReader br = new BufferedReader(new FileReader(ftso.getExpectedResult()))) {

						String line;
						String regex = "";
						while ((line = br.readLine()) != null) {
							if (line != null && line.contains(ftso.getObjectID().trim())) {
			            		regex = ".*" + ftso.getObjectID() + ".*" + ftso.getTestData() + ".*";
			            		flag = line.matches(regex);
								if(flag){
									ftso.setExpectedResult(line);
									break;
								}
				            }
						}
					
					}
					catch(Exception ex){
					}
					
					if(flag){
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);						
						setHighlightXls(ftso, IndexedColors.GREEN.getIndex());
					}
					else{
						ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);	
						ftso.setExpectedResult(MessageObject.MSG0040 + ftso.getTestData());
						setHighlightXls(ftso, IndexedColors.RED.getIndex());
					}
				break;
				
				//[cleaninput] Clear the input before add new test data
				case ParamsObject.KW_CONSTANT_CLEAN_INPUT :
					//1. set field element
					wo.setFieldElement(ftso);
					//3. clear value
					wo.clearValueElement(ftso);
					//3. set value
					wo.setValueElement(ftso);										
					ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
				break;

				//[close_pop_up_window] Close pop up window
				case ParamsObject.KW_CONSTANT_CLOSE_POP_UP_WINDOW :
					ftso.setWriteToReport(false);

					String parent = wo.getWd().getWindowHandle();

			        Set<String> pops=wo.getWd().getWindowHandles();
			        for(String popupHandle : pops){
			            if(!popupHandle.contains(parent))
			            {
			            	wo.getWd().switchTo().window(popupHandle);
				            //System.out.println("Pop up Up Title: "+ wo.getWd().switchTo().window(popupHandle).getTitle());
				            wo.getWd().close();
			            }
			        }

			        Set<String> myWindows=wo.getWd().getWindowHandles();
			        for(String mywindow : myWindows){
			            if(mywindow.contains(parent))
			            {
			            	wo.getWd().switchTo().window(mywindow);
				        	break;			            	
			            }
			        }
			        
					ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
				break;
				
				//[close_new_tab_window] Close new tab window
				case ParamsObject.KW_CONSTANT_CLOSE_NEW_TAB_WINDOW :
					ftso.setWriteToReport(false);

					String mainWindow = wo.getWd().getWindowHandle();

			        Set<String> tabs=wo.getWd().getWindowHandles();
			        for(String tabHandle : tabs){
			            if(!tabHandle.contains(mainWindow))
			            {
			            	wo.getWd().switchTo().window(tabHandle);
				            wo.getWd().close();
			            }
			        }

			        Set<String> allWindows=wo.getWd().getWindowHandles();
			        for(String mywindow : allWindows){
			            if(mywindow.contains(mainWindow))
			            {
			            	wo.getWd().switchTo().window(mywindow);
				        	break;			            	
			            }
			        }
			        
					ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
				break;				

				//[alertAccept] To click on the 'OK' button of the alert
				case ParamsObject.KW_CONSTANT_ALERT_ACCEPT :
					try { 
						ftso.setWriteToReport(false);
						//Now the alert appears. 
						Alert alert = wo.getWd().switchTo().alert();
						alert.accept();

						//parent = wo.getWd().getWindowHandle();
						//wo.getWd().switchTo().window(parent);
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}
					catch (Exception e) {
					    //do what you normally would if you didn't have the alert.
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}
				break;					
				
				//[alertDismiss] To click on the 'Cancel' button of the alert
				case ParamsObject.KW_CONSTANT_ALERT_DISMISS :
					try { 
						ftso.setWriteToReport(false);
						
						//Now the alert appears. 
						Alert alert = wo.getWd().switchTo().alert();
						alert.dismiss();
						
						//parent = wo.getWd().getWindowHandle();
						//wo.getWd().switchTo().window(parent);
				        
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}
					catch (Exception e) {
					    //do what you normally would if you didn't have the alert.
						ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
					}
				break;		
				
				//[wait my object] To wait on specific object
				case ParamsObject.KW_CONSTANT_WAIT_MY_OBJECT :
					ftso.setWriteToReport(false);
					Thread.sleep( Long.valueOf(ftso.getDescription()) * 1000 );					
					ftso.setStatus(ParamsObject.PARAM_REPORT_PASSED);
				break;					

				default:
				break;
			}
			
		}catch(Exception ex){
			ftso.setEndTime(Util.getCurrentTimeFormat(ParamsObject.PARAM_DATE_YYYY_MM_DD_HH_MM_SS_REPORT, Log.getCurrLogDate()));
			ftso.setStatus(ParamsObject.PARAM_REPORT_FAILED);
			ftso.setErrorMessage(ex.getMessage());			
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");					
		}
		
		return ftso;

	}
	
}

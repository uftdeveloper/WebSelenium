package com.appfuxion.selenium.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.appfuxion.selenium.model.FinalTestScriptObject;
import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;
import com.appfuxion.selenium.model.TestScriptObject;
import com.appfuxion.selenium.view.Main;
import com.poiji.bind.Poiji;

public class TestScript {
	
	private ArrayList<ArrayList<FinalTestScriptObject>> finalTestScripts = new ArrayList<ArrayList<FinalTestScriptObject>>();
	
	public ArrayList<ArrayList<FinalTestScriptObject>> getFinalTestScripts() {
		return finalTestScripts;
	}

	public void setFinalTestScripts(ArrayList<ArrayList<FinalTestScriptObject>> finalTestScripts) {
		this.finalTestScripts = finalTestScripts;
	}

	public void prepareTestScript(String[] args) throws IOException, Throwable{		
		Main.mainDialog.getProgressLabel().setText(ParamsObject.PARAM_GUI_PREPARING_TESTSCRIPT_LABEL);
		Main.mainDialog.getProgressBar().setMinimum(0);
		Main.mainDialog.getProgressBar().setMaximum(args.length - 1);
		for(int i = 0 ; i < args.length; i++){
			finalTestScripts.addAll(readTestScript(args[i]));
			Main.mainDialog.getProgressBar().setValue(i);
		}
		Log.doLogger(Level.INFO, MessageObject.MSG0007 + finalTestScripts.size() + ParamsObject.PARAM_UNIT_FINAL_TEST_SCRIPT, "");
	}
	
	@SuppressWarnings("unused")
	public int getColumnNumberDynamicValue(String template, Row currentRow, Map<String, Integer> map) throws IOException, Throwable{
		
		try{
			
		    StringBuilder sb = new StringBuilder();
		    int prev = 0;
		    for (int start, end; (start = template.indexOf("{", prev)) > -1; prev = end + 1) {
		        sb.append(template.substring(prev, start));
		        end = template.indexOf('}', start + 1);
		        if (end < -1) {
		            prev = start;
		            break;
		        }
		        String key = template.substring(start + 1, end);
	
		        return map.get(key);
	
		    }
		    sb.append(template.substring(prev));
		    return -1;
		    
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, ex.getMessage(), "");
			return -1;
		}
	}
	
	public String parseDynamicValue(String template, Row currentRow, Map<String, Integer> map) throws IOException, Throwable{
		
		try{
			
		    StringBuilder sb = new StringBuilder();
		    int prev = 0;
		    for (int start, end; (start = template.indexOf("{", prev)) > -1; prev = end + 1) {
		        sb.append(template.substring(prev, start));
		        end = template.indexOf('}', start + 1);
		        if (end < -1) {
		            prev = start;
		            break;
		        }
		        String key = template.substring(start + 1, end);
		        String value = "";
	
		        Cell currentCell = currentRow.getCell(map.get(key));
	            if (currentCell.getCellTypeEnum() == CellType.STRING) {
	            	value = currentCell.getStringCellValue();
	            } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
	            	value = Double.toString(currentCell.getNumericCellValue());
	            }	        
		        
		        if (value == null || value == "") {
		            sb.append("");
		        } else {
		            sb.append(value);
		        }
	
		    }
		    sb.append(template.substring(prev));
		    return sb.toString();
		    
		}catch(Exception ex){
			Log.doLogger(Level.SEVERE, "ERROR : "+ template +" ; "+ ex.getMessage(), "");
			return "";
		}
		
	}
		
	@SuppressWarnings({ "resource" })
	public ArrayList<ArrayList<FinalTestScriptObject>> readTestScript(String filePath) throws IOException, Throwable{
		ArrayList<ArrayList<FinalTestScriptObject>> testScripts = new ArrayList<ArrayList<FinalTestScriptObject>>();
		Log.doLogger(Level.INFO, MessageObject.MSG0009+" - "+filePath, "");
		ArrayList<TestScriptObject> tsObjects = (ArrayList<TestScriptObject>) Poiji.fromExcel(new File(filePath), TestScriptObject.class);
		Log.doLogger(Level.INFO, MessageObject.MSG0008 + tsObjects.size() + ParamsObject.PARAM_UNIT_STEP, "");
		try {
    		Log.doLogger(Level.INFO, MessageObject.MSG0010, "");

    		int currExcelRow = -1;
            FileInputStream excelFile = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(excelFile);
            
        	if(workbook.getSheetAt(1)!=null && workbook.getSheetAt(1).getSheetName().equals(ParamsObject.PARAM_TEST_DATA_SHEET)){
                Sheet datatypeSheet = workbook.getSheetAt(1);
                
                //get header map (string name , index column)
                Map<String, Integer> map = new HashMap<String,Integer>(); //Create map
                Row headerRow = datatypeSheet.getRow(0); //Get first row
                //following is boilerplate from the java doc
                short minColIx = headerRow.getFirstCellNum(); //get the first column index for a row
                short maxColIx = headerRow.getLastCellNum(); //get the last column index for a row
                for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
                   Cell cell = headerRow.getCell(colIx); //get the cell
                   map.put(cell.getStringCellValue().trim(),cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
                }                
                
                Iterator<Row> iterator = datatypeSheet.iterator();

                //**iterate go get all rows test data
                while (iterator.hasNext()) {
                	currExcelRow++;

                    Row currentRow = iterator.next();
                    //if test data row td_run != Y, then move to next row
                    if(currentRow.getCell(0)==null || !currentRow.getCell(0).getStringCellValue().equals(ParamsObject.PARAM_Y)){
                    	continue;
                    }
                    //iterate the testscript object to convert it into final testscript object
                    ArrayList<FinalTestScriptObject> tmpTsObjects = new ArrayList<FinalTestScriptObject>();
                    
                    //tmpTsObjects.addAll(tsObjects);
                    for(int j = 0; j < tsObjects.size(); j++){
                    	//create tsObjects that have different class with poiji.
                    	tmpTsObjects.add(new FinalTestScriptObject(tsObjects.get(j)));
                        tmpTsObjects.get(j).setTestScriptAbsolutePath(filePath);
                        tmpTsObjects.get(j).setTestDataRowNumber(currExcelRow);
                    	//Test Data
                    	if(tsObjects.get(j).getTestData()!=null && tsObjects.get(j).getTestData().trim().indexOf("{") > -1 ){                    		
                			tmpTsObjects.get(j).setTestData(parseDynamicValue(tsObjects.get(j).getTestData().trim() , currentRow, map));
                			tmpTsObjects.get(j).setTestDataColumnNumber(getColumnNumberDynamicValue(tsObjects.get(j).getTestData().trim() , currentRow, map));
                    	}
                    	//Step Name
                    	if(tsObjects.get(j).getStepName()!=null && tsObjects.get(j).getStepName().trim().indexOf("{") > -1){                    		
                			tmpTsObjects.get(j).setStepName(parseDynamicValue(tsObjects.get(j).getStepName().trim() , currentRow, map));
                    	}
                    	//Object ID
                    	if(tsObjects.get(j).getObjectID() !=null && tsObjects.get(j).getObjectID().trim().indexOf("{") > -1){                    		
                			tmpTsObjects.get(j).setObjectID(parseDynamicValue(tsObjects.get(j).getObjectID().trim() , currentRow, map));
                    	}
                    	//Keyword
                    	if(tsObjects.get(j).getKeyword()!=null && tsObjects.get(j).getKeyword().trim().indexOf("{") > -1 ){                    		
                			tmpTsObjects.get(j).setKeyword(parseDynamicValue(tsObjects.get(j).getKeyword().trim() , currentRow, map));
                    	}
                    	//ExpectedResult
                    	if(tsObjects.get(j).getExpectedResult()!=null && tsObjects.get(j).getExpectedResult().trim().indexOf("{") > -1){                    		
                			tmpTsObjects.get(j).setExpectedResult(parseDynamicValue(tsObjects.get(j).getExpectedResult().trim() , currentRow, map));
                    	}
                    	tmpTsObjects.get(j).setTestCaseID(currentRow.getCell(1).getStringCellValue());
                    }
                    
                    //System.out.println(tmpTsObjects.toString());
                    testScripts.add(tmpTsObjects);
                    //System.out.println();
                }                
        		Log.doLogger(Level.INFO, MessageObject.MSG0011 + testScripts.size() + ParamsObject.PARAM_UNIT_ROW, "");
        	}else{
        		//TestScript with no TestData sheet.
        		ArrayList<FinalTestScriptObject> tmpTsObjects = new ArrayList<FinalTestScriptObject>();
                //tmpTsObjects.addAll(tsObjects);
                for(int k = 0; k < tsObjects.size(); k++){
                	//create tsObjects that have different class with poiji.
                	tmpTsObjects.add(new FinalTestScriptObject(tsObjects.get(k)));
                }
        		testScripts.add(tmpTsObjects);
        		Log.doLogger(Level.INFO, MessageObject.MSG0011, "");
        	}
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return testScripts;
	}
}

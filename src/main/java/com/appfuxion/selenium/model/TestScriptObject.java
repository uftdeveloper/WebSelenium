package com.appfuxion.selenium.model;

import com.poiji.annotation.ExcelCell;

public class TestScriptObject {
	
	private String testCaseID;
	
	@ExcelCell(0)
	private String stepName;
	
	@ExcelCell(1)
	private String objectType;
	
	@ExcelCell(2)
	private String objectID;
	
	@ExcelCell(3)
	private String objectFrame;

	@ExcelCell(4)
	private String action;
	
	@ExcelCell(5)
	private String testData;
	
	@ExcelCell(6)
	private String keyword;
	
	@ExcelCell(7)
	private String applicationPath;

	@ExcelCell(8)
	private String driver;

	@ExcelCell(9)
	private String environment;

	@ExcelCell(10)
	private String description;
	
	@ExcelCell(11)
	private String expectedResult;
	
	@Override
    public String toString() {
        return "TestScript{" +
                "testCaseID=" + ( testCaseID !=null ? testCaseID : "" ) +
                ", stepName=" + ( stepName !=null ? stepName : "" ) +
                ", objectType='" + ( objectType !=null ? objectType : "" ) + '\'' +
                ", objectID='" + ( objectID !=null ? objectID : "" ) + '\'' +
                ", objectFrame='" + ( objectFrame !=null ? objectFrame : "" ) + '\'' +
                ", action=" + ( action !=null ? action : "" ) +
                ", testData=" + ( testData !=null ? testData : "" ) +
                ", keyword='" + (keyword !=null ? keyword : "" ) + '\'' +
                ", applicationPath='" + ( applicationPath!=null ? applicationPath : "" ) + '\'' +
                ", driver='" + ( driver !=null ? driver : "" ) + '\'' +
                ", environment='" + (environment !=null ? environment : "" ) + '\'' +
                ", description='" + (description !=null ? description : "" ) + '\'' +
                ", expectedResult='" + (expectedResult !=null ? expectedResult : "" ) + '\'' +
                '}' + "\n";
    }

	public String getTestCaseID() {
		return testCaseID;
	}

	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}
	
	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public String getObjectFrame() {
		return objectFrame;
	}

	public void setObjectFrame(String objectFrame) {
		this.objectFrame = objectFrame;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	
}

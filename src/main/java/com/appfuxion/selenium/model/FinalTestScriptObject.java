package com.appfuxion.selenium.model;


public class FinalTestScriptObject {
	private String testCaseID;
	
	private String stepName;
	
	private String objectType;
	
	private String objectID;
	
	private String objectFrame;

	private String action;
	
	private String testData;
	
	private String keyword;
	
	private String applicationPath;

	private String driver;

	private String environment;

	private String description;
	
	private String expectedResult;
	
	private String no;
	
	private String status;
	
	private String startTime;
	
	private String endTime;
	
	private String index;
	
	private String projectName;
	
	private String outputPath;
	
	private String platform;
	
	private String appVersion;
	
	private String reportPath;
	
	private String reportDate;
	
	private String reportDuration;
	
	private String folderDoc;
	
	private String folderHtm;
	
	private String folderLogs;
	
	private String folderPdf;
	
	private String folderRpt;
	
	private String folderSst;
	
	private String folderXls;
	
	private String screenshotFile;
	
	private String reportStatus;
	
	private String screenshotName;
	
	private String errorMessage;
	
	private boolean writeToReport;
	
	private String referenceReport;
		
	private String ifelsestatement_result;

	private String ifelsestatement;
	
	private String testScriptAbsolutePath;
	
	private int testDataRowNumber;
	
	private int testDataColumnNumber;

	public int getTestDataColumnNumber() {
		return testDataColumnNumber;
	}

	public void setTestDataColumnNumber(int testDataColumnNumber) {
		this.testDataColumnNumber = testDataColumnNumber;
	}

	public int getTestDataRowNumber() {
		return testDataRowNumber;
	}

	public void setTestDataRowNumber(int testDataRowNumber) {
		this.testDataRowNumber = testDataRowNumber;
	}

	public String getTestScriptAbsolutePath() {
		return testScriptAbsolutePath;
	}

	public void setTestScriptAbsolutePath(String testScriptAbsolutePath) {
		this.testScriptAbsolutePath = testScriptAbsolutePath;
	}

	public String getIfelsestatement() {
		return ifelsestatement;
	}

	public String isIfelsestatement() {
		return ifelsestatement;
	}

	public void setIfelsestatement(String ifelsestatement) {
		this.ifelsestatement = ifelsestatement;
	}

	public String getIfelsestatement_result() {
		return ifelsestatement_result;
	}

	public void setIfelsestatement_result(String ifelsestatement_result) {
		this.ifelsestatement_result = ifelsestatement_result;
	}

	public String getReferenceReport() {
		return referenceReport;
	}

	public void setReferenceReport(String referenceReport) {
		this.referenceReport = referenceReport;
	}

	public void updateObject (FinalTestScriptObject ftso) {
		this.testCaseID = ftso.getTestCaseID();
		this.stepName = ftso.getStepName();
		this.objectType = ftso.getObjectType();
		this.objectID = ftso.getObjectID();
		this.objectFrame = ftso.getObjectFrame();
		this.action = ftso.getAction();
		this.testData = ftso.getTestData();
		this.keyword = ftso.getKeyword();
		this.applicationPath = ftso.getApplicationPath();
		this.driver = ftso.getDriver();
		this.environment = ftso.getEnvironment();
		this.description = ftso.getDescription();
		this.expectedResult = ftso.getExpectedResult();
		this.no = ftso.getNo();
		this.status = ftso.getStatus();
		this.startTime = ftso.getStartTime();
		this.endTime = ftso.getEndTime();
		this.index = ftso.getIndex();
		this.projectName = ftso.getProjectName();
		this.outputPath = ftso.getOutputPath();
		this.platform = ftso.getPlatform();
		this.appVersion = ftso.getAppVersion();
		this.reportPath = ftso.getReportPath();
		this.reportDate = ftso.getReportDate();
		this.reportDuration = ftso.getReportDuration();
		this.folderDoc = ftso.getFolderDoc();
		this.folderHtm = ftso.getFolderHtm();
		this.folderLogs = ftso.getFolderLogs();
		this.folderPdf = ftso.getFolderPdf();
		this.folderRpt = ftso.getFolderRpt();
		this.folderSst = ftso.getFolderSst();
		this.folderXls = ftso.getFolderXls();
		this.screenshotFile = ftso.getScreenshotFile();
		this.reportStatus = ftso.getReportStatus();
		this.screenshotName = ftso.getScreenshotName();
		this.errorMessage = ftso.getErrorMessage();
		this.writeToReport = ftso.isWriteToReport();
		this.referenceReport = ftso.getReferenceReport();
		this.ifelsestatement_result = ftso.getIfelsestatement_result();
		this.ifelsestatement = ftso.isIfelsestatement();
		this.testScriptAbsolutePath = ftso.getTestScriptAbsolutePath();
		this.testDataRowNumber = ftso.getTestDataRowNumber();
		this.testDataColumnNumber = ftso.getTestDataColumnNumber();
	}

	public boolean isWriteToReport() {
		return writeToReport;
	}

	public void setWriteToReport(boolean writeToReport) {
		this.writeToReport = writeToReport;
	}

	public String getErrorMessage() {
		return errorMessage == null ? "" : errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getScreenshotName() {
		return screenshotName;
	}

	public void setScreenshotName(String screenshotName) {
		this.screenshotName = screenshotName;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getScreenshotFile() {
		return screenshotFile;
	}

	public void setScreenshotFile(String screenshotFile) {
		this.screenshotFile = screenshotFile;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportDuration() {
		return reportDuration;
	}

	public void setReportDuration(String reportDuration) {
		this.reportDuration = reportDuration;
	}

	public String getFolderDoc() {
		return folderDoc;
	}

	public void setFolderDoc(String folderDoc) {
		this.folderDoc = folderDoc;
	}

	public String getFolderHtm() {
		return folderHtm;
	}

	public void setFolderHtm(String folderHtm) {
		this.folderHtm = folderHtm;
	}

	public String getFolderLogs() {
		return folderLogs;
	}

	public void setFolderLogs(String folderLogs) {
		this.folderLogs = folderLogs;
	}

	public String getFolderPdf() {
		return folderPdf;
	}

	public void setFolderPdf(String folderPdf) {
		this.folderPdf = folderPdf;
	}

	public String getFolderRpt() {
		return folderRpt;
	}

	public void setFolderRpt(String folderRpt) {
		this.folderRpt = folderRpt;
	}

	public String getFolderSst() {
		return folderSst;
	}

	public void setFolderSst(String folderSst) {
		this.folderSst = folderSst;
	}

	public String getFolderXls() {
		return folderXls;
	}

	public void setFolderXls(String folderXls) {
		this.folderXls = folderXls;
	}

	public FinalTestScriptObject(TestScriptObject testScriptObject) {
		super();
		this.testCaseID = testScriptObject.getTestCaseID();
		this.stepName = testScriptObject.getStepName();
		this.objectType = testScriptObject.getObjectType();
		this.objectID = testScriptObject.getObjectID();
		this.objectFrame = testScriptObject.getObjectFrame();
		this.action = testScriptObject.getAction();
		this.testData = testScriptObject.getTestData();
		this.keyword = testScriptObject.getKeyword();
		this.applicationPath = testScriptObject.getApplicationPath();
		this.driver = testScriptObject.getDriver();
		this.environment = testScriptObject.getEnvironment();
		this.description = testScriptObject.getDescription();
		this.expectedResult = testScriptObject.getExpectedResult();
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
		return testData == null ? "" : testData;
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
		return expectedResult == null ? "" : expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
	
	
}

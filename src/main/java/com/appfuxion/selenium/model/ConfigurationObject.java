package com.appfuxion.selenium.model;

public class ConfigurationObject {
	
	private String String_currentLogPath = ParamsObject.CONFIG_REPORT_LOG_NAME;
	private String String_configpath = "../config.ini";
	private String String_templateDocPath = "Template.docx";
	private String String_datawritePath = "C:\\autodata";
	private String about;
	private String defaultpath;
	private String recursive;
	private String projectname;
	private String platform;
	private String waittime;
	private String synctimeout;
	private String outputpath;
	private String tracelog;
	private String getnow;
	private String web;
	private String mobile;
	private String win;
	private String te;
	private String uia;
	private String vb;
	private String net;
	private String sap;
	private String word;
	private String pdf;
	private String excel;
	private String image;
	private String as400makerid;
	private String as400makerpass;
	private String as400path;
	private String packagename;
	private String capturedevice;
	private String enablelog;
	private String forcescreenshot;
	private String closebrowser;
	private String fieldnotexist;
	private String importsheet;
	private String messagechecker;
	private String mobilelaunchmethod;
	private String pdftotextpath;
	private String reportpath;
	private String smspathfile;
	private String webtracelogfilename= ParamsObject.PARAM_LOG_FOLDER + "/WebSeleniumTraceLog";
	private Integer passed = 0;
	private Integer failed = 0;
	private Integer run = 0;
	private Integer total = 0;
	private String template_doc="Template.docx";//Template_NH2.docx||Template_APPFUXION.docx
	private String template_html="Appfuxion";//NH2||APPFUXION
	private String highlight_verify_xls="ON";//ON||OFF
	private String close_browser_after_run="NO";//YES||NO
	private String waitobject = "0";
	
	public String getWaitobject() {
		return waitobject;
	}
	public void setWaitobject(String waitobject) {
		this.waitobject = waitobject;
	}
	public String getClose_browser_after_run() {
		return close_browser_after_run;
	}
	public void setClose_browser_after_run(String close_browser_after_run) {
		this.close_browser_after_run = close_browser_after_run;
	}
	public String getHighlight_verify_xls() {
		return highlight_verify_xls;
	}
	public void setHighlight_verify_xls(String highlight_verify_xls) {
		this.highlight_verify_xls = highlight_verify_xls;
	}
	public String getString_datawritePath() {
		return String_datawritePath;
	}
	public void setString_datawritePath(String string_datawritePath) {
		String_datawritePath = string_datawritePath;
	}
	public String getTemplate_doc() {
		return template_doc;
	}
	public void setTemplate_doc(String template_doc) {
		this.template_doc = template_doc;
	}
	public String getTemplate_html() {
		return template_html;
	}
	public void setTemplate_html(String template_html) {
		this.template_html = template_html;
	}
	public String getString_templateDocPath() {
		return String_templateDocPath;
	}
	public void setString_templateDocPath(String string_templateDocPath) {
		String_templateDocPath = string_templateDocPath;
	}
	public Integer getPassed() {
		return passed;
	}
	public void setPassed(Integer passed) {
		this.passed = passed;
	}
	public Integer getFailed() {
		return failed;
	}
	public void setFailed(Integer failed) {
		this.failed = failed;
	}
	public Integer getRun() {
		return run;
	}
	public void setRun(Integer run) {
		this.run = run;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getString_currentLogPath() {
		return String_currentLogPath;
	}
	public void setString_currentLogPath(String string_currentLogPath) {
		String_currentLogPath = string_currentLogPath;
	}
	public String getString_configpath() {
		return String_configpath;
	}
	public void setString_configpath(String string_configpath) {
		String_configpath = string_configpath;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getDefaultpath() {
		return defaultpath;
	}
	public void setDefaultpath(String defaultpath) {
		this.defaultpath = defaultpath;
	}
	public String getRecursive() {
		return recursive;
	}
	public void setRecursive(String recursive) {
		this.recursive = recursive;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getWaittime() {
		return waittime;
	}
	public void setWaittime(String waittime) {
		this.waittime = waittime;
	}
	public String getSynctimeout() {
		return synctimeout;
	}
	public void setSynctimeout(String synctimeout) {
		this.synctimeout = synctimeout;
	}
	public String getOutputpath() {
		return outputpath;
	}
	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}
	public String getTracelog() {
		return tracelog;
	}
	public void setTracelog(String tracelog) {
		this.tracelog = tracelog;
	}
	public String getGetnow() {
		return getnow;
	}
	public void setGetnow(String getnow) {
		this.getnow = getnow;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getWin() {
		return win;
	}
	public void setWin(String win) {
		this.win = win;
	}
	public String getTe() {
		return te;
	}
	public void setTe(String te) {
		this.te = te;
	}
	public String getUia() {
		return uia;
	}
	public void setUia(String uia) {
		this.uia = uia;
	}
	public String getVb() {
		return vb;
	}
	public void setVb(String vb) {
		this.vb = vb;
	}
	public String getNet() {
		return net;
	}
	public void setNet(String net) {
		this.net = net;
	}
	public String getSap() {
		return sap;
	}
	public void setSap(String sap) {
		this.sap = sap;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public String getExcel() {
		return excel;
	}
	public void setExcel(String excel) {
		this.excel = excel;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAs400makerid() {
		return as400makerid;
	}
	public void setAs400makerid(String as400makerid) {
		this.as400makerid = as400makerid;
	}
	public String getAs400makerpass() {
		return as400makerpass;
	}
	public void setAs400makerpass(String as400makerpass) {
		this.as400makerpass = as400makerpass;
	}
	public String getAs400path() {
		return as400path;
	}
	public void setAs400path(String as400path) {
		this.as400path = as400path;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getCapturedevice() {
		return capturedevice;
	}
	public void setCapturedevice(String capturedevice) {
		this.capturedevice = capturedevice;
	}
	public String getEnablelog() {
		return enablelog;
	}
	public void setEnablelog(String enablelog) {
		this.enablelog = enablelog;
	}
	public String getForcescreenshot() {
		return forcescreenshot;
	}
	public void setForcescreenshot(String forcescreenshot) {
		this.forcescreenshot = forcescreenshot;
	}
	public String getClosebrowser() {
		return closebrowser;
	}
	public void setClosebrowser(String closebrowser) {
		this.closebrowser = closebrowser;
	}
	public String getFieldnotexist() {
		return fieldnotexist;
	}
	public void setFieldnotexist(String fieldnotexist) {
		this.fieldnotexist = fieldnotexist;
	}
	public String getImportsheet() {
		return importsheet;
	}
	public void setImportsheet(String importsheet) {
		this.importsheet = importsheet;
	}
	public String getMessagechecker() {
		return messagechecker;
	}
	public void setMessagechecker(String messagechecker) {
		this.messagechecker = messagechecker;
	}
	public String getMobilelaunchmethod() {
		return mobilelaunchmethod;
	}
	public void setMobilelaunchmethod(String mobilelaunchmethod) {
		this.mobilelaunchmethod = mobilelaunchmethod;
	}
	public String getPdftotextpath() {
		return pdftotextpath;
	}
	public void setPdftotextpath(String pdftotextpath) {
		this.pdftotextpath = pdftotextpath;
	}
	public String getReportpath() {
		return reportpath;
	}
	public void setReportpath(String reportpath) {
		this.reportpath = reportpath;
	}
	public String getSmspathfile() {
		return smspathfile;
	}
	public void setSmspathfile(String smspathfile) {
		this.smspathfile = smspathfile;
	}
	public String getWebtracelogfilename() {
		return webtracelogfilename;
	}
	public void setWebtracelogfilename(String webtracelogfilename) {
		this.webtracelogfilename = webtracelogfilename;
	}
	
}

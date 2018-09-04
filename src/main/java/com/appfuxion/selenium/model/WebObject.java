package com.appfuxion.selenium.model;

import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.appfuxion.selenium.view.Main;

public class WebObject {
	private WebDriver wd;
	private WebElement we;
	private By loc;
	private WebDriverWait wait;
	private Select sel;

	public Select getSel() {
		return sel;
	}

	public void setSel(Select sel) {
		this.sel = sel;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public void setWait(WebDriverWait wait) {
		this.wait = wait;
	}

	public WebDriver getWd() {
		return wd;
	}

	public void setWd(WebDriver wd) {
		this.wd = wd;
	}	

	public WebElement getWe() {
		return we;
	}

	public void setWe(WebElement we) {		
		this.we = we;
	}		

	public By getLoc() {
		return loc;
	}

	public void setLoc(By loc) {
		this.loc = loc;
	}
		
	public By setFieldLocator(FinalTestScriptObject fto){
		By cLoc = null;
		StringBuilder locatorValue = new StringBuilder();
		if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_ID) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_ID) + ParamsObject.PARAM_LOC_ID.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.id(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_NAME) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_NAME) + ParamsObject.PARAM_LOC_NAME.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.name(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_CSS) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_CSS) + ParamsObject.PARAM_LOC_CSS.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.cssSelector(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_LINKTEXT1) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_LINKTEXT1) + ParamsObject.PARAM_LOC_LINKTEXT1.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.linkText(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_LINKTEXT2) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_LINKTEXT2) + ParamsObject.PARAM_LOC_LINKTEXT2.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.linkText(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_PARTIALLINKTEXT) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_PARTIALLINKTEXT) + ParamsObject.PARAM_LOC_PARTIALLINKTEXT.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.partialLinkText(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_TAGNAME) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_TAGNAME) + ParamsObject.PARAM_LOC_TAGNAME.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.tagName(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_XPATH) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_XPATH) + ParamsObject.PARAM_LOC_XPATH.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.xpath(locatorValue.toString());
		}
		else if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_CLASS) > -1){
			int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_CLASS) + ParamsObject.PARAM_LOC_CLASS.length();
			int endIndex = fto.getObjectID().length();
			
			if(fto.getObjectID().indexOf(",") > -1){
				if(fto.getObjectID().indexOf(",",beginIndex) > -1){
					endIndex = fto.getObjectID().indexOf(",",beginIndex);
				}else fto.getObjectID().length();
			}
			locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
			cLoc = By.className(locatorValue.toString());
		}		
		return cLoc;
	}	
	
	public void setFieldElement(FinalTestScriptObject fto) throws NumberFormatException, InterruptedException{
		if(fto.getObjectType()!=null && fto.getObjectID()!=null && fto.getObjectType()!="" && fto.getObjectID()!=""){
			Thread.sleep( Long.valueOf(Main.configurationObject.getWaitobject()) * 1000 );
			this.loc = setFieldLocator(fto);			
			StringBuilder locatorValue = new StringBuilder();
			if(fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_IFRAME) > -1){
				int beginIndex = fto.getObjectID().indexOf(ParamsObject.PARAM_LOC_IFRAME) + ParamsObject.PARAM_LOC_IFRAME.length();
				int endIndex = fto.getObjectID().length();
				
				if(fto.getObjectID().indexOf(",") > -1){
					if(fto.getObjectID().indexOf(",",beginIndex) > -1){
						endIndex = fto.getObjectID().indexOf(",",beginIndex);
					}else fto.getObjectID().length();
				}
				
				this.wd.switchTo().defaultContent();
				locatorValue.append(fto.getObjectID().substring(beginIndex, endIndex));
				this.wd.switchTo().frame(locatorValue.toString());
				Thread.sleep( Long.valueOf(Main.configurationObject.getWaittime()) * 1000 );
			}else{
				this.wd.switchTo().defaultContent();
			}
			
			this.we = this.wd.findElement(this.loc);					
			
		}
	}
	
	public void clearValueElement(FinalTestScriptObject fto){
		if(fto.getAction()!=null && fto.getAction()!=""){
			if(fto.getAction().toLowerCase().equals(ParamsObject.PARAM_ACTION_SET)){
				wait.until(ExpectedConditions.visibilityOfElementLocated(this.loc));
				this.we.clear();
			}
		}
	}	
	
	public void setValueElement(FinalTestScriptObject fto) throws NumberFormatException, InterruptedException{

		if(fto.getAction()!=null && fto.getAction()!=""){
			if(fto.getAction().toLowerCase().equals(ParamsObject.PARAM_ACTION_CLICK)){	
				wait.until(ExpectedConditions.elementToBeClickable(this.we));				
				this.we.click();
			}else if(fto.getAction().toLowerCase().equals(ParamsObject.PARAM_ACTION_SET)){
				wait.until(ExpectedConditions.visibilityOfElementLocated(this.loc));
				this.we.sendKeys(fto.getTestData());
			}else if(fto.getAction().toLowerCase().equals(ParamsObject.PARAM_ACTION_SELECT)){
				wait.until(ExpectedConditions.visibilityOfElementLocated(this.loc));
				sel = new Select(this.we);
				sel.selectByVisibleText(fto.getTestData());
			}
		}
		
		
	}
	
	public String getValueFromDropDown(WebElement element, String compareText) {
        List<WebElement> options = new Select(element).getAllSelectedOptions(); 
	    for (WebElement option : options){
	        if (option.getText().equals(compareText)){
	            return option.getText();
	        }
	    }
	    return null;
	}
	
	public String getValueElement(FinalTestScriptObject fto){
		String value = "";
		
		if(fto.getObjectType().toLowerCase().trim().equals(ParamsObject.OT_CONSTANT_WEBEDIT)){
			value = we.getText();
			if(value==null || value.equals("")) 
				value = this.we.getAttribute(ParamsObject.AT_CONSTANT_VALUE);			
		}
		else if(fto.getObjectType().toLowerCase().trim().equals(ParamsObject.OT_CONSTANT_WEBLIST)){
			sel = new Select(this.we);
			WebElement option = sel.getFirstSelectedOption();
			value = option.getText();
		}
		else if(fto.getObjectType().toLowerCase().trim().equals(ParamsObject.OT_CONSTANT_WEBCHECKBOX)){
			value = we.isSelected() == true ? ParamsObject.OT_CONSTANT_WEBEHCKBOX_ON : ParamsObject.OT_CONSTANT_WEBEHCKBOX_OFF;
		}
		else if(fto.getObjectType().toLowerCase().trim().equals(ParamsObject.OT_CONSTANT_WEBRADIOGROUP)){
			value = we.isSelected() == true ? ParamsObject.OT_CONSTANT_WEBEHCKBOX_ON : ParamsObject.OT_CONSTANT_WEBEHCKBOX_OFF;
		}else{
			value = we.getText();
			if(value==null || value.equals("")) 
				value = this.we.getAttribute(ParamsObject.AT_CONSTANT_VALUE);						
		}
		
		return value;
	}
	
	private static Function<WebDriver,WebElement> presenceOfElementLocated(final By locator) {
	    return new Function<WebDriver, WebElement>() {
	        @Override
	        public WebElement apply(WebDriver driver) {
	            return driver.findElement(locator);
	        }
	    };
	}
}

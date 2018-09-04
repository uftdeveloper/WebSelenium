package com.appfuxion.selenium.controller;

import com.appfuxion.selenium.model.MessageObject;
import com.appfuxion.selenium.model.ParamsObject;

public class About {
	public About(String[] args){
		if(args!=null && args.length > 0 && args[0].toLowerCase().equals(ParamsObject.PARAM_ABOUT)){
			System.out.println(MessageObject.MSG0019);		
			System.exit(0);
		}
	}
}

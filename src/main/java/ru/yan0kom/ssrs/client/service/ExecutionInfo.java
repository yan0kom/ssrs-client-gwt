package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class ExecutionInfo extends JavaScriptObject {
	  protected ExecutionInfo() {
	  }
	  
	  public final native String getExecutionId() /*-{ return this.executionID; }-*/;
	  public final native Parameter[] getParameters() /*-{ return this.parameters ? this.parameters.reportParameter : null; }-*/;
}

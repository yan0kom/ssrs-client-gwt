package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class RdlExecutionInfo extends JavaScriptObject {
	  protected RdlExecutionInfo() {
	  }
	  
	  public final native String getExecutionId() /*-{ return this.executionID; }-*/;
	  public final native RdlParameter[] getParameters() /*-{ return this.parameters ? this.parameters.reportParameter : null; }-*/;
}

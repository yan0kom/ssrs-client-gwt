package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class RdlValidValue extends JavaScriptObject {
	  protected RdlValidValue() {
	  }
	  
	  public final native String getLabel() /*-{ return this.label; }-*/;
	  public final native String getValue() /*-{ return this.value; }-*/;
}

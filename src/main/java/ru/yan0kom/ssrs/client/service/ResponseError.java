package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class ResponseError extends JavaScriptObject {
	  protected ResponseError() {
	  }
	  
	  public final native String getMessage() /*-{ return this.message; }-*/;
	  public final native String getException() /*-{ return this.exception; }-*/;
}

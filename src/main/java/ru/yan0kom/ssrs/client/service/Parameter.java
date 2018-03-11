package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.JavaScriptObject;

public class Parameter extends JavaScriptObject {
	  protected Parameter() {
	  }
	  
	  public final native String getName() /*-{ return this.name; }-*/;
	  public final native String getType() /*-{ return this.type; }-*/;
	  public final native String getPrompt() /*-{ return this.prompt; }-*/;
	  public final native boolean isValidValuesQueryBased() /*-{ return this.validValuesQueryBased; }-*/;
	  public final native boolean isMultiValue() /*-{ return this.multiValue; }-*/;
	  public final native ValidValue[] getValidValues() /*-{ return this.validValues ? this.validValues.validValue : null; }-*/;
	  public final native String[] getDefaultValues() /*-{ return this.defaultValues ? this.defaultValues.value : null; }-*/;
}

package ru.yan0kom.ssrs.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class ParamLabel extends Composite {
	public ParamLabel(String text) {
		Label container = new Label(text);
		initWidget(container);
		setStyleName("param-label", true);		
	}
	
	public void setEnabled(boolean enable) {
		setStyleName("param-label-disabled", !enable);		
	}
}

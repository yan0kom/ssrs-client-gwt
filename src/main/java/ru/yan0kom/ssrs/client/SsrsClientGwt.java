package ru.yan0kom.ssrs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SsrsClientGwt implements EntryPoint {

	@Override
	public void onModuleLoad() {
		TabbedReport report = new TabbedReport();
		
		String repName = Window.Location.getQueryString();
		if (repName.startsWith("?")) {
			repName = repName.substring(1);
		}
		if (repName.endsWith("&")) {
			repName = repName.substring(0, repName.length()-1);
		}
		
		report.addTab(repName, repName);
		report.selectTab(0);
		RootPanel.get().add(report);
	}
	
	public static native void print(String text)
	/*-{
	    console.log(text);
	}-*/;	
}

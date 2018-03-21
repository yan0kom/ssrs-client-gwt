package ru.yan0kom.ssrs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import ru.yan0kom.ssrs.client.ui.ReportViewer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SsrsClientGwt implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		String reportPath = Window.Location.getQueryString();
		if (reportPath.startsWith("?")) {
			reportPath = reportPath.substring(1);
		}
		if (reportPath.endsWith("&")) {
			reportPath = reportPath.substring(0, reportPath.length()-1);
		}
		
		if (reportPath == null || reportPath.isEmpty()) {
			RootLayoutPanel.get().add(new Label("Usage: index.html?/path/to/report"));
			return;
		}
		
		ReportViewer.openReportLink(reportPath, null);				
	}
	
	public static native void print(String text)
	/*-{
	    console.log(text);
	}-*/;

}

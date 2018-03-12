package ru.yan0kom.ssrs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import ru.yan0kom.ssrs.client.gwt.MultiTabReport;
import ru.yan0kom.ssrs.client.gwt.ReportViewer;
import ru.yan0kom.ssrs.client.service.ReportExt;
import ru.yan0kom.ssrs.client.service.ReportService;
import ru.yan0kom.ssrs.client.service.ReportServiceGetExtCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SsrsClientGwt implements EntryPoint, ReportServiceGetExtCallback {

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
			RootPanel.get().add(new Label("Usage: index.html?/path/to/report"));
			return;
		}
		
		ReportService.getExt(reportPath, this);
	}
	
	public static native void print(String text)
	/*-{
	    console.log(text);
	}-*/;

	@Override
	public void onError(Response response, Throwable exception) {
		RootPanel.get().add(new Label(makeErrorMessage(response, exception)));		
	}

	@Override
	public void onGetExt(ReportExt ext) {
		if (ext.getTabs() == null) {
			ReportViewer report = new ReportViewer(ext.getPath());
			report.load();
			RootPanel.get().add(report);			
		}else {
			MultiTabReport mtReport = new MultiTabReport();
			for (ReportExt.Tab tab : ext.getTabs()) {
				mtReport.addTab(tab.getName(), tab.getPath());
			}
			if (ext.getTabs().size() > 0) {
				mtReport.selectTab(0);
			}
			RootPanel.get().add(mtReport);
		}
	}
	
	public static String makeErrorMessage(Response response, Throwable exception) {
		if (exception != null) {
			return exception.getMessage();
		}else if (response != null) {
			return response.getStatusText();
		}
		return "Unknown error";		
	}
}

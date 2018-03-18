package ru.yan0kom.ssrs.client.gwt;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import jsinterop.annotations.JsMethod;
import ru.yan0kom.ssrs.client.SsrsClientGwt;
import ru.yan0kom.ssrs.client.service.ReportExt;
import ru.yan0kom.ssrs.client.service.ReportService;
import ru.yan0kom.ssrs.client.service.ReportServiceGetExtCallback;

public class ReportViewer implements ReportServiceGetExtCallback {
	private static ReportViewer instance;
	
	public static void init() {
		if (instance == null) {
			instance = new ReportViewer();			
		}
	}

	private ReportViewer() {
	}
	
	@Override
	public void onError(Response response, Throwable exception) {
		RootPanel.get().add(new Label(makeErrorMessage(response, exception)));		
	}

	@Override
	public void onGetExt(ReportExt ext) {
		RootPanel.get().clear();
		
		if (ext.getTabs() == null) {
			ReportView report = new ReportView(ext.getPath());
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

	@JsMethod(namespace = "ReportViewer")
	public static void openReportLink(String linkPath, String linkParams) {
		init();
		ReportService.getExt(linkPath, instance);
		SsrsClientGwt.print(linkPath+"?"+linkParams);
	}
	
}

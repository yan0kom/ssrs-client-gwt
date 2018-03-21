package ru.yan0kom.ssrs.client.ui;

import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import jsinterop.annotations.JsMethod;
import ru.yan0kom.ssrs.client.SsrsClientGwt;
import ru.yan0kom.ssrs.client.bean.Parameters;
import ru.yan0kom.ssrs.client.bean.ReportExt;
import ru.yan0kom.ssrs.client.bean.ServiceBeanFactory;
import ru.yan0kom.ssrs.client.service.ReportService;
import ru.yan0kom.ssrs.client.service.ReportServiceGetExtCallback;

public class ReportViewer implements ReportServiceGetExtCallback {
	private static ReportViewer instance;
	private Stack<Composite> reportStack;
	
	public static void init() {
		if (instance == null) {
			instance = new ReportViewer();			
		}
	}
	
	public static ReportViewer getInstance() {
		return instance;
	}

	private ReportViewer() {
		reportStack = new Stack<>();
	}
	
	@Override
	public void onError(Response response, Throwable exception) {
		RootLayoutPanel.get().add(new Label(makeErrorMessage(response, exception)));		
	}

	@Override
	public void onGetExt(ReportExt ext) {
		RootLayoutPanel.get().clear();
		
		if (ext.getTabs() == null) {
			ReportView report = new ReportView(ext);
			reportStack.push(report);
			RootLayoutPanel.get().add(report);
			report.load();
		}else {
			MultiTabReport mtReport = new MultiTabReport(ext);
			reportStack.push(mtReport);
			RootLayoutPanel.get().add(mtReport);
			if (ext.getTabs().size() > 0) {
				mtReport.selectTab(0);
			}
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
		ServiceBeanFactory factory = GWT.create(ServiceBeanFactory.class);
		String json;
		if (linkParams != null) {
			json = "{\"parameters\":[{\"name\":\""+linkParams.replaceAll("=", "\", \"value\":\"").replaceAll("&", "\"},{\"name\":\"")+"\"}]}";
		}else {
			json = "{\"parameters\":[]}";
		}
		AutoBean<Parameters> bean = AutoBeanCodex.decode(factory, Parameters.class, json);
		ReportService.getExt(linkPath, bean.as(), instance);

		SsrsClientGwt.print(linkPath+", "+AutoBeanCodex.encode(bean).getPayload());		
	}
	
	public boolean isShowReturn() {
		return reportStack.size() > 0;
	}

	public void goFirst() {
		RootLayoutPanel.get().clear();
		reportStack.setSize(1);
		RootLayoutPanel.get().add(reportStack.peek());
	}

	public boolean isShowBack() {
		return reportStack.size() > 1;
	}

	public void goBack() {
		RootLayoutPanel.get().clear();
		reportStack.pop();
		RootLayoutPanel.get().add(reportStack.peek());
	}
	
}

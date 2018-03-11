package ru.yan0kom.ssrs.client.service;

public interface ReportServiceRenderCallback extends ReportServiceErrorCallback {
	public void onRender(String html);
}

package ru.yan0kom.ssrs.client.service;

public interface ReportServiceLoadCallback extends ReportServiceErrorCallback {
	public void onLoad(ExecutionInfo info);
}

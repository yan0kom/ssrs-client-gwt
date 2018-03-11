package ru.yan0kom.ssrs.client.service;

import com.google.gwt.http.client.Response;

public interface ReportServiceErrorCallback {
	public void onError(Response response, Throwable exception);
}

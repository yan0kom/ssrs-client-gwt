package ru.yan0kom.ssrs.client.service;

import ru.yan0kom.ssrs.client.bean.ReportExt;

public interface ReportServiceGetExtCallback extends ReportServiceErrorCallback {
	public void onGetExt(ReportExt ext);
}

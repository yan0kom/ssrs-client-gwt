package ru.yan0kom.ssrs.client.service;

import java.util.List;

public interface ReportExt {
	interface Tab {
		String getName();
		void setName(String name); 
		String getPath();
		void setPath(String path); 
	}

	String getPath();
	void setPath(String path); 
	
	List<Tab> getTabs();
	void setTabs(List<Tab> tabs);
}

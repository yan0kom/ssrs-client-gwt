package ru.yan0kom.ssrs.client;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;

public class TabbedReport extends TabPanel  {

	public TabbedReport() {
		setWidth("100%");
		
		addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				ReportTab tab = (ReportTab) getWidget(event.getSelectedItem());
				tab.load();
			}
		});
	}
	
	public void addTab(String name, String reportPath) {
		add(new ReportTab(reportPath), name);
	}

}

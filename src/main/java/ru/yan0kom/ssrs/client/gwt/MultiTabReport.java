package ru.yan0kom.ssrs.client.gwt;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;

public class MultiTabReport extends TabPanel  {

	public MultiTabReport() {
		setStyleName("multi-tab-report", true);
		
		addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				ReportViewer tab = (ReportViewer) getWidget(event.getSelectedItem());
				tab.load();
			}
		});
	}
	
	public void addTab(String name, String reportPath) {
		add(new ReportViewer(reportPath), name);
	}

}

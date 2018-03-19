package ru.yan0kom.ssrs.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import ru.yan0kom.ssrs.client.bean.ReportExt;
import ru.yan0kom.ssrs.client.bean.ServiceBeanFactory;

public class MultiTabReport extends Composite  {
	private TabPanel container;

	public MultiTabReport(ReportExt ext) {
		container = new TabPanel();
				
		for (ReportExt.Tab tab : ext.getTabs()) {
			addTab(tab.getName(), tab.getPath());
		}
		
		container.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				ReportView tab = (ReportView) container.getWidget(event.getSelectedItem());
				tab.load();
			}
		});
		
		initWidget(container);
		container.setStyleName("multi-tab-report", true);
	}
	
	public void addTab(String name, String reportPath) {
		ServiceBeanFactory factory = GWT.create(ServiceBeanFactory.class);
		ReportExt ext = factory.reportExt().as();
		ext.setPath(reportPath);
		container.add(new ReportView(ext), name);
	}
	
	public void selectTab(int idx) {
		container.selectTab(idx);
	}

}

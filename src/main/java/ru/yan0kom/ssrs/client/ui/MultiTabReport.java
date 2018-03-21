package ru.yan0kom.ssrs.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import ru.yan0kom.ssrs.client.bean.ReportExt;
import ru.yan0kom.ssrs.client.bean.ServiceBeanFactory;

public class MultiTabReport extends Composite  {
	private TabLayoutPanel container;

	public MultiTabReport(ReportExt ext) {
		container = new TabLayoutPanel(30, Unit.PX);
				
		for (ReportExt.Tab tab : ext.getTabs()) {
			addTab(tab.getName(), tab.getPath());
		}
		
		container.addSelectionHandler((SelectionEvent<Integer> event) -> {
			ReportView tab = (ReportView) container.getWidget(event.getSelectedItem());
			tab.load();
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
		container.selectTab(idx, false);
		ReportView tab = (ReportView) container.getWidget(idx);
		tab.load();
	}

}

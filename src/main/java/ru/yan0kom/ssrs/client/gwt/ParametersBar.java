package ru.yan0kom.ssrs.client.gwt;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import ru.yan0kom.ssrs.client.service.ReportParameters;
import ru.yan0kom.ssrs.client.service.ReportParameters.ReportParameter;

public class ParametersBar extends Composite {
	private HorizontalPanel panel;
	private Grid grid;
	private Consumer<ReportParameter> cnangeCallback; 
	
	public ParametersBar(Consumer<ReportParameter> cnangeCallback) {
		this.cnangeCallback = cnangeCallback;
		panel = new HorizontalPanel();
		panel.setStyleName("gwt-MenuBar-horizontal", true);
		panel.setStyleName("parameters-bar", true);

		grid = new Grid();
		panel.add(grid);
		initWidget(panel);
	}
	
	public void build(ReportParameters repParams) {
		panel.setVisible(false);
		
		if (repParams.getCount() == 0) {
			grid.resize(0, 0);
			return;
		}
		
		grid.resize(repParams.getCount(), 2);		
		for (int i = 0; i < repParams.getCount(); ++i) {
			ReportParameter repParam = repParams.get(i);

			Label paramLabel = new Label(repParam.getLabel());
			paramLabel.setWordWrap(false);
			paramLabel.getElement().getStyle().setMarginRight(5, Unit.PX);
			grid.setWidget(i, 0, paramLabel);
			
			if (repParam.isMultiValue()) {
				if (repParam.getValidValuesCount() <= 5) {
					grid.setWidget(i, 1, new MultiSelectParam(repParam, cnangeCallback));
				}else {
					grid.setWidget(i, 1, new SpoilerMultiSelectParam(repParam, cnangeCallback));
				}
			}else {
				grid.setWidget(i, 1, new SingleSelectParam(repParam, cnangeCallback));
			}
		}
		
		panel.setVisible(true);
	}
}

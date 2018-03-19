package ru.yan0kom.ssrs.client.ui;

import java.util.function.Consumer;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import ru.yan0kom.ssrs.client.service.ParametersDefinition;
import ru.yan0kom.ssrs.client.service.ParametersDefinition.ParameterDefinition;

public class ParametersBar extends Composite {
	private HorizontalPanel panel;
	private Grid grid;
	private Consumer<ParameterDefinition> cnangeCallback; 
	private boolean enabled;
	
	public ParametersBar(Consumer<ParameterDefinition> cnangeCallback) {
		this.cnangeCallback = cnangeCallback;
		enabled = true;
		
		panel = new HorizontalPanel();
		panel.setStyleName("gwt-MenuBar-horizontal", true);
		
		grid = new Grid();
		panel.add(grid);
		initWidget(panel);
		setStyleName("parameters-bar", true);
	}
	
	public void build(ParametersDefinition repParams) {
		panel.setVisible(false);
		
		if (repParams.getCount() == 0) {
			grid.resize(0, 0);
			return;
		}
		
		grid.resize(repParams.getVisibleCount(), 2);		
		for (int i = 0; i < repParams.getCount(); ++i) {
			ParameterDefinition repParam = repParams.get(i);
			if (repParam.isHidden()) {
				continue;
			}
			
			ParamLabel paramLabel = new ParamLabel(repParam.getLabel());			
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
		
		setEnabled(enabled);
		panel.setVisible(true);
	}
	
	public void setEnabled(boolean enable) {
		enabled = enable;
		for (int i = 0; i < grid.getRowCount(); ++i) {
			for (int j = 0; j < grid.getColumnCount(); ++j) {
				Widget widget = grid.getWidget(i, j);
				if (widget instanceof ParamLabel) {
					((ParamLabel)widget).setEnabled(enable);
				}else if (widget instanceof ParamInput){
					((ParamInput)widget).setEnabled(enable);
				}
			}
		}
	}
}

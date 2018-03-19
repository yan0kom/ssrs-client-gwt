package ru.yan0kom.ssrs.client.ui;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

import ru.yan0kom.ssrs.client.service.ParametersDefinition.ParameterDefinition;

public class MultiSelectParam extends ParamInput {
	private FlowPanel container;
	
	public MultiSelectParam(ParameterDefinition repParam, Consumer<ParameterDefinition> changeCallback) {
		container = new FlowPanel();
		
		ValueChangeHandler<Boolean> handler = (ValueChangeEvent<Boolean> event) -> {
			CheckBox source = (CheckBox) event.getSource();
			if (event.getValue() == Boolean.TRUE) {
				repParam.setValueByLabel(source.getText());
			}else {
				repParam.unsetValueByLabel(source.getText());
			}
			if (changeCallback != null) {
				changeCallback.accept(repParam);
			}				
		};
		
		for (int j = 0; j < repParam.getValidValuesCount(); ++j) {
			CheckBox valCheckbox = new CheckBox(repParam.getValidLabel(j));
			valCheckbox.getElement().getStyle().setMarginRight(5, Unit.PX);
			valCheckbox.setValue(repParam.isValidValueSelected(j));
			valCheckbox.addValueChangeHandler(handler);						
			container.add(valCheckbox);
		}

		initWidget(container);
	}

	@Override
	public void setEnabled(boolean enable) {
		for (int i = 0; i < container.getWidgetCount(); ++i) {
			((CheckBox) container.getWidget(i)).setEnabled(enable);
		}
	}
}

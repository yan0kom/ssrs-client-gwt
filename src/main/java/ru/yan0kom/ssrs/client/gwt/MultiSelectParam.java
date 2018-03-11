package ru.yan0kom.ssrs.client.gwt;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import ru.yan0kom.ssrs.client.service.ReportParameters.ReportParameter;

public class MultiSelectParam extends Composite {

	public MultiSelectParam(ReportParameter repParam, Consumer<ReportParameter> changeCallback) {
		FlowPanel valuesContainer = new FlowPanel();
		
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
			valuesContainer.add(valCheckbox);
		}

		initWidget(valuesContainer);
	}
}

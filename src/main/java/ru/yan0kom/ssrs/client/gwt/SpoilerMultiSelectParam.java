package ru.yan0kom.ssrs.client.gwt;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.yan0kom.ssrs.client.service.ReportParameters.ReportParameter;

public class SpoilerMultiSelectParam extends Composite {
	private boolean valuesChanged;

	public SpoilerMultiSelectParam(ReportParameter repParam, Consumer<ReportParameter> changeCallback) {
		valuesChanged = false;
		
		//handler for checkbox with value (except "All")
		ValueChangeHandler<Boolean> checkboxHandler = (ValueChangeEvent<Boolean> event) -> {
			CheckBox source = (CheckBox) event.getSource();
			repParam.setValueByLabel(source.getText(), event.getValue());
			valuesChanged = true;
		};

		HorizontalPanel panel = new HorizontalPanel();
		int allIdx = repParam.getIndexOfAll();
		if (allIdx != -1) {
			CheckBox allCheckbox = new CheckBox(repParam.getValidLabel(allIdx));
			allCheckbox.addValueChangeHandler((ValueChangeEvent<Boolean> event) -> {
				CheckBox source = (CheckBox) event.getSource();
				repParam.setValueByLabel(source.getText(), event.getValue());
				if (changeCallback != null) {
					changeCallback.accept(repParam);
				}
			});
			panel.add(allCheckbox);
		}
		
		DisclosurePanel dis = new DisclosurePanel("Выбрать");
		dis.addCloseHandler((event) -> {
			if (valuesChanged && changeCallback != null) {
				changeCallback.accept(repParam);
			}
			valuesChanged = false;
		});
		
		VerticalPanel checkList = new VerticalPanel();		
		for (int j = 0; j < repParam.getValidValuesCount(); ++j) {
			if (j == allIdx) {
				continue;
			}
			CheckBox valCheckbox = new CheckBox(repParam.getValidLabel(j));
			valCheckbox.getElement().getStyle().setMarginRight(5, Unit.PX);
			valCheckbox.setValue(repParam.isValidValueSelected(j));
			valCheckbox.addValueChangeHandler(checkboxHandler);						
			checkList.add(valCheckbox);
		}

		dis.setContent(checkList);
		panel.add(dis);
		initWidget(panel);
	}
}

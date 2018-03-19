package ru.yan0kom.ssrs.client.ui;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.yan0kom.ssrs.client.service.ParametersDefinition.ParameterDefinition;

public class SpoilerMultiSelectParam extends ParamInput {
	private boolean valuesChanged;
	private boolean enabled;
	private CheckBox allCheckbox;
	private DisclosurePanel disclosurePanel;

	public SpoilerMultiSelectParam(ParameterDefinition repParam, Consumer<ParameterDefinition> changeCallback) {
		valuesChanged = false;
		enabled = true;
		
		//handler for checkbox with value (except "All")
		ValueChangeHandler<Boolean> checkboxHandler = (ValueChangeEvent<Boolean> event) -> {
			CheckBox source = (CheckBox) event.getSource();
			repParam.setValueByLabel(source.getText(), event.getValue());
			valuesChanged = true;
		};

		HorizontalPanel panel = new HorizontalPanel();
		int allIdx = repParam.getIndexOfAll();
		if (allIdx != -1) {
			allCheckbox = new CheckBox(repParam.getValidLabel(allIdx));
			allCheckbox.setValue(repParam.isValidValueSelected(allIdx));			
			allCheckbox.addValueChangeHandler((ValueChangeEvent<Boolean> event) -> {
				CheckBox source = (CheckBox) event.getSource();
				repParam.setValueByLabel(source.getText(), event.getValue());
				if (changeCallback != null) {
					changeCallback.accept(repParam);
				}
			});
			panel.add(allCheckbox);
		}
		
		disclosurePanel = new DisclosurePanel("Выбрать");		
		disclosurePanel.addCloseHandler((event) -> {
			if (valuesChanged && changeCallback != null) {
				changeCallback.accept(repParam);
			}
			valuesChanged = false;
		});
		disclosurePanel.addOpenHandler((event) -> {
			if (!enabled) {
				disclosurePanel.setOpen(false);
			}
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

		disclosurePanel.setContent(checkList);
		panel.add(disclosurePanel);
		initWidget(panel);
	}

	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
		if (allCheckbox != null) {
			allCheckbox.setEnabled(enable);
		}		
		if (!enabled) {
			disclosurePanel.setOpen(false);
		}
		disclosurePanel.setStyleName("gwt-DisclosurePanel-disabled", !enable);
	}
}

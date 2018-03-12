package ru.yan0kom.ssrs.client.gwt;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

import ru.yan0kom.ssrs.client.service.ReportParameters.ReportParameter;

public class SingleSelectParam extends Composite {

	public SingleSelectParam(ReportParameter repParam, Consumer<ReportParameter> changeCallback) {
		HorizontalPanel panel = new HorizontalPanel();
		int allIdx = repParam.getIndexOfAll();
		
		ListBox choice = new ListBox();
		for (int j = 0; j < repParam.getValidValuesCount(); ++j) {
			if (j == allIdx) {
				continue;
			}			
			choice.addItem(repParam.getValidLabel(j), repParam.getValidValue(j));
			choice.setItemSelected(choice.getItemCount()-1, repParam.isValidValueSelected(j));						
		}
		choice.addChangeHandler((event) -> {
			repParam.setValue(choice.getSelectedValue());
			if (changeCallback != null) {
				changeCallback.accept(repParam);
			}					
		});		
		
		if (allIdx != -1) {
			CheckBox allCheckbox = new CheckBox(repParam.getValidLabel(allIdx));
			allCheckbox.getElement().getStyle().setMarginRight(5, Unit.PX);			
			
			allCheckbox.setValue(repParam.isValidValueSelected(allIdx));
			choice.setEnabled(!allCheckbox.getValue());
			
			allCheckbox.addValueChangeHandler((ValueChangeEvent<Boolean> event) -> {
				CheckBox source = (CheckBox) event.getSource();
				
				choice.setEnabled(!event.getValue());
				if (event.getValue()) {
					repParam.setValueByLabel(source.getText());
				}else {
					repParam.setValue(choice.getSelectedValue());
				}
								
				if (changeCallback != null) {
					changeCallback.accept(repParam);
				}
			});
			panel.add(allCheckbox);
		}
				
		panel.add(choice);		
		initWidget(panel);
	}
}

package ru.yan0kom.ssrs.client.gwt;

import java.util.function.Consumer;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

import ru.yan0kom.ssrs.client.service.ReportParameters.ReportParameter;

public class SingleSelectParam extends Composite {

	public SingleSelectParam(ReportParameter repParam, Consumer<ReportParameter> changeCallback) {
		ListBox choice = new ListBox();
		for (int j = 0; j < repParam.getValidValuesCount(); ++j) {
			choice.addItem(repParam.getValidLabel(j), repParam.getValidValue(j));
			choice.setItemSelected(choice.getItemCount()-1, repParam.isValidValueSelected(j));						
		}
		choice.addChangeHandler((event) -> {
			repParam.setValue(choice.getSelectedValue());
			if (changeCallback != null) {
				changeCallback.accept(repParam);
			}					
		});
		initWidget(choice);
	}
}

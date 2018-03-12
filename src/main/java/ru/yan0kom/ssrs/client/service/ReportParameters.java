package ru.yan0kom.ssrs.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ReportParameters {
	
	public class Tuple {
		public String first;
		public String second;
		public Tuple(String first, String second) {
			this.first = first;
			this.second = second;
		}
	}
	
	public static class ReportParameter {
		private String name;
		private String label;
		private String type;
		private boolean multiValue;
		private List<String> values;
		private List<String> validValues;
		private List<String> validLabels;
		
		public ReportParameter(Parameter sourceParam) {
			this();
			name = sourceParam.getName();
			label = sourceParam.getPrompt();
			setType(sourceParam.getType());
			multiValue = sourceParam.isMultiValue();			
			
			if (sourceParam.getValidValues() != null) {
				for (ValidValue sourceValidValue : sourceParam.getValidValues()) {
					validLabels.add(sourceValidValue.getLabel());
					validValues.add(sourceValidValue.getValue());
				}
			}
			
			if (sourceParam.getDefaultValues() != null) {
				values = new LinkedList<>(Arrays.asList(sourceParam.getDefaultValues()));
			}else {
				values = new LinkedList<>();
			}
		}
		
		public ReportParameter() {
			validValues = new ArrayList<>();
			validLabels = new ArrayList<>();
			values = new LinkedList<>();
			type = "String";			
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}

		public boolean isMultiValue() {
			return multiValue;
		}
		public void setMultiValue(boolean multiValue) {
			this.multiValue = multiValue;
		}
		
		public void addValidValue(String label, String value) {
			validLabels.add(label);
			validValues.add(value);
		}
		
		public boolean hasValidValues() {
			return validValues.size() > 0;
		}
		
		/** Returns index of special "All" value
		 * for string it is '*', for int is 0
		 */
		public int getIndexOfAll() {
			switch (type) {
				case "STRING":
					return validValues.indexOf("*");
				case "INTEGER":
					return validValues.indexOf("0");
			}
			return -1;
		}
		
		public int getValidValuesCount() {
			return validValues.size();
		}

		public String getValidLabel(int index) {
			return validLabels.get(index);
		}

		public String getValidValue(int index) {
			return validValues.get(index);
		}
		
		public boolean isValidValueSelected(int index) {
			return values.contains(validValues.get(index));
		}
		
		public void setValue(String value, boolean isSet) {
			if (isSet) {
				setValue(value);
			}else {
				unsetValue(value);
			}
		}
		
		public void setValue(String value) {
			if (multiValue) {
				if (!values.contains(value)) {
					values.add(value);
				}
			}else {
				values.clear();
				values.add(value);
			}
		}

		public void unsetValue(String value) {
			if (multiValue) {
				values.remove(value);
			}else {
				values.clear();
			}
		}
		
		public void setValueByLabel(String label, boolean isSet) {
			setValue(validValues.get(validLabels.indexOf(label)), isSet);
		}
		
		public void setValueByLabel(String label) {
			setValue(validValues.get(validLabels.indexOf(label)));
		}

		public void unsetValueByLabel(String label) {
			unsetValue(validValues.get(validLabels.indexOf(label)));
		}
		
		public List<String> getValues() {
			return values;
		}
		
	}
	
	private List<ReportParameter> params;
	
	public ReportParameters(Parameter[] sourceParams) {
		params = new ArrayList<>();
		if (sourceParams != null) {
			for (Parameter sourceParam : sourceParams) {
				params.add(new ReportParameter(sourceParam));
			}
		}
	}
	
	public ReportParameters() {
		this(null);
	}

	public int getCount() {
		return params.size();
	}
	
	public void add(ReportParameter param) {
		params.add(param);
	}
	
	public ReportParameter get(int index) {
		return params.get(index);
	}

	public void set(int index, String value) {
		params.get(index).setValue(value);
	}

	public void setByLabel(int index, String label) {
		params.get(index).setValueByLabel(label);
	}

	public void unset(int index, String value) {
		params.get(index).unsetValue(value);
	}
	
	public void unsetByLabel(int index, String label) {
		params.get(index).unsetValueByLabel(label);
	}	
	
	public List<Tuple> getAllValues() {
		List<Tuple> all = new LinkedList<>();
		for (ReportParameter param : params) {
			for (String val : param.getValues()) {
				all.add(new Tuple(param.getName(), val));				
			}
		}
		return all;
	}

	public List<ParameterizeRequest.Parameter> getAllParamValues() {
		ServiceBeanFactory factory = GWT.create(ServiceBeanFactory.class);
		List<ParameterizeRequest.Parameter> rrpList = new LinkedList<>(); 
		
		for (ReportParameter param : params) {
			for (String val : param.getValues()) {
				AutoBean<ParameterizeRequest.Parameter> rrp = factory.parameter();
				rrp.as().setName(param.getName());
				rrp.as().setValue(val);
				rrpList.add(rrp.as());
			}
		}
		return rrpList;
	}
	
}

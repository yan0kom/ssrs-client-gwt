package ru.yan0kom.ssrs.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;

import ru.yan0kom.ssrs.client.bean.Parameter;
import ru.yan0kom.ssrs.client.bean.ServiceBeanFactory;

public class ParametersDefinition {
	
	public static class ParameterDefinition {
		private String name;
		private String label;
		private String type;
		private boolean multiValue;
		private List<String> values;
		private List<String> validValues;
		private List<String> validLabels;
		
		public ParameterDefinition(RdlParameter sourceParam) {
			this();
			name = sourceParam.getName();
			label = sourceParam.getPrompt();
			setType(sourceParam.getType());
			multiValue = sourceParam.isMultiValue();			
			
			if (sourceParam.getValidValues() != null) {
				for (RdlValidValue sourceValidValue : sourceParam.getValidValues()) {
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
		
		public ParameterDefinition() {
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
		
		public boolean isHidden() {
			return (label == null || label.isEmpty());
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
	
	private List<ParameterDefinition> paramList;
	private HashMap<String, ParameterDefinition> paramMap;
	
	public ParametersDefinition(RdlParameter[] sourceParams) {
		paramList = new ArrayList<>();
		paramMap = new HashMap<>();
		if (sourceParams != null) {
			for (RdlParameter sourceParam : sourceParams) {
				ParameterDefinition pd = new ParameterDefinition(sourceParam);
				paramList.add(pd);
				paramMap.put(sourceParam.getName(), pd);
			}
		}
	}
	
	public ParametersDefinition() {
		this(null);
	}

	public int getCount() {
		return paramList.size();
	}
	
	/** returns visible parameters count */
	public int getVisibleCount() {
		int cnt = 0;
		for (ParameterDefinition param : paramList) {
			if (!param.isHidden()) {
				++cnt;
			}
		}
		return cnt;
	}	
	
	public void add(ParameterDefinition param) {
		paramList.add(param);
	}
	
	public ParameterDefinition get(int index) {
		return paramList.get(index);
	}

	public void set(int index, String value) {
		paramList.get(index).setValue(value);
	}

	public void setByLabel(int index, String label) {
		paramList.get(index).setValueByLabel(label);
	}

	public void unset(int index, String value) {
		paramList.get(index).unsetValue(value);
	}
	
	public void unsetByLabel(int index, String label) {
		paramList.get(index).unsetValueByLabel(label);
	}	
	
	public List<Parameter> getAllParamValues() {
		ServiceBeanFactory factory = GWT.create(ServiceBeanFactory.class);
		List<Parameter> rrpList = new LinkedList<>(); 
		
		for (ParameterDefinition param : paramList) {
			for (String val : param.getValues()) {
				AutoBean<Parameter> rrp = factory.parameter();
				rrp.as().setName(param.getName());
				rrp.as().setValue(val);
				rrpList.add(rrp.as());
			}
		}
		return rrpList;
	}
	
	public void setValues(List<Parameter> vals) {
		if (vals != null) {
			for (Parameter val : vals) {
				ParameterDefinition pd = paramMap.get(val.getName());
				if (pd != null) {
					pd.setValue(val.getValue());
				}
			}
		}
	}
}

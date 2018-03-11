package ru.yan0kom.ssrs.client.service;

import java.util.List;

interface ParameterizeRequest {
	interface Parameter {
		String getName();
		void setName(String name); 
		String getValue();
		void setValue(String value); 
	}
	
	List<Parameter> getParameters();
	void setParameters(List<Parameter> parameters);
}

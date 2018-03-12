package ru.yan0kom.ssrs.client.service;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

interface ServiceBeanFactory extends AutoBeanFactory {
	AutoBean<ParameterizeRequest> renderRequest();
	AutoBean<ParameterizeRequest.Parameter> parameter();
	AutoBean<ReportExt> reportExt();
}

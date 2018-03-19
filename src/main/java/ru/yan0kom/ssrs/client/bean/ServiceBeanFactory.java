package ru.yan0kom.ssrs.client.bean;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ServiceBeanFactory extends AutoBeanFactory {
	AutoBean<Parameters> parameters();
	AutoBean<Parameter> parameter();
	AutoBean<ReportExt> reportExt();
}

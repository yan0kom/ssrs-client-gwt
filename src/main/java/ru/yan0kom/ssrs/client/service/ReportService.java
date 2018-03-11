package ru.yan0kom.ssrs.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

//import ru.shipsea.ssrs.client.MopeSsrsClient;

public class ReportService {
	public static String backendUrl = "/ssrs-client-back";
	public static String renderFormat = "HTML40";
	
	public static void load(String path, final ReportServiceLoadCallback callback) {
		StringBuilder query = new StringBuilder(backendUrl);
		query.append("/run/load?path=");
		query.append(path);
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode(query.toString()));
		rb.setHeader("Content-Type", "application/json; charset=utf-8");
		try {
			rb.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						ExecutionInfo info = JsonUtils.safeEval(response.getText());
						callback.onLoad(info);
					} else {
						callback.onError(response, null);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onError(null, exception);
				}
			});
		} catch (RequestException exception) {
			callback.onError(null, exception);
		}		
	}
	
	public static void parameterize(String executionId, ReportParameters params, final ReportServiceParameterizeCallback callback) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, buildQuery("parameterize", executionId, null, null, null));		
		rb.setHeader("Content-Type", "application/json; charset=utf-8");
		
		ServiceBeanFactory factory = GWT.create(ServiceBeanFactory.class);
		AutoBean<ParameterizeRequest> rr = factory.renderRequest();
		rr.as().setParameters(params.getAllParamValues());
		rb.setRequestData(AutoBeanCodex.encode(rr).getPayload());
		
		rb.setCallback(new RequestCallback() {			
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode()) {
					callback.onParameterize();
				}else {
					handleError(response, callback);
				}
			}
			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(null, exception);
			}
		});
		
		try {
			rb.send();
		} catch (RequestException exception) {
			callback.onError(null, exception);
		}		
	}	
	
	public static void render(String executionId, String path, ReportParameters params, final ReportServiceRenderCallback callback) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, buildQuery("render", executionId, path, renderFormat, null));		
		rb.setHeader("Content-Type", "application/json; charset=utf-8");		
		rb.setCallback(new RequestCallback() {			
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode()) {
					callback.onRender(response.getText());
				}else {
					handleError(response, callback);
				}
			}
			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(null, exception);
			}
		});
		
		try {
			rb.send();
		} catch (RequestException exception) {
			callback.onError(null, exception);
		}		
	}
	
	public static void export(String executionId, String path, String format, String fileName, ReportParameters params) {
		Window.open(buildQuery("render", executionId, path, format, fileName), "_self", null);
	}

	public static void print(String executionId, String path, String format, ReportParameters params) {
		Window.open(buildQuery("render", executionId, path, format, null), "_blank", null);
	}
	
	private static void handleError(Response response, ReportServiceErrorCallback callback) {
		if (500 == response.getStatusCode()) {
			ResponseError err = JsonUtils.safeEval(response.getText());
			if ("org.apache.axis.AxisFault".equals(err.getException()) && err.getMessage() != null &&
					err.getMessage().contains("ExecutionNotFoundException")) {
				callback.onError(response, new ExecutionNotFoundException(err.getMessage()));
			}else {
				callback.onError(response, new Exception(err.getMessage()));
			}
		}else {
			callback.onError(response, null);
		}		
	}
	
	private static String buildQuery(String method, String executionId, String path, String format, String fileName) {
		StringBuilder query = new StringBuilder("/ssrs-client-back/run/");
		query.append(method);
		query.append("?executionId=");
		query.append(executionId);
		if (path != null) {
			query.append("&report=");
			query.append(path);
		}
		if (format != null) {
			query.append("&format=");
			query.append(format);			
		}	
		if (fileName != null) {
			query.append("&fileName=");
			query.append(fileName);			
		}
		return query.toString();
	}

}
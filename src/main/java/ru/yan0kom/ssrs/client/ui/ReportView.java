package ru.yan0kom.ssrs.client.ui;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.yan0kom.ssrs.client.bean.ReportExt;
import ru.yan0kom.ssrs.client.service.RdlExecutionInfo;
import ru.yan0kom.ssrs.client.service.ExecutionNotFoundException;
import ru.yan0kom.ssrs.client.service.ParametersDefinition;
import ru.yan0kom.ssrs.client.service.ReportService;
import ru.yan0kom.ssrs.client.service.ReportServiceLoadCallback;
import ru.yan0kom.ssrs.client.service.ReportServiceParameterizeCallback;
import ru.yan0kom.ssrs.client.service.ReportServiceRenderCallback;
import ru.yan0kom.ssrs.client.ui.ParametersBar;

public class ReportView extends Composite 
		implements ReportServiceLoadCallback, ReportServiceParameterizeCallback, ReportServiceRenderCallback {
	
	private HTML reportContent;
	private ParametersBar parametersBar;
	private ReportMenu menu;
	private ParametersDefinition reportParams;
	private RdlExecutionInfo executionInfo;
	private boolean parametersInitialized;
	private ReportExt ext;	
	
	private class ReportMenu extends MenuBar {
		public void setEnabled(boolean enable) {
			for (MenuItem item : getItems()){
				item.setEnabled(enable);
			}
		}
	};	

	public ReportView(ReportExt ext) {
		this.ext = ext;
		VerticalPanel container = new VerticalPanel();
				
		//params
		parametersBar = new ParametersBar((param) -> {
			reload();
		});		
		container.add(parametersBar);
		
		//content
		reportContent = new HTML();		
		container.add(reportContent);
		
		//actions		
		menu = new ReportMenu();

		if (ReportViewer.getInstance().isShowReturn()) {
			menu.addItem("Возврат", () -> {
				ReportViewer.getInstance().goFirst();
			});
			
		}
		if (ReportViewer.getInstance().isShowBack()) {
			menu.addItem("Назад", () -> {
				ReportViewer.getInstance().goBack();
			});
			
		}
		menu.addItem("Обновить", () -> {
			reload();
		});
		
		MenuBar menuPrint = new MenuBar(true);
		menuPrint.addItem("PDF для печати", () -> {
			ReportService.print(executionInfo.getExecutionId(), ext.getPath(), "PDF", reportParams);
		});
		menuPrint.addItem("HTML для печати", () -> {
			ReportService.print(executionInfo.getExecutionId(), ext.getPath(), "HTML40", reportParams);
		});
		menu.addItem("Печать", menuPrint);
		
		MenuBar menuExport = new MenuBar(true);
		menuExport.addItem("PDF", makeExportCommand("PDF"));
		menuExport.addItem("Excel 2007+", makeExportCommand("EXCELOPENXML"));		
		menuExport.addItem("Powerpoint 2007+", makeExportCommand("PPTX"));
		menuExport.addItem("Word 2007+", makeExportCommand("WORDOPENXML"));
		menuExport.addItem("Web-архив (.mht)", makeExportCommand("MHTML"));
		
		MenuBar menuExportData = new MenuBar(true);
		menuExportData.addItem("Формат CSV", makeExportCommand("CSV"));
		//menuExportData.addItem("Формат с разделением табуляцией", cmdRefresh);
		menuExportData.addItem("Формат XML", makeExportCommand("XML"));
		menuExport.addItem("Данные", menuExportData);
		
		menu.addItem("Экспорт", menuExport);
		
		container.add(menu);
		
		initWidget(container);
		setStyleName("report-view", true);		
	}
	
	public void load() {
		if (executionInfo == null) {
			setEnabled(false);
			reportContent.setHTML("<br><br><i>Загрузка отчета...</i><br><br>");
			ReportService.load(ext.getPath(), this);
		}
	}
	
	private void reload() {
		executionInfo = null;
		load();
	}
	
	//не обновляет диаграммы, так что делаю reload
	private void refresh() {
		reportContent.setHTML("<br><br><i>Формирование отчета...</i><br><br>");
		ReportService.parameterize(executionInfo.getExecutionId(), reportParams, this);		
	}

	private Command makeExportCommand(final String format) {
		return new Command() {
			@Override
			public void execute() {
				ReportService.export(executionInfo.getExecutionId(), ext.getPath(), format, getName(), reportParams);
			}
		};
	}
	
	private String getName() {
		String parts[] = ext.getPath().split("/");
		if (parts.length > 2) {
			return parts[2] + "_" + parts[3];	
		}
		return "report";
	}
	
	private void setEnabled(boolean enable) {
		parametersBar.setEnabled(enable);
		menu.setEnabled(enable);
	}
	
	@Override
	public void onLoad(RdlExecutionInfo info) {
		executionInfo = info;
		if (parametersInitialized) {
			refresh();
			return;
		}
					
		reportParams = new ParametersDefinition(info.getParameters());
		reportParams.setValues(ext.getParameters());
		parametersBar.build(reportParams);
		parametersInitialized = true;
		
		reportContent.setHTML("<br><br><i>Определение отчета загружено</i><br><br>");
		refresh();
	}

	@Override
	public void onParameterize() {
		ReportService.render(executionInfo.getExecutionId(), ext.getPath(), reportParams, this);			
	}

	@Override
	public void onRender(String html) {
		reportContent.setHTML(html);
		setEnabled(true);
	}
	
	@Override
	public void onError(Response response, Throwable exception) {
		if (exception != null && exception instanceof ExecutionNotFoundException) {
			reload();
		}else {
			reportContent.setHTML(ReportViewer.makeErrorMessage(response, exception));
		}
	}
}

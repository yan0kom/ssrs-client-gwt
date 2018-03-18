package ru.yan0kom.ssrs.client.gwt;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.yan0kom.ssrs.client.gwt.ParametersBar;
import ru.yan0kom.ssrs.client.service.ExecutionInfo;
import ru.yan0kom.ssrs.client.service.ExecutionNotFoundException;
import ru.yan0kom.ssrs.client.service.ReportParameters;
import ru.yan0kom.ssrs.client.service.ReportService;
import ru.yan0kom.ssrs.client.service.ReportServiceLoadCallback;
import ru.yan0kom.ssrs.client.service.ReportServiceParameterizeCallback;
import ru.yan0kom.ssrs.client.service.ReportServiceRenderCallback;

public class ReportView extends Composite 
		implements ReportServiceLoadCallback, ReportServiceParameterizeCallback, ReportServiceRenderCallback {
	private HTML reportContent;
	private ParametersBar parametersBar;
	private ReportParameters reportParams;
	private ExecutionInfo executionInfo;
	private boolean parametersInitialized;
	private String path;

	public ReportView(String path) {
		this.path = path;
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
		final MenuBar menu = new MenuBar();
				
		Command cmdRefresh = new Command() {
			@Override
			public void execute() {
				reload();
			}	
		};
		menu.addItem("Обновить", cmdRefresh);
		
		final MenuBar menuPrint = new MenuBar(true);
		menuPrint.addItem("PDF для печати", () -> {
			ReportService.print(executionInfo.getExecutionId(), path, "PDF", reportParams);
		});
		menuPrint.addItem("HTML для печати", () -> {
			ReportService.print(executionInfo.getExecutionId(), path, "HTML40", reportParams);
		});
		menu.addItem("Печать", menuPrint);
		
		final MenuBar menuExport = new MenuBar(true);
		menuExport.addItem("PDF", makeExportCommand("PDF"));
		menuExport.addItem("Excel 2007+", makeExportCommand("EXCELOPENXML"));		
		menuExport.addItem("Powerpoint 2007+", makeExportCommand("PPTX"));
		menuExport.addItem("Word 2007+", makeExportCommand("WORDOPENXML"));
		menuExport.addItem("Web-архив (.mht)", makeExportCommand("MHTML"));
		
		final MenuBar menuExportData = new MenuBar(true);
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
			reportContent.setHTML("<br><br><i>Загрузка отчета...</i><br><br>");
			ReportService.load(path, this);
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
				ReportService.export(executionInfo.getExecutionId(), path, format, getName(), reportParams);
			}
		};
	}
	
	private String getName() {
		String parts[] = path.split("/");
		if (parts.length > 2) {
			return parts[2] + "_" + parts[3];	
		}
		return "report";
	}
	
	@Override
	public void onLoad(ExecutionInfo info) {
		executionInfo = info;
		if (parametersInitialized) {
			refresh();
			return;
		}
					
		reportParams = new ReportParameters(info.getParameters());
		parametersBar.build(reportParams);
		parametersInitialized = true;
		
		reportContent.setHTML("<br><br><i>Определение отчета загружено</i><br><br>");
		refresh();
	}

	@Override
	public void onParameterize() {
		ReportService.render(executionInfo.getExecutionId(), path, reportParams, this);			
	}

	@Override
	public void onRender(String html) {
		reportContent.setHTML(html);
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

package edu.cust.util.excel;

import javax.servlet.http.HttpServletRequest;

public interface ImportExport {
	
	public ImportHeaders getImportHeaders(HttpServletRequest request);
	
	public ExportHeaders getExportHeaders(HttpServletRequest request);

}

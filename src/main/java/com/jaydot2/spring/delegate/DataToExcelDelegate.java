/**
 * 
 */
package com.jaydot2.spring.delegate;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jaydot2.spring.dao.SurveyDAO;
import com.jaydot2.spring.model.Survey;

/**
 * <b>Description</b>
 * <p>
 * 
 * </p>
 * 
 * @author james_r_bray
 *
 */
public class DataToExcelDelegate {
	
	private Logger log = LogManager.getLogger(DataToExcelDelegate.class);
	
	private SurveyDAO surveyDao;

	/**
	 * 
	 */
	public DataToExcelDelegate() {
		surveyDao = SurveyDAO.getInstance();
	}
	
	/**
	 * <b>Description:</b>
	 * <p>
	 * Get the survey data and populate a new spreadsheet.
	 * </p>
	 */
	public void createSpreadsheet() {
		log.debug("Entering createSpreadsheet()...");
		Map<String, List<Survey>> data = new HashMap<String, List<Survey>>();
		List<Survey> surveys = surveyDao.retrieveAllSurveyRecords();
		
		Workbook wb = new HSSFWorkbook();
		Sheet surveySheet = wb.createSheet("SurveyData");
		
		int index = 0;
		// Create header row
		Row rowHeader = surveySheet.createRow(index);
		Cell cellA1 = rowHeader.createCell(0);
		cellA1.setCellValue("ID");
		Cell cellB1 = rowHeader.createCell(1);
		cellB1.setCellValue("RATING");
		Cell cellC1 = rowHeader.createCell(2);
		cellC1.setCellValue("WHY FEELING");
		Cell cellD1 = rowHeader.createCell(3);
		cellD1.setCellValue("WORK SATISFACTION");
		Cell cellE1 = rowHeader.createCell(4);
		cellE1.setCellValue("ANSWER");
		Cell cellF1 = rowHeader.createCell(5);
		cellF1.setCellValue("COMMENT");
		Cell cellG1 = rowHeader.createCell(1);
		cellG1.setCellValue("INSTITUTION KEY");
		// loop here and create data rows
		for(Survey survey : surveys) {
			index++;
			Row row = surveySheet.createRow(index);
			Cell cellA = row.createCell(0);
			cellA.setCellValue(survey.getId());
			Cell cellB = row.createCell(1);
			cellB.setCellValue(survey.getRating());
			Cell cellC = row.createCell(2);
			cellC.setCellValue(survey.getWhyFeeling());
			Cell cellD = row.createCell(3);
			cellD.setCellValue(survey.getWorkDissatisfaction());
			Cell cellE = row.createCell(4);
			cellE.setCellValue(survey.getAnswerMatrix());
			Cell cellF = row.createCell(5);
			cellF.setCellValue(survey.getComment());
			Cell cellG = row.createCell(1);
			cellG.setCellValue(survey.getKey());
		}
		
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("/var/www/html/docsurvey/downloads/survey.xls"));
			wb.write(bos);
			bos.close();
		} catch(IOException ioe) {
			log.error("A critical error was thrown.  Unable to write out Excel file:  " + ioe.getMessage());
		}
		
		//TODO Send completed spreadsheet to document
		log.debug("Exiting createSpreadsheet()...");
	}

}

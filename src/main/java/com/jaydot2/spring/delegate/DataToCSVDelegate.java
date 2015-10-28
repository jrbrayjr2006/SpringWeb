package com.jaydot2.spring.delegate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jaydot2.spring.model.Survey;

/**
 * <b>Description:</b>
 * <p>
 * This delegate class is used to convert Lists of data objects to comma separated value output
 * </p>
 * 
 * @author james_r_bray
 *
 */
public class DataToCSVDelegate {
	
	private Logger log = LogManager.getLogger(DataToCSVDelegate.class);
	
	//Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	private Object[] fileHeader;


	public DataToCSVDelegate(Object[] _fileHeader) {
		this.fileHeader = _fileHeader;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public String convertSurveysToCSV(List<Survey> surveys) {
		log.debug("Entering convertSurveysToCSV(List<Survey>)...");
		// could create a factory to handle this
		//FileWriter fWriter = null;
		StringWriter sWriter = null;
		StringBuffer output = new StringBuffer();
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
		try {
			sWriter = new StringWriter();
			//fWriter = new FileWriter("/home/ec2-user/survey_data.txt");
			csvFilePrinter = new CSVPrinter(sWriter, csvFormat);
			for(Survey survey : surveys) {
				List<String> surveyRecord = new ArrayList<String>();
				surveyRecord.add(String.valueOf(survey.getId()));
				surveyRecord.add(String.valueOf(survey.getRating()));
				surveyRecord.add(survey.getWhyFeeling());
				surveyRecord.add(survey.getWorkDissatisfaction());
				surveyRecord.add(survey.getAnswerMatrix());
				surveyRecord.add(survey.getComment());
				csvFilePrinter.printRecord(surveyRecord);
			}
			output.append(sWriter.toString());
		} catch(IOException ioe) {
			log.error(ioe.getMessage());
		} finally {
			// will add code to flush here
			try{
				//sWriter.close();
				csvFilePrinter.close();
			} catch(IOException ioe) {
				// do nothing
			}
		}
		
		log.debug("Exiting convertSurveysToCSV(List<Survey>)...");
		return output.toString();
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public String convertToCSV(List data) {
		
		return null;
	}

}

/**
 * 
 */
package com.jaydot2.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaydot2.spring.dao.SurveyDAO;
import com.jaydot2.spring.delegate.DataToCSVDelegate;
import com.jaydot2.spring.delegate.DataToExcelDelegate;
import com.jaydot2.spring.model.Institution;
import com.jaydot2.spring.model.Survey;

/**
 * <b>Description:</b>
 * <p>
 * These are a set of exposed API's for CRUD operations on a backend database
 * </p>
 * @author james_r_bray
 *
 */
@CrossOrigin(maxAge = 3600)
@RestController
public class SurveyController {
	
	private Logger log = LogManager.getLogger(SurveyController.class);
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private SurveyDAO surveyDao;
	
	@RequestMapping("/simple")
	public String getSimpleData() {
		return "test";
	}
	
	@RequestMapping("/complex")
	public Map<String,String> getComplexData() {
		surveyDao = SurveyDAO.getInstance();
		return surveyDao.getDummyData();
	}
	
	@CrossOrigin
	@RequestMapping("/count")
	public int getRowCount() {
		surveyDao = SurveyDAO.getInstance();
		return surveyDao.getRecordCount();
	}
	
	@CrossOrigin
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public int insertRecord(@RequestParam(value="rating", required=true) int rating, 
			@RequestParam(value="why_feeling", required=true) String whyFeeling,
			@RequestParam(value="work_dissatisfaction", required=true) String workDissatisfaction,
			@RequestParam(value="answer_matrix", required=false, defaultValue="") String answerMatrix,
			@RequestParam(value="comments", required=false, defaultValue="no comment") String comments,
			@RequestParam(value="organization_key", required=true, defaultValue="DEMO") String key){
		log.debug("Entering insertRecord(int,String,String,String,String)...");
		surveyDao = SurveyDAO.getInstance();
		Survey survey = new Survey();
		survey.setRating(rating);
		survey.setWhyFeeling(whyFeeling);
		survey.setWorkDissatisfaction(workDissatisfaction);
		survey.setAnswerMatrix(answerMatrix);
		survey.setComment(comments);
		survey.setKey(key);
		log.debug("Feeling is " + survey.getWhyFeeling());
		int rows = surveyDao.insertRecord(survey);
		log.debug("Exiting insertRecord(int,String,String,String,String)...");
		return rows;
	}
	
	/**
	 * <b>Description:</b>
	 * <p>
	 * Exposed API for adding a new medical institution
	 * </p>
	 * <b>EXAMPLE:</b>
	 * <pre>
	 * http://localhost:8080/spring/neworg?organization_key=UCHI001&organization_name=University%20of%20Chicago%Medical&demo=PROD
	 * </pre>
	 * @param orgKey
	 * @param orgName
	 * @param demo
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/neworg", method=RequestMethod.POST)
	public int addNewInstitution(@RequestParam(value="organization_key", required=true) String orgKey, 
			@RequestParam(value="organization_name", required=false, defaultValue="none") String orgName,
			@RequestParam(value="demo", required=false, defaultValue="DEMO") String demo) {
		log.debug("Entering addNewInstitution(String,String,String)...");
		surveyDao = SurveyDAO.getInstance();
		Institution institution = new Institution();
		institution.setOrganizationKey(orgKey);
		institution.setOrganizationName(orgName);
		institution.setDemo(demo);
		int rows = surveyDao.createNewInstitutionRecord(institution);
		log.debug("Exiting addNewInstitution(String,String,String)...");
		return rows;
	}
	
	@CrossOrigin
	@RequestMapping("/institutions")
	public Map<String,List<Institution>> getInstitutions() {
		log.debug("Entering getInstitutions()...");
		surveyDao = SurveyDAO.getInstance();
		Map<String,List<Institution>> data = new HashMap<String,List<Institution>>();
		List<Institution> institutions = surveyDao.retrieveAllInstitutions();
		data.put("data", institutions);
		log.debug("Exiting getInstitutions()...");
		return data;
	}
	
	@CrossOrigin
	@RequestMapping("/surveydata")
	public Map<String, List<Survey>> getSurveyData() {
		log.debug("Entering getSurveyData()...");
		surveyDao = SurveyDAO.getInstance();
		Map<String, List<Survey>> data = new HashMap<String, List<Survey>>();
		List<Survey> surveys = surveyDao.retrieveAllSurveyRecords();
		data.put("data", surveys);
		log.debug("Exiting getSurveyData()...");
		return data;
	}
	
	@CrossOrigin
	@RequestMapping("/institutionsurveydata")
	public Map<String, List<Survey>> getSurveyDataByInstitution(@RequestParam(value="organization_key", required=true, defaultValue="DEMO") String key) {
		log.debug("Entering getSurveyDataByInstitution(String)...");
		surveyDao = SurveyDAO.getInstance();
		Map<String, List<Survey>> data = new HashMap<String, List<Survey>>();
		List<Survey> surveys = surveyDao.retrieveSurveyRecordsByInstitution(key);
		data.put("data", surveys);
		log.debug("Exiting getSurveyDataByInstitution(String)...");
		return data;
	}
	
	@CrossOrigin
	@RequestMapping("/exportdata")
	public String getSurveyDataAsCSV() {
		log.debug("Entering getSurveyDataAsCSV()...");
		List<Survey> data = new ArrayList<Survey>();
		Object[] header = {"id", "rating", "why_feeling", "work_dissatisfaction", "answer_matrix", "comments"};
		DataToCSVDelegate dataToCSV = new DataToCSVDelegate(header);
		surveyDao = SurveyDAO.getInstance();
		data = surveyDao.retrieveAllSurveyRecords();
		String output = dataToCSV.convertSurveysToCSV(data);
		log.debug("Exiting getSurveyDataAsCSV()...");
		return output;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/deleteinstitutions")
	public String deleteInstitutions() {
		log.debug("Entering deleteInstitutions()...");
		String message = "not deleted";
		surveyDao = SurveyDAO.getInstance();
		boolean result = surveyDao.deleteAllInstitutions();
		if(result) {
			message = "institutions deleted";
		}
		
		log.debug("Exiting deleteInstitutions()...");
		return message;
	}
	
	/**
	 * <p>
	 * Export the data to an Excel spreadsheet
	 * </p>
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/exporttoexcel")
	public String exportDataToSpreadsheet() {
		log.debug("Entering exportDataToSpreadsheet()...");
		DataToExcelDelegate dataToExcel = new DataToExcelDelegate();
		dataToExcel.createSpreadsheet();
		log.debug("Exiting exportDataToSpreadsheet()...");
		return "survey.xlsx";
	}
}

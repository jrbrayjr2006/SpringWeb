/**
 * 
 */
package com.jaydot2.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaydot2.spring.dao.SurveyDAO;
import com.jaydot2.spring.model.Institution;
import com.jaydot2.spring.model.Survey;

/**
 * @author james_r_bray
 *
 */
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
	
	@RequestMapping("/count")
	public int getRowCount() {
		surveyDao = SurveyDAO.getInstance();
		return surveyDao.getRecordCount();
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public int insertRecord(@RequestParam(value="rating", required=true) int rating, 
			@RequestParam(value="why_feeling", required=true) String whyFeeling,
			@RequestParam(value="work_dissatisfaction", required=true) String workDissatisfaction,
			@RequestParam(value="answer_matrix", required=false, defaultValue="") String answerMatrix,
			@RequestParam(value="comments", required=false, defaultValue="no comment") String comments) {
		log.debug("Entering insertRecord(int,String,String,String,String)...");
		surveyDao = SurveyDAO.getInstance();
		Survey survey = new Survey();
		survey.setRating(rating);
		survey.setWhyFeeling(whyFeeling);
		survey.setWorkDissatisfaction(workDissatisfaction);
		survey.setAnswerMatrix(answerMatrix);
		survey.setComment(comments);
		log.debug("Feeling is " + survey.getWhyFeeling());
		int rows = surveyDao.insertRecord(survey);
		log.debug("Exiting insertRecord(int,String,String,String,String)...");
		return rows;
	}
	
	@RequestMapping(value="/neworg")
	public int addNewInstitution(@RequestParam(value="organization_key", required=true) String orgKey, 
			@RequestParam(value="organization_name", required=false, defaultValue="none") String orgName) {
		log.debug("Entering addNewInstitution(String,String)...");
		surveyDao = SurveyDAO.getInstance();
		Institution institution = new Institution();
		institution.setOrganizationKey(orgKey);
		institution.setOrganizationName(orgName);
		int rows = surveyDao.createNewInstitutionRecord(institution);
		log.debug("Exiting addNewInstitution(String,String)...");
		return rows;
	}
	
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
}

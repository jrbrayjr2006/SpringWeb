/**
 * 
 */
package com.jaydot2.spring;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaydot2.spring.dao.SurveyDAO;
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
		surveyDao = new SurveyDAO();
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
}

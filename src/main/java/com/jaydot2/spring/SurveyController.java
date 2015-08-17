/**
 * 
 */
package com.jaydot2.spring;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydot2.spring.dao.SurveyDAO;
import com.jaydot2.spring.model.Survey;

/**
 * @author james_r_bray
 *
 */
@RestController
public class SurveyController {
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
	
	@RequestMapping("/add")
	public int insertRecord() {
		surveyDao = SurveyDAO.getInstance();
		Survey survey = new Survey();
		int rows = surveyDao.insertRecord(survey);
		return rows;
	}
}

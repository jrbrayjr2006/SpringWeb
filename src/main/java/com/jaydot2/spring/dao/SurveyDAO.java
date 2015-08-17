/**
 * 
 */
package com.jaydot2.spring.dao;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.jaydot2.spring.model.Survey;


/**
 * @author james_r_bray
 *
 */
@Repository
public class SurveyDAO {
	
	private Logger log = LogManager.getLogger(SurveyDAO.class);
	
	private static SurveyDAO surveyDao;
	
	private static final String insertSQL = "INSERT INTO survey (rating, why_feeling, work_dissatisfaction, answer_matrix, comments) VALUES (?, ?, ?, ?, ?);";
	
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	
	
	public static SurveyDAO getInstance() {
		if(surveyDao == null) {
			surveyDao = new SurveyDAO();
			surveyDao.setDataSource();
		}
		return surveyDao;
	}
	
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource() {
		log.debug("Entering setDataSource()...");
		this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		this.dataSource.setUrl("jdbc:mysql://localhost:3306/physician_survey");
		this.dataSource.setUsername("root");
		this.dataSource.setPassword("");
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		log.debug("Exiting setDataSource()...");
	}
	
	public void setDataSource(DataSource dataDource) {
		this.jdbcTemplate = new JdbcTemplate(dataDource);
	}
	
	public Map<String, String> getDummyData() {
		Map<String,String> data = new HashMap<String,String>();
		data.put("data", "dummy");
		return data;
	}
	
	public int getRecordCount() {
		log.debug("Entering getRecordCount()...");
		int rowCount = 0;
		String sql = "select count(*) from survey";
		rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
		log.debug("Exiting getRecordCount()...");
		return rowCount;
	}
	
	/**
	 * Insert a new record into the survey table
	 * @param survey
	 */
	public int insertRecord(Survey survey) {
		log.debug("Entering insertRecord(Survey)...");
		
		log.debug("Setup the parameters for the SQL statement");
		Object[] params = new Object[]{survey.getRating(), null, survey.getWorkDissatisfaction(), survey.getAnswerMatrix(), survey.getComment()};
		
		log.debug("define the argument types...");
		int[] types = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
		
		int rowCount = jdbcTemplate.update(insertSQL, params, types);
		log.debug(rowCount +" records successfully inserted!");
		log.debug("Exiting insertRecord(Survey)...");
		return rowCount;
	}

}

/**
 * 
 */
package com.jaydot2.spring.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;


/**
 * @author james_r_bray
 *
 */
@Repository
public class SurveyDAO {
	
	private Logger log = LogManager.getLogger(SurveyDAO.class);
	
	private static SurveyDAO surveyDao;
	
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

}

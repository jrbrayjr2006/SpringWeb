/**
 * 
 */
package com.jaydot2.spring.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.jaydot2.spring.model.Institution;
import com.jaydot2.spring.model.Survey;
import com.mysql.jdbc.Connection;


/**
 * @author james_r_bray
 *
 */
@Repository
public class SurveyDAO {
	
	private Logger log = LogManager.getLogger(SurveyDAO.class);
	
	private static SurveyDAO surveyDao;
	
	private static final String insertSQL = "INSERT INTO survey (rating, why_feeling, work_dissatisfaction, answer_matrix, comments) VALUES (?, ?, ?, ?, ?);";
	private static final String retrieveAllInstitutionRecordsSQL = "SELECT organization_key, organization_name, demo FROM institutions;";
	
	private static final String insertInstitutionRecordSQL = "INSERT INTO institutions (organization_key, organization_name, demo) VALUES (?,?,?);";
	private static final String retrieveInstitutionRecordSQL = "SELECT organization_key, organization_name, demo FROM institutions WHERE organization_key = ?;";
	private static final String retrieveAllSurveyDataSQL = "SELECT * FROM survey;";
	
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
	
	public List<Survey> retrieveAllSurveyRecords() {
		log.debug("Entering retrieveAllSurveyRecords()...");
		List<Survey> surveys = new ArrayList<Survey>();
		
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(retrieveAllSurveyDataSQL);
		for(Map<String,Object> row : rows) {
			Survey survey = new Survey();
			survey.setId((Integer)row.get("id"));
			survey.setRating((Integer)row.get("rating"));
			survey.setWhyFeeling((String)row.get("why_feeling"));
			survey.setWorkDissatisfaction((String)row.get("work_dissatisfaction"));
			survey.setAnswerMatrix((String)row.get("answer_matrix"));
			survey.setComment((String)row.get("comments"));
			surveys.add(survey);
		}
		log.debug("Exiting retrieveAllSurveyRecords()...");
		return surveys;
	}
	
	/**
	 * Insert a new record into the survey table
	 * @param survey
	 */
	public int insertRecord(Survey survey) {
		log.debug("Entering insertRecord(Survey)...");
		
		log.debug("Setup the parameters for the SQL statement");
		Object[] params = new Object[]{survey.getRating(), survey.getWhyFeeling(), survey.getWorkDissatisfaction(), survey.getAnswerMatrix(), survey.getComment()};
		
		log.debug("define the argument types...");
		int[] types = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
		
		int rowCount = jdbcTemplate.update(insertSQL, params, types);
		log.debug(rowCount +" records successfully inserted!");
		log.debug("Exiting insertRecord(Survey)...");
		return rowCount;
	}
	
	/**
	 * <p>
	 * Create a new institution record in the institutions table
	 * </p>
	 * @param institution
	 * @return
	 */
	public int createNewInstitutionRecord(Institution institution) {
		log.debug("Entering createNewInstitutionRecord(Institution)...");
		Object[] params = new Object[]{institution.getOrganizationKey(), institution.getOrganizationName(), institution.getDemo()};
		int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
		int rowCount = jdbcTemplate.update(insertInstitutionRecordSQL, params, types);
		log.debug("Exiting createNewInstitutionRecord(Institution)...");
		return rowCount;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Institution> retrieveAllInstitutions() {
		List<Institution> institutions = new ArrayList<Institution>();
		
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(retrieveAllInstitutionRecordsSQL);
		for(Map<String,Object> row : rows) {
			Institution inst = new Institution();
			inst.setOrganizationKey((String)row.get("organization_key"));
			inst.setOrganizationName((String)row.get("organization_name"));
			inst.setDemo((String)row.get("demo"));
			institutions.add(inst);
		}
		return institutions;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean retrieveInstitution(String key) {
		Institution inst = new Institution();
		boolean result = false;
		//TODO add logic to get institution
		Object params[] = new Object[]{key};
		int count = jdbcTemplate.queryForObject(retrieveInstitutionRecordSQL, params, Integer.class);
		if(count > 0) {
			result = true;
		}
		return result;
	}
	
	/*  MODIFY TO ALLOW CREATION OF CSV FILE */
	public void exportData(Connection conn,String filename) {
        Statement stmt;
        String query;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             
            //For comma separated file
            query = "SELECT * FROM survey into OUTFILE  '"+filename+
                    "' FIELDS TERMINATED BY ',' FROM survey t";
            stmt.executeQuery(query);
             
        } catch(Exception e) {
            e.printStackTrace();
            stmt = null;
        }
    }
	//*/
}

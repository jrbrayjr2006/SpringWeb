/**
 * 
 */
package com.jaydot2.spring.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author james_r_bray
 *
 */
public class Survey implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 444659582573476726L;
	private long id;
	private int rating;
	private String workDissatisfaction;
	private String answerMatrix;
	private Map<String,String> questionAnswer;
	private String comment;

	/**
	 * 
	 */
	public Survey() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getWorkDissatisfaction() {
		return workDissatisfaction;
	}

	public void setWorkDissatisfaction(String workDissatisfaction) {
		this.workDissatisfaction = workDissatisfaction;
	}

	public String getAnswerMatrix() {
		return answerMatrix;
	}

	public void setAnswerMatrix(String answerMatrix) {
		this.answerMatrix = answerMatrix;
	}

	public Map<String, String> getQuestionAnswer() {
		return questionAnswer;
	}

	public void setQuestionAnswer(Map<String, String> questionAnswer) {
		this.questionAnswer = questionAnswer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Survey other = (Survey) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Survey [rating=" + rating + ", workDissatisfaction=" + workDissatisfaction + "]";
	}
	

}

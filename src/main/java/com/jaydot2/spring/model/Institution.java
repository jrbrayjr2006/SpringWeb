/**
 * 
 */
package com.jaydot2.spring.model;

/**
 * @author james_r_bray
 *
 */
public class Institution {
	
	private String organizationKey;
	private String organizationName;
	private String demo;

	/**
	 * Default no argument constructor
	 */
	public Institution() {
		// default no argument constructor
	}

	public String getOrganizationKey() {
		return organizationKey;
	}

	public void setOrganizationKey(String organizationKey) {
		this.organizationKey = organizationKey;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

}

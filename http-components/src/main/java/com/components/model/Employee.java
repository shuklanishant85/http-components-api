package com.components.model;

import com.google.gson.annotations.SerializedName;

public class Employee {

	@SerializedName("id")
	private String id;
	@SerializedName("employee_name")
	private String employeeName;
	@SerializedName("employee_salary")
	private String employeeSalary;
	@SerializedName("employee_age")
	private String employeeAge;
	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("id")
	public String getId() {
		return id;
	}

	@SerializedName("id")
	public void setId(String id) {
		this.id = id;
	}

	@SerializedName("employee_name")
	public String getEmployeeName() {
		return employeeName;
	}

	@SerializedName("employee_name")
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@SerializedName("employee_salary")
	public String getEmployeeSalary() {
		return employeeSalary;
	}

	@SerializedName("employee_salary")
	public void setEmployeeSalary(String employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

	@SerializedName("employee_age")
	public String getEmployeeAge() {
		return employeeAge;
	}

	@SerializedName("employee_age")
	public void setEmployeeAge(String employeeAge) {
		this.employeeAge = employeeAge;
	}

	@SerializedName("profile_image")
	public String getProfileImage() {
		return profileImage;
	}

	@SerializedName("profile_image")
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

}
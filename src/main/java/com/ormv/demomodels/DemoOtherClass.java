package com.ormv.demomodels;

import java.util.Objects;

import com.ormv.annotations.Column;
import com.ormv.annotations.Entity;
import com.ormv.annotations.Id;

@Entity(tableName="test_table") 
public class DemoOtherClass {
	
	@Id(columnName="test_id")
	private int testId;
	
	@Column(columnName="test_field_1")
	private String testField1;

	
	
	public DemoOtherClass() {
		super();
	}

	public DemoOtherClass(int testId, String testField1) {
		super();
		this.testId = testId;
		this.testField1 = testField1;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTestField1() {
		return testField1;
	}

	public void setTestField1(String testField1) {
		this.testField1 = testField1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(testField1, testId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemoOtherClass other = (DemoOtherClass) obj;
		return Objects.equals(testField1, other.testField1) && testId == other.testId;
	}

	@Override
	public String toString() {
		return "DemoOtherClass [testId=" + testId + ", testField1=" + testField1 + "]";
	}
	
	
}

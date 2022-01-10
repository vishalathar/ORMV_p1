package com.ormv.demomodels;

import java.util.Objects;

import com.ormv.annotations.Column;
import com.ormv.annotations.Entity;
import com.ormv.annotations.Id;
import com.ormv.annotations.JoinColumn;


// annotated class

@Entity(tableName="users")
public class DemoUser {
	
	@Id(columnName="user_id", strategy="GenerationType.IDENTITY") // primary key
	private int id;
	
	@Column(columnName="first_name")
	private String firstName;
	
	@Column(columnName="last_name")
	private String lastname;
	
	@JoinColumn(columnName="test_relation")
	private DemoOtherClass testRelation;
	
	public DemoUser() {
		super();
	}
	
	public DemoUser(Object id) {
		super();
		this.id = (int) id;
	}
	
	public DemoUser(int id, String firstName, String lastname) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastname = lastname;
	}
	
	public DemoUser(String firstName, String lastname) {
		super();
		this.firstName = firstName;
		this.lastname = lastname;
	}

	@Override
	public String toString() {
		return "DemoUser [id=" + id + ", firstName=" + firstName + ", lastname=" + lastname + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, id, lastname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemoUser other = (DemoUser) obj;
		return Objects.equals(firstName, other.firstName) && id == other.id && Objects.equals(lastname, other.lastname);
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
	
	
}

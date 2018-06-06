package com.zk.json;

public class Account {

	private int id;
	private String name;
	private String email;
	private String address;
	private Birthday birthday;

	public Account() {
		
	}
	
	public Account(int id, String name, Birthday birthday) {
		super();
		this.id = id;
		this.name = name;
		this.birthday = birthday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Birthday getBirthday() {
		return birthday;
	}

	public void setBirthday(Birthday birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", birthday=" + birthday + "]";
	}



}

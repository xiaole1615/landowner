package com.landowner;

public class Persion {

	private int id;
	private String name;
	private int sex;
	private int age;
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Persion(int id, String name, int sex, int age) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
	}
	public Persion() {
		super();
	}
	
	
}

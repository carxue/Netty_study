package org.netty.discard;

import org.msgpack.annotation.Message;

@Message
public class Person {
	private String name;
	private Integer age;
	private Boolean sex;
	private String describe;

	
	public Person() {
		super();
	}

	public Person(String name, Boolean sex) {
		super();
		this.name = name;
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public Person setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Person setAge(Integer age) {
		this.age = age;
		return this;
	}

	public Boolean getSex() {
		return sex;
	}

	public Person setSex(Boolean sex) {
		this.sex = sex;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public Person setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", sex=" + sex + ", describe=" + describe + "]";
	}

}

package com.geekbrains.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class Employee {
    private int id;
    private String name;
    private String position;
    private int age;
    private float salary;
    @JacksonXmlElementWrapper(useWrapping = false)
    private AddInfo addInfo;

    public Employee() {
        this(0, "", "", 0, 0.0f, null);
    }

    public Employee(int id, String name, String position, int age, float salary, AddInfo addInfo) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.age = age;
        this.salary = salary;
        this.addInfo = addInfo;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public AddInfo getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(AddInfo addInfo) {
        this.addInfo = addInfo;
    }

}

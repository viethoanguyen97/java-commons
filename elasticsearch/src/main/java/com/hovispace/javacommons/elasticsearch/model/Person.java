package com.hovispace.javacommons.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

    @JsonProperty("full_name")
    private String _fullName;

    @JsonProperty("age")
    private int _age;

    @JsonProperty("date_of_birth")
    private String _dateOfBirth;

    public Person() {
    }

    public Person(String fullName, int age, String dateOfBirth) {
        _age = age;
        _fullName = fullName;
        _dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return _age;
    }

    public void setAge(int age) {
        _age = age;
    }

    public String getFullName() {
        return _fullName;
    }

    public void setFullName(String fullName) {
        _fullName = fullName;
    }

    public String getDateOfBirth() {
        return _dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        _dateOfBirth = dateOfBirth;
    }
}

package com.hovispace.javacommons.utilities.kryo;

import java.util.Date;

public class Person {

    private String _name;
    private int _age;
    private Date _birthDate;

    public Person() {
    }

    public Person(String name, int age, Date birthDate) {
        _name = name;
        _age = age;
        _birthDate = birthDate;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getAge() {
        return _age;
    }

    public void setAge(int age) {
        _age = age;
    }

    public Date getBirthDate() {
        return _birthDate;
    }

    public void setBirthDate(Date birthDate) {
        _birthDate = birthDate;
    }
}

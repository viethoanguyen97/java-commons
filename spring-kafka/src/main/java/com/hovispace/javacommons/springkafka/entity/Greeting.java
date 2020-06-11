package com.hovispace.javacommons.springkafka.entity;

public class Greeting {

    private String _message;
    private String _name;

    public Greeting(String message, String name) {
        _message = message;
        _name = name;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
}

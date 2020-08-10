package com.hovispace.javacommons.utilities.kryo;

import java.io.Serializable;

public class ComplexObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private String _name;

    public ComplexObject() {}

    public ComplexObject(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
}

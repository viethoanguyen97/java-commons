package com.hovispace.javacommons.utilities.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.Date;

/**
 * Kryo is a Java serialization framework with a focus on speed, efficiency, and a user-friendly API.
 * Kryo is significantly faster and more compact than Java serialization (often as much as 10x),
 * but does not support all Serializable types and requires you to register the classes you’ll use in the program in advance for best performance.
 * https://stackoverflow.com/questions/58946987/what-are-the-pros-and-cons-of-java-serialization-vs-kryo-serialization
 * https://github.com/EsotericSoftware/kryo
 *
 */
public class OtherPersonSerializer extends Serializer<OtherPerson> {

    @Override
    public void write(Kryo kryo, Output output, OtherPerson person) {
        output.writeString(person.getName());
        output.writeLong(person.getBirthDate().getTime());
    }

    @Override
    public OtherPerson read(Kryo kryo, Input input, Class<? extends OtherPerson> aClass) {
        OtherPerson person = new OtherPerson();
        person.setName(input.readString());
        person.setBirthDate(new Date(input.readLong()));
        person.setAge(18); // we will customize one setter method to assure that the class will be this serializer in test
        return person;
    }
}

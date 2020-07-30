package com.hovispace.javacommons.utilities.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * If we need more control over the serialization process, we have two options;
 * we can write our own Serializer class and register it with Kryo or let the class handle the serialization by itself.
 */
public class KryoSerializerUnitTest {

    private Kryo _kryo;
    private Input _input;
    private Output _output;

    @Before
    public void init() throws FileNotFoundException {
        _kryo = new Kryo();

        // From kyro 5: registration is required by default. For this tutorial, kryo object is initialized in @Before method. imho: in production code, consider a verbose-object.
        // see: https://github.com/EsotericSoftware/kryo/issues/627
        _kryo.register(Person.class);
        _kryo.register(OtherPerson.class);
        _kryo.register(Date.class);
        _kryo.register(ComplexObject.class);
        _input = new Input(new FileInputStream("file.dat"));
        _output = new Output(new FileOutputStream("file.dat"));
    }

    /**
     * Default serializers: When Kryo serializes an object, it creates an instance of a previously registered Serializer class to do the conversion to bytes.
     * These are called default serializers and can be used without any setup on our part.
     * <p>
     * The library already provides several such serializers that process primitives, lists, maps, enums, etc.
     * If no serializer is found for a given class, then a FieldSerializer is used, which can handle almost any type of object.
     */
    @Test
    public void test_kryo_default_serialize_and_deserialize_object() throws Exception {
        Instant currentTime = Instant.now();
        Person person = new Person("Grapes", 22, Date.from(currentTime));
        _kryo.writeObject(_output, person);
        _output.close();

        Person readPerson = _kryo.readObject(_input, Person.class);
        _input.close();

        assertThat(readPerson).isEqualToComparingFieldByField(person);
    }

    @Test
    public void test_kryo_object_custom_serializer() throws Exception {
        Instant currentTime = Instant.now();
        OtherPerson person = new OtherPerson("Grapes", 0, Date.from(currentTime));
        _kryo.writeObject(_output, person);
        _output.close();

        OtherPerson readPerson = _kryo.readObject(_input, OtherPerson.class);
        _input.close();

        assertThat(readPerson.getAge()).isEqualTo(18);
        assertThat(readPerson.getName()).isEqualTo(person.getName());
        assertThat(readPerson.getBirthDate()).isEqualTo(person.getBirthDate());
    }

    /**
     * In sporadic cases, Kryo won't be able to serialize a class.
     * If this happens, and writing a custom serializer isn't an option, we can use the standard Java serialization mechanism using a JavaSerializer.
     * This requires that the class implements the Serializable interface as usual.
     */
    @Test
    public void test_kryo_with_JavaSerializable() throws Exception {
        ComplexObject complexObject = new ComplexObject("Grapes");
        _kryo.register(ComplexObject.class, new JavaSerializer());
        _kryo.writeObject(_output, complexObject);
        _output.close();

        ComplexObject readComplexObject = _kryo.readObject(_input, ComplexObject.class);
        assertThat(readComplexObject.getName()).isEqualTo("Grapes");
    }
}
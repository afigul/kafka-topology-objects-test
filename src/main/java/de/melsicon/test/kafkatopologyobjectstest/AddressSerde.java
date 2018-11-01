package de.melsicon.test.kafkatopologyobjectstest;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class AddressSerde implements Serde<Address> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<Address> serializer() {
        return new AddressSerializer();
    }

    @Override
    public Deserializer<Address> deserializer() {
        return new AddressDeserializer();
    }
}

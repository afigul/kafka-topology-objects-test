package de.melsicon.test.kafkatopologyobjectstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class AddressDeserializer implements Deserializer<Address> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Address deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        Address address = null;
        try {
            address = mapper.readValue(bytes, Address.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void close() {

    }
}

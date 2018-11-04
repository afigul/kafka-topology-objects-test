package de.melsicon.test.kafkatopologyobjectstest.serde;

import de.melsicon.test.kafkatopologyobjectstest.model.Address;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AdressSerdeTest {
    @Test
    public void testSerde() {
        AddressSerde serde = new AddressSerde();
        Address address = Address.builder().city("Frankfurt").build();
        byte[] serialized = serde.serializer().serialize("", address);
        Address result = serde.deserializer().deserialize("", serialized);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCity()).isEqualTo("Frankfurt");
    }
}

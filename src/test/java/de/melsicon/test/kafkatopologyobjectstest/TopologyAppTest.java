package de.melsicon.test.kafkatopologyobjectstest;

import de.melsicon.test.kafkatopologyobjectstest.model.Address;
import de.melsicon.test.kafkatopologyobjectstest.serde.AddressSerde;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class TopologyAppTest {
    @Test
    public void testTopology() {
        AddressSerde serde = new AddressSerde();
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "bla");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:1234");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, serde.getClass().getName());
        Processor processor = new Processor(props);
        Topology topology = processor.createTopology();
        ConsumerRecordFactory factory = new ConsumerRecordFactory(new StringSerializer(), serde.serializer());
        TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props);

        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Frankfurt").build()));

        ProducerRecord<String,Address> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), serde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");

        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));

        ProducerRecord<String,Address> readOutput2 = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), serde.deserializer());
        Assertions.assertThat(readOutput2).isNull();
    }

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

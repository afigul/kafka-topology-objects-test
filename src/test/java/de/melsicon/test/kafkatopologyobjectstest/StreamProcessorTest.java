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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class StreamProcessorTest {

    TopologyTestDriver topologyTestDriver;
    ConsumerRecordFactory factory;
    private AddressSerde addressSerde;

    @BeforeEach
    void setUp() {
        addressSerde = new AddressSerde();
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "bla");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:1234");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, addressSerde.getClass().getName());
        StreamProcessor streamProcessor = new StreamProcessor(props);
        Topology topology = streamProcessor.createTopology();
        topologyTestDriver = new TopologyTestDriver(topology, props);
        factory = new ConsumerRecordFactory(new StringSerializer(), addressSerde.serializer());
    }

    @Test
    public void testNonFilteredValues() {
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Frankfurt").build()));

        ProducerRecord<String, Address> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");
    }

    @Test
    public void testFilteredValues() {
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));

        ProducerRecord<String, Address> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput).isNull();
    }

    @Test
    public void testFilteredMultibleValues() {
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Frankfurt").build()));
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Frankfurt").build()));
        topologyTestDriver.pipeInput(factory.create("source", Address.builder().city("Bielefeld").build()));

        ProducerRecord<String, Address> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");

        readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");

        readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput).isNull();
    }

    @Test
    public void testFilteredMultibleValuesUpdateWithKey() {
        topologyTestDriver.pipeInput(factory.create("source", "key1", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", "key2", Address.builder().city("Frankfurt").build()));
        topologyTestDriver.pipeInput(factory.create("source", "key3", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", "key4", Address.builder().city("Bielefeld").build()));
        topologyTestDriver.pipeInput(factory.create("source", "key2", Address.builder().city("Frankfurt").build()));
        topologyTestDriver.pipeInput(factory.create("source", "key1", Address.builder().city("Bielefeld").build()));

        ProducerRecord<String, Address> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");

        readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput.value()).isNotNull();
        Assertions.assertThat(readOutput.value().getCity()).isEqualTo("Frankfurt");

        readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), addressSerde.deserializer());
        Assertions.assertThat(readOutput).isNull();
    }
}

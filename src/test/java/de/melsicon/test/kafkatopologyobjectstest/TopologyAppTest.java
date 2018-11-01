package de.melsicon.test.kafkatopologyobjectstest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Properties;

public class TopologyAppTest {
    @Test
    public void testTopology() {
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "bla");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:1234");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        Processor processor = new Processor(props);
        Topology topology = processor.createTopology();
        ConsumerRecordFactory factory = new ConsumerRecordFactory(new StringSerializer(), new StringSerializer());
        TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props);
        topologyTestDriver.pipeInput(factory.create("source", "blablub"));

        ProducerRecord<String,String> readOutput = topologyTestDriver.readOutput("sink", Serdes.String().deserializer(), Serdes.String().deserializer());
        Assertions.assertThat(readOutput.value()).isNotEmpty();
        Assertions.assertThat(readOutput.value()).isEqualTo("blablub");
    }

}

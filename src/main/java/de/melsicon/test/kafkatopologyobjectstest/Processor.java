package de.melsicon.test.kafkatopologyobjectstest;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Slf4j
public class Processor {
    private KafkaStreams streams;

    public Processor(Properties props) {
        this.streams = new KafkaStreams(createTopology(), props);
    }

    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Address> stream = builder.<String, Address>stream("source");
        stream.filter((s, address) -> !address.getCity().equalsIgnoreCase("Bielefeld")).to("sink");
        return builder.build();
    }

    public void start() {
        log.info("Starting streams");
        streams.cleanUp();
        streams.start();
    }

    public void stop() {
        log.info("closing streams");
        streams.close();
        streams.cleanUp();
    }
}

package de.melsicon.test.kafkatopologyobjectstest;

import de.melsicon.test.kafkatopologyobjectstest.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

@Slf4j
public class StreamProcessor {
    private KafkaStreams streams;

    public StreamProcessor(Properties props) {
        this.streams = new KafkaStreams(createTopology(), props);
    }

    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Address> stream = builder.<String, Address>stream("source");
        stream = stream.filter((s, address) -> !address.getCity().equalsIgnoreCase("Bielefeld"));
        stream.to("sink");

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

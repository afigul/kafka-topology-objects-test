package de.melsicon.test.kafkatopologyobjectstest;

import de.melsicon.test.kafkatopologyobjectstest.model.Address;
import de.melsicon.test.kafkatopologyobjectstest.serde.AddressSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;

@Slf4j
public class TableProcessor {
    private KafkaStreams streams;

    public TableProcessor(Properties props) {
        this.streams = new KafkaStreams(createTopology(), props);
    }

    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, Address> kTable = builder.table("source2");
        //kTable.filter((s, address) -> !address.getCity().equalsIgnoreCase("Bielefeld"));

        kTable.toStream().to("sink2");


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

package de.melsicon.test.kafkatopologyobjectstest;

import java.util.Properties;

public class TopologyApp {

    public static void main(String[] args) {
        Properties props = new Properties();
        StreamProcessor streamProcessor = new StreamProcessor(props);
        streamProcessor.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streamProcessor::stop));
    }

}

package de.melsicon.test.kafkatopologyobjectstest;

import java.util.Properties;

public class TopologyApp {

    public static void main(String[] args) {
        Properties props = new Properties();
        Processor processor = new Processor(props);
        processor.start();
        Runtime.getRuntime().addShutdownHook(new Thread(processor::stop));
    }

}

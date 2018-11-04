package de.melsicon.test.kafkatopologyobjectstest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String streetname;
    private int germanPlz;
    private String city;
}

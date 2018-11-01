package de.melsicon.test.kafkatopologyobjectstest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String name;
    private String streetname;
    private int germanPlz;
    private String city;
}

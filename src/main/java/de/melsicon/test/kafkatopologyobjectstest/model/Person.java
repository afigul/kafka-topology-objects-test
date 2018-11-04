package de.melsicon.test.kafkatopologyobjectstest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private String surname;
    private List<Address> addressList;
}

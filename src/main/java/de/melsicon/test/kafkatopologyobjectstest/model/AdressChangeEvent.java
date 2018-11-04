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
public class AdressChangeEvent {
    private String id = UUID.randomUUID().toString();
    private Address newAddress;
}

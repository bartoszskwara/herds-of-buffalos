package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@AllArgsConstructor
public enum Resource {
    wood, clay, iron;

    public static Integer BASE_PRODUCTION_AMOUNT = 100;

    public static Resource getByName(String name) {
        return Stream.of(values()).filter(v -> v.name().equals(name)).findFirst().orElse(null);
    }

    public static List<Resource> findAll() {
        return asList(values());
    }
}

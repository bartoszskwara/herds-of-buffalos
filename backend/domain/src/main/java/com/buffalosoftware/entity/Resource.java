package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;

import java.util.List;

import static java.util.Arrays.asList;

@AllArgsConstructor
public enum Resource {
    wood, clay, iron;

    public static List<Resource> findAll() {
        return asList(values());
    }
}

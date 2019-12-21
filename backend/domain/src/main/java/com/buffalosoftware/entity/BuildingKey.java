package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildingKey {
    townhall("Town Hall", 30),
    barracks("Barracks", 20),
    armory("Armory", 10),
    granary("Granary", 30),
    brickyard("Brickyard", 25),
    sawmill("Sawmill", 25),
    ironworks("Ironworks", 25),
    pasture("Pasture", 10),
    machineFactory("Machine Factory", 5),
    palace("Palace", 3),
    wall("Wall", 15),
    market("Market", 10),
    well("Well", 1),
    fountain("Fountain", 1),
    statue("Statue", 1);

    private String name;
    private Integer maxLevel;
}

package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

@Getter
@AllArgsConstructor
public enum Building {
    townhall("Town Hall", 30, 500L, 500L, 500L, 1.2),
    barracks("Barracks", 20, 500L, 600L, 700L, 1.1),
    armory("Armory", 10, 550L, 400L, 900L, 1.05),
    granary("Granary", 30, 800L, 800L, 800L, 1.2),
    brickyard("Brickyard", 25, 200L, 600L, 400L, 1.2),
    sawmill("Sawmill", 25, 500L, 200L, 400L, 1.2),
    ironworks("Ironworks", 25, 300L, 400L, 600L, 1.2),
    pasture("Pasture", 10, 1800L, 1200L, 1000L, 1.25),
    machineFactory("Machine Factory", 5, 2800L, 2200L, 2500L, 1.85),
    palace("Palace", 3, 50000L, 50000L, 50000L, 3.0),
    wall("Wall", 15, 900L, 700L, 300L, 1.3),
    market("Market", 10, 1000L, 1000L, 800L, 1.6),
    well("Well", 1, 17000L, 17000L, 17000L, 1.0),
    fountain("Fountain", 1, 11000L, 11900L, 10500L, 1.0),
    statue("Statue", 1, 13800L, 12500L, 16600L, 1.0);

    private static final List<Building> allAvailableBuildings;

    static {
        allAvailableBuildings = asList(Building.values());
    }

    private String name;
    private Integer maxLevel;
    private Long firstLevelWoodCost;
    private Long firstLevelClayCost;
    private Long firstLevelIronCost;
    private Double nextLevelCostFactor;

    public static List<Building> list() {
        return allAvailableBuildings;
    }

    public Long getWoodCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelWoodCost * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }

    public Long getClayCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelClayCost * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }

    public Long getIronCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelIronCost * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }
}

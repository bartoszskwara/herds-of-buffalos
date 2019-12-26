package com.buffalosoftware.entity;

import com.buffalosoftware.entity.Resource.Clay;
import com.buffalosoftware.entity.Resource.Iron;
import com.buffalosoftware.entity.Resource.Wood;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

@Getter
@AllArgsConstructor
public enum Building {
    townhall("Town Hall", 30, new Wood(500L), new Clay(500L), new Iron(500L), 1.2),
    barracks("Barracks", 20, new Wood(500L), new Clay(600L), new Iron(700L), 1.1),
    armory("Armory", 10, new Wood(550L), new Clay(400L), new Iron(900L), 1.05),
    granary("Granary", 30, new Wood(800L), new Clay(800L), new Iron(800L), 1.2),
    brickyard("Brickyard", 25, new Wood(200L), new Clay(600L), new Iron(400L), 1.2),
    sawmill("Sawmill", 25, new Wood(500L), new Clay(200L), new Iron(400L), 1.2),
    ironworks("Ironworks", 25, new Wood(300L), new Clay(400L), new Iron(600L), 1.2),
    pasture("Pasture", 10, new Wood(1800L), new Clay(1200L), new Iron(1000L), 1.25),
    machineFactory("Machine Factory", 5, new Wood(2800L), new Clay(2200L), new Iron(2500L), 1.85),
    palace("Palace", 3, new Wood(50000L), new Clay(50000L), new Iron(50000L), 3.0),
    wall("Wall", 15, new Wood(900L), new Clay(700L), new Iron(300L), 1.3),
    market("Market", 10, new Wood(1000L), new Clay(1000L), new Iron(800L), 1.6),
    well("Well", 1, new Wood(17000L), new Clay(17000L), new Iron(17000L), 1.0),
    fountain("Fountain", 1, new Wood(11000L), new Clay(11900L), new Iron(10500L), 1.0),
    statue("Statue", 1, new Wood(13800L), new Clay(12500L), new Iron(16600L), 1.0);

    private String name;
    private Integer maxLevel;
    private Wood firstLevelWoodCost;
    private Clay firstLevelClayCost;
    private Iron firstLevelIronCost;
    private Double nextLevelCostFactor;

    public static List<Building> list() {
        return asList(values());
    }

    public Long getWoodCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelWoodCost.getAmount() * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }

    public Long getClayCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelClayCost.getAmount() * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }

    public Long getIronCostForLevel(Integer nextLevel) {
        return Math.round(firstLevelIronCost.getAmount() * Math.pow(nextLevelCostFactor, nextLevel - 1));
    }
}

package com.buffalosoftware.entity;

import com.buffalosoftware.Cost;
import com.buffalosoftware.ICostEntity;
import com.buffalosoftware.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Getter
@AllArgsConstructor
public enum Building implements ICostEntity {
    townHall("Town Hall", 30, 1.2),
    barracks("Barracks", 20, 1.1),
    armory("Armory", 10, 1.05),
    granary("Granary", 30, 1.2),
    brickyard("Brickyard", 25, 1.2),
    sawmill("Sawmill", 25, 1.2),
    ironworks("Ironworks", 25, 1.2),
    pasture("Pasture", 10, 1.25),
    machineFactory("Machine Factory", 5, 1.85),
    palace("Palace", 3, 3.0),
    wall("Wall", 15, 1.3),
    market("Market", 10, 1.6),
    well("Well", 1, 1.0),
    fountain("Fountain", 1, 1.0),
    statue("Statue", 1, 1.0),
    shipyard("Shipyard", 5, 1.5);

    private String name;
    private Integer maxLevel;
    private Double nextLevelCostFactor;

    private static final List<Building> allAvailableBuildings;
    private static final Map<Building, Cost> buildingFirstLevelCost;
    private static final Map<String, Building> buildingsByKey;
    private final static Map<Building, Long> firstLevelConstructionTimeMap;

    static {
        allAvailableBuildings = asList(values());

        buildingFirstLevelCost = new HashMap<>();
        buildingFirstLevelCost.put(townHall, new Cost(500, 500, 500));
        buildingFirstLevelCost.put(barracks, new Cost(500, 600, 700));
        buildingFirstLevelCost.put(armory, new Cost(550, 400, 900));
        buildingFirstLevelCost.put(granary, new Cost(800, 800, 800));
        buildingFirstLevelCost.put(brickyard, new Cost(200, 600, 400));
        buildingFirstLevelCost.put(sawmill, new Cost(500, 200, 400));
        buildingFirstLevelCost.put(ironworks, new Cost(300, 400, 600));
        buildingFirstLevelCost.put(pasture, new Cost(1800, 1200, 1000));
        buildingFirstLevelCost.put(machineFactory, new Cost(2800, 2200, 2500));
        buildingFirstLevelCost.put(palace, new Cost(50000, 50000, 50000));
        buildingFirstLevelCost.put(wall, new Cost(900, 700, 300));
        buildingFirstLevelCost.put(market, new Cost(1000, 1000, 800));
        buildingFirstLevelCost.put(well, new Cost(17000, 17000, 17000));
        buildingFirstLevelCost.put(fountain, new Cost(11000, 11900, 10500));
        buildingFirstLevelCost.put(statue, new Cost(13800, 12500, 16600));
        buildingFirstLevelCost.put(shipyard, new Cost(10000, 10000, 10000));

        buildingsByKey = allAvailableBuildings.stream().collect(Collectors.toMap(Enum::name, Function.identity()));

        long debugValue = 10;
        firstLevelConstructionTimeMap = new HashMap<>();
        firstLevelConstructionTimeMap.put(townHall, 130000 / debugValue);
        firstLevelConstructionTimeMap.put(barracks, 160000 / debugValue);
        firstLevelConstructionTimeMap.put(armory, 180000 / debugValue);
        firstLevelConstructionTimeMap.put(granary, 240000 / debugValue);
        firstLevelConstructionTimeMap.put(brickyard, 105000 / debugValue);
        firstLevelConstructionTimeMap.put(sawmill, 100800 / debugValue);
        firstLevelConstructionTimeMap.put(ironworks, 104000 / debugValue);
        firstLevelConstructionTimeMap.put(pasture, 1200000 / debugValue);
        firstLevelConstructionTimeMap.put(machineFactory, 340000 / debugValue);
        firstLevelConstructionTimeMap.put(palace, 550000 / debugValue);
        firstLevelConstructionTimeMap.put(wall, 200000 / debugValue);
        firstLevelConstructionTimeMap.put(market, 200000 / debugValue);
        firstLevelConstructionTimeMap.put(well, 700000 / debugValue);
        firstLevelConstructionTimeMap.put(fountain, 700000 / debugValue);
        firstLevelConstructionTimeMap.put(statue, 650000 / debugValue);
        firstLevelConstructionTimeMap.put(shipyard, 800000 / debugValue);
    }

    public static List<Building> list() {
        return allAvailableBuildings;
    }

    @Override
    public Cost getUpgradingCostForLevel(Integer level) {
        Integer woodCost = Math.round(getFirstLevelCost(this).getWood() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer clayCost = Math.round(getFirstLevelCost(this).getClay() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer ironCost = Math.round(getFirstLevelCost(this).getIron() * (float) Math.pow(nextLevelCostFactor, level - 1));
        return new Cost(woodCost, clayCost, ironCost);
    }

    public long getConstructionTimeForLevel(Integer level) {
        return Math.round(getFirstLevelConstructionTimeMap(this) * (float) Math.pow(nextLevelCostFactor, level - 1));
    }

    private static Cost getFirstLevelCost(Building building) {
        return buildingFirstLevelCost.get(building);
    }

    public static Optional<Building> getByKey(String key) {
        return Optional.ofNullable(buildingsByKey.get(key));
    }

    private static Long getFirstLevelConstructionTimeMap(Building building) {
        return firstLevelConstructionTimeMap.get(building);
    }

}

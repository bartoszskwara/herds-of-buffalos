package com.buffalosoftware.unit;

import com.buffalosoftware.Cost;
import com.buffalosoftware.ICostEntity;
import com.buffalosoftware.entity.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.buffalosoftware.entity.Building.barracks;
import static com.buffalosoftware.entity.Building.machineFactory;
import static com.buffalosoftware.entity.Building.palace;
import static com.buffalosoftware.entity.Building.pasture;
import static com.buffalosoftware.entity.Building.shipyard;
import static com.buffalosoftware.entity.Building.townhall;
import static com.buffalosoftware.unit.UnitType.cavalry;
import static com.buffalosoftware.unit.UnitType.civilian;
import static com.buffalosoftware.unit.UnitType.infantry;
import static com.buffalosoftware.unit.UnitType.navy;
import static com.buffalosoftware.unit.UnitType.vehicle;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Getter
@AllArgsConstructor
public enum Unit implements ICostEntity {
    //civilian
    citizen("Citizen", civilian, townhall, 1, 1, 1.0, 1.0),

    //infantry
    spearman("Spearman",    infantry, barracks, 1, 3, 3.0, 3.0),
    guardsman("Guardsman",  infantry, barracks, 2, 2, 4.5, 4.5),
    axeman("Axeman",        infantry, barracks, 3, 3, 3.0, 3.0),
    archer("Archer",        infantry, barracks, 4, 3, 3.5, 3.5),
    spy("Spy",              infantry, barracks, 5, 1, 1.0, 1.0),
    stormTrooper("Storm Trooper",   infantry, barracks, 6, 5, 3.2, 3.2),
    greatBuffalo("Great Buffalo",   infantry, palace,   1, 1, 1.0, 1.0),

    //cavalry
    lightBuffalo("Light Buffalo", cavalry, pasture, 1, 4, 2.2, 2.2),
    heavyBuffalo("Heavy Buffalo", cavalry, pasture, 2, 3, 3.1, 3.1),

    //vehicle
    plasmaCanon("Plasma Canon", vehicle, machineFactory, 1, 2, 5.0, 5.0),
    bomber("Bomber",            vehicle, machineFactory, 2, 3, 4.6, 4.6),
    destroyer("Destroyer",      vehicle, machineFactory, 3, 2, 3.5, 3.5),

    //navy
    merchantShip("Merchant Ship",   navy, shipyard, 1, 1, 1.0, 1.0),
    sailboat("Sailboat",            navy, shipyard, 2, 3, 6.0, 6.0),
    warship("Warship",              navy, shipyard, 3, 3, 3.0, 3.0);

    private String name;
    private UnitType type;
    private Building building;
    private Integer orderInBuilding;
    private Integer maxLevel;
    private Double nextLevelCostFactor;
    private Double nextLevelSkillsFactor;

    private final static Map<Unit, Cost> firstLevelRecruitmentCostMap;
    private final static Map<Unit, Cost> firstLevelUpgradingCostMap;
    private final static Map<Unit, UnitSkills> firstLevelUnitSkillsMap;
    private final static Map<Unit, Building> buildingByUnitMap;
    private final static Map<Building, List<Unit>> unitsByBuildingMap;

    static {
        firstLevelRecruitmentCostMap = new HashMap<>();
        firstLevelRecruitmentCostMap.put(citizen, new Cost(10, 10, 10));
        firstLevelRecruitmentCostMap.put(spearman, new Cost(30, 30, 40));
        firstLevelRecruitmentCostMap.put(guardsman, new Cost(45, 45, 90));
        firstLevelRecruitmentCostMap.put(axeman, new Cost(60, 10, 50));
        firstLevelRecruitmentCostMap.put(archer, new Cost(100, 20, 40));
        firstLevelRecruitmentCostMap.put(spy, new Cost(100, 100, 100));
        firstLevelRecruitmentCostMap.put(stormTrooper, new Cost(110, 120, 150));
        firstLevelRecruitmentCostMap.put(greatBuffalo, new Cost(30000, 30000, 30000));
        firstLevelRecruitmentCostMap.put(lightBuffalo, new Cost(220, 220, 220));
        firstLevelRecruitmentCostMap.put(heavyBuffalo, new Cost(400, 330, 570));
        firstLevelRecruitmentCostMap.put(plasmaCanon, new Cost(1000, 800, 2000));
        firstLevelRecruitmentCostMap.put(bomber, new Cost(1800, 1000, 3000));
        firstLevelRecruitmentCostMap.put(destroyer, new Cost(4600, 5300, 6700));
        firstLevelRecruitmentCostMap.put(merchantShip, new Cost(1200, 1100, 800));
        firstLevelRecruitmentCostMap.put(sailboat, new Cost(1900, 1200, 2000));
        firstLevelRecruitmentCostMap.put(warship, new Cost(4600, 2900, 3800));

        firstLevelUnitSkillsMap = new HashMap<>();
        firstLevelUnitSkillsMap.put(citizen, new UnitSkills(10, 10, 10));
        firstLevelUnitSkillsMap.put(spearman, new UnitSkills(25, 45, 80));
        firstLevelUnitSkillsMap.put(guardsman, new UnitSkills(35, 35, 70));
        firstLevelUnitSkillsMap.put(axeman, new UnitSkills(45, 15, 60));
        firstLevelUnitSkillsMap.put(archer, new UnitSkills(40, 50, 30));
        firstLevelUnitSkillsMap.put(spy, new UnitSkills(10, 10, 50));
        firstLevelUnitSkillsMap.put(stormTrooper, new UnitSkills(70, 40, 90));
        firstLevelUnitSkillsMap.put(greatBuffalo, new UnitSkills(150, 150, 150));
        firstLevelUnitSkillsMap.put(lightBuffalo, new UnitSkills(90, 50, 90));
        firstLevelUnitSkillsMap.put(heavyBuffalo, new UnitSkills(65, 80, 120));
        firstLevelUnitSkillsMap.put(plasmaCanon, new UnitSkills(80, 80, 160));
        firstLevelUnitSkillsMap.put(bomber, new UnitSkills(100, 10, 120));
        firstLevelUnitSkillsMap.put(destroyer, new UnitSkills(200, 50, 170));
        firstLevelUnitSkillsMap.put(merchantShip, new UnitSkills(10, 10, 50));
        firstLevelUnitSkillsMap.put(sailboat, new UnitSkills(130, 100, 150));
        firstLevelUnitSkillsMap.put(warship, new UnitSkills(220, 210, 150));

        firstLevelUpgradingCostMap = new HashMap<>();
        firstLevelUpgradingCostMap.put(citizen, new Cost(100, 100, 100));
        firstLevelUpgradingCostMap.put(spearman, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(guardsman, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(axeman, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(archer, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(spy, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(stormTrooper, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(greatBuffalo, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(lightBuffalo, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(heavyBuffalo, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(plasmaCanon, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(bomber, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(destroyer, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(merchantShip, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(sailboat, new Cost(1000, 1000, 1000));
        firstLevelUpgradingCostMap.put(warship, new Cost(1000, 1000, 1000));

        buildingByUnitMap = list().stream()
                .collect(toMap(identity(), Unit::getBuilding));
        unitsByBuildingMap = list().stream()
                .collect(groupingBy(Unit::getBuilding));
    }
    

    public static Optional<Building> getBuildingByUnit(Unit unit) {
        return ofNullable(buildingByUnitMap.get(unit));
    }

    public static List<Unit> getUnitsByBuilding(Building building) {
        return unitsByBuildingMap.get(building) != null ? unitsByBuildingMap.get(building) : emptyList();
    }

    public static List<Unit> list() {
        return asList(values());
    }

    public Cost getRecruitmentCostForLevel(Integer level) {
        Integer woodCost = Math.round(getFirstLevelRecruitmentCost(this).getWood() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer clayCost = Math.round(getFirstLevelRecruitmentCost(this).getClay() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer ironCost = Math.round(getFirstLevelRecruitmentCost(this).getIron() * (float) Math.pow(nextLevelCostFactor, level - 1));
        return new Cost(woodCost, clayCost, ironCost);
    }

    @Override
    public Cost getUpgradingCostForLevel(Integer level) {
        Integer woodCost = Math.round(getFirstLevelUpgradingCost(this).getWood() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer clayCost = Math.round(getFirstLevelUpgradingCost(this).getClay() * (float) Math.pow(nextLevelCostFactor, level - 1));
        Integer ironCost = Math.round(getFirstLevelUpgradingCost(this).getIron() * (float) Math.pow(nextLevelCostFactor, level - 1));
        return new Cost(woodCost, clayCost, ironCost);
    }

    public UnitSkills getSkillsForLevel(Integer level) {
        Integer attack = Math.round(getFirstLevelUnitSkills(this).getAttack() * (float) Math.pow(nextLevelSkillsFactor, level - 1));
        Integer defense = Math.round(getFirstLevelUnitSkills(this).getDefense() * (float) Math.pow(nextLevelSkillsFactor, level - 1));
        Integer health = Math.round(getFirstLevelUnitSkills(this).getHealth() * (float) Math.pow(nextLevelSkillsFactor, level - 1));
        return new UnitSkills(attack, defense, health);
    }

    private static Cost getFirstLevelRecruitmentCost(Unit unit) {
        return firstLevelRecruitmentCostMap.get(unit);
    }
    private static Cost getFirstLevelUpgradingCost(Unit unit) {
        return firstLevelUpgradingCostMap.get(unit);
    }
    private static UnitSkills getFirstLevelUnitSkills(Unit unit) {
        return firstLevelUnitSkillsMap.get(unit);
    }
}

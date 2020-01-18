package com.buffalosoftware.resource;

import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
public class ResourceService {

    CityRepository cityRepository;

    public void decreaseResources(City city, ResourcesDto resourcesDto) {
        if(city == null || resourcesDto == null) {
            return;
        }
        CityResources currentWood = getResourcesAmount(city, Resource.wood);
        CityResources currentClay = getResourcesAmount(city, Resource.clay);
        CityResources currentIron = getResourcesAmount(city, Resource.iron);
        Integer newWoodAmount = currentWood.getAmount() - resourcesDto.getWood();
        Integer newClayAmount = currentWood.getAmount() - resourcesDto.getWood();
        Integer newIronAmount = currentWood.getAmount() - resourcesDto.getWood();

        if(newWoodAmount < 0 || newClayAmount < 0 || newIronAmount < 0) {
            throw new IllegalArgumentException("Resources cannot be negative!");
        }

        currentWood.setAmount(newWoodAmount);
        currentClay.setAmount(newClayAmount);
        currentIron.setAmount(newIronAmount);
    }

    public boolean areResourceRequirementsMet(Set<CityResources> cityResources, ResourcesDto resourcesCost) {
        return getAmountOf(wood, cityResources) >= resourcesCost.getWood()
                && getAmountOf(clay, cityResources) >= resourcesCost.getClay()
                && getAmountOf(iron, cityResources) >= resourcesCost.getIron();
    }


    public Integer getAmountOf(Resource resource, Set<CityResources> resources) {
        if(isEmpty(resources) || resource == null) {
            return 0;
        }

        return resources.stream()
                .filter(r -> resource.equals(r.getResource()))
                .findFirst()
                .map(CityResources::getAmount)
                .orElse(0);
    }

    private CityResources getResourcesAmount(City city, Resource resource) {
        return city.getCityResources().stream()
                .filter(r -> resource.equals(r.getResource()))
                .findFirst()
                .orElse(null);
    }
}

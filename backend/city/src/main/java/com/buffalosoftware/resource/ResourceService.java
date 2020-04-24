package com.buffalosoftware.resource;

import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.buffalosoftware.entity.Resource.*;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
public class ResourceService {

    public void decreaseResources(City city, ResourcesDto resourcesDto) {
        decreaseResources(city, resourcesDto, 1);
    }

    public void decreaseResources(City city, ResourcesDto resourcesDto, Integer amount) {
        if(city == null || resourcesDto == null) {
            return;
        }
        int value = resourcesDto.getWood() * amount;
        city.getCityResources().forEach(resource -> resource.decreaseAmount(value));
    }

    public boolean doesCityHaveEnoughResources(Set<CityResources> cityResources, ResourcesDto resourcesCost) {
        if(resourcesCost == null) {
            return true;
        }
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
}

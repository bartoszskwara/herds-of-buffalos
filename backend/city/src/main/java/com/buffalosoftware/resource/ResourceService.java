package com.buffalosoftware.resource;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.IProcessInstanceProducerProvider;
import com.buffalosoftware.api.resource.IResourceService;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class ResourceService implements IResourceService {
    private final CityRepository cityRepository;
    private final ITimeService timeService;
    private final IProcessInstanceProducerProvider processInstanceProducerProvider;

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

    @Override
    public void increaseResources(Long cityId, Resource resource, Integer amount) {
        if(amount == null) {
            return;
        }
        City city = cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City not found!"));
        Optional<CityResources> resourceInCity = city.getCityResources().stream()
                .filter(res -> resource.equals(res.getResource()))
                .findFirst();

        CityResources cityResources;
        if(resourceInCity.isPresent()) {
            cityResources = resourceInCity.get();
        } else {
            cityResources = CityResources.builder()
                    .city(city)
                    .amount(0)
                    .resource(resource)
                    .updateDate(timeService.now())
                    .build();
            city.getCityResources().add(cityResources);
        }
        cityResources.increaseAmount(amount);
        cityRepository.save(city);
    }
}

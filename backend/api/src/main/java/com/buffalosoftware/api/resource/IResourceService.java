package com.buffalosoftware.api.resource;

import com.buffalosoftware.entity.Resource;

public interface IResourceService {
    void increaseResources(Long cityId, Resource resource, Integer amount);
}

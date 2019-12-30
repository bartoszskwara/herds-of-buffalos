package com.buffalosoftware.dto.resources;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourcesDto {
    private Integer wood;
    private Integer clay;
    private Integer iron;
}

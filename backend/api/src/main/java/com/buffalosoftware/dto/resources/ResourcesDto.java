package com.buffalosoftware.dto.resources;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourcesDto {
    private Long wood;
    private Long clay;
    private Long iron;
}

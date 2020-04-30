package com.buffalosoftware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPointsDto {
    private Long userId;
    private Long points;
}

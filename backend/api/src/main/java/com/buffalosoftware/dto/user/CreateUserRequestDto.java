package com.buffalosoftware.dto.user;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class CreateUserRequestDto extends BaseDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}

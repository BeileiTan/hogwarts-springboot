package com.springdemo.hogwartsartifactsonline.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(Integer id,

                      @NotBlank(message = "name is required")
                      String username,

                      boolean enabled,

                      @NotBlank(message = "roles is required")
                      String roles
                      ) {
}

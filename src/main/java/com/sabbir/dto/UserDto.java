package com.sabbir.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private String createdBy;
    private String role;
}

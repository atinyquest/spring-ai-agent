package com.tinyquest.agent.api.user.model.request;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    private String username;
    private String password;
    private String email;

}

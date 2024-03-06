package com.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
//This DTO will be used for exposing attributes. i.e. we are not going to expose User entity now and will use User entity
//for DB only.
//Data Transfer Object
public class UserDto {

    private int id;
    private String name;
    private String email;
    private String password;
    private String about;
}

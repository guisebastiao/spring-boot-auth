package com.guisebastiao.springbootauth.dtos;

import com.guisebastiao.springbootauth.enuns.RoleEnum;
import com.guisebastiao.springbootauth.enuns.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private UUID id;

    @NotBlank(message = "Please enter your name")
    @Size(min = 3, max = 50, message = "The name is outside the allowed length")
    private String name;

    @NotBlank(message = "Please enter your e-mail")
    @Size(max = 250, message = "The e-mail is outside the allowed length")
    @Email(message = "The e-mail is invalid, please, enter valid e-mail")
    private String email;

    @NotBlank(message = "Please enter your password")
    @Size(min = 8, message = "The password must have at least 8 characters")
    @Size(max = 20, message = "The password can´t have more than 20 characters")
    private String password;

    @NotNull(message = "Please enter the user role")
    @ValueOfEnum(enumClass = RoleEnum.class, message = "Role is not valid")
    private String role;
}

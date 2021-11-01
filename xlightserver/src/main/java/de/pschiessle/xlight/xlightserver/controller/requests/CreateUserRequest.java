package de.pschiessle.xlight.xlightserver.controller.requests;

import javax.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank(message = "Username not provided") String name,
    @NotBlank(message = "Password not provided") String password
) {

}

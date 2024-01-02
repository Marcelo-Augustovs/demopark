package com.mavs.demopark.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UsuarioLoginDto {

    @Email(message = "Formato de email invalido")
    @NotBlank
    private String username;
    @Size(min = 6,max = 6)
    @NotBlank
    private String password;
}

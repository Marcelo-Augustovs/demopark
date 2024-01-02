package com.mavs.demopark.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioSenhaDto {

    private String senhaAtual;
    private String novaSenha;
    private String confirmarSenha;

}

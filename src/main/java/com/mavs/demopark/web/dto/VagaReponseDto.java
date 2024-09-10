package com.mavs.demopark.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class VagaReponseDto {
    private Long id;
    private String codigo;
    private String status;
}

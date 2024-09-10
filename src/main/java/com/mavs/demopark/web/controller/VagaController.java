package com.mavs.demopark.web.controller;

import com.mavs.demopark.entity.Vaga;
import com.mavs.demopark.service.VagaService;
import com.mavs.demopark.web.dto.VagaCreateDto;
import com.mavs.demopark.web.dto.VagaReponseDto;
import com.mavs.demopark.web.dto.mapper.VagaMapper;
import com.mavs.demopark.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma nova vaga",
            description = "Recurso para criar uma nova vaga."
                    + "Requisição exige uso de bearer token. Acesso restrito a Role'ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION,description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Vaga já cadastrado no sistema",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto){
        Vaga vaga = VagaMapper.toVagas(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "Localizar uma vaga",
            description = "Recurso para retornar uma vaga pelo seu codigo."
                    + "Requisição exige uso de bearer token. Acesso restrito a Role'ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = VagaReponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Usuario sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaReponseDto> findByCode(@PathVariable String codigo){
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }
}

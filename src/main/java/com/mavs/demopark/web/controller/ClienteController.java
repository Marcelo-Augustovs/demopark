package com.mavs.demopark.web.controller;

import com.mavs.demopark.entity.Cliente;
import com.mavs.demopark.jwt.JwtUserDetails;
import com.mavs.demopark.service.ClienteService;
import com.mavs.demopark.service.UsuarioService;
import com.mavs.demopark.web.dto.ClienteCreateDto;
import com.mavs.demopark.web.dto.ClienteResponseDto;
import com.mavs.demopark.web.dto.mapper.ClienteMapper;
import com.mavs.demopark.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes",description = "Contém todas as operações relativas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente",
            description = "Recurso para criar um novo cliente vinculado a um usuario cadastrado. "
             + "Requisição exige uso de bearer token. Acesso restrito a Role'CLIENT'",
            responses = {
                @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = ClienteResponseDto.class))),
                @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                @ApiResponse(responseCode = "409", description = "Cliente Cpf já cadastrado no sistema",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                      @AuthenticationPrincipal JwtUserDetails userDetails){

        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

}

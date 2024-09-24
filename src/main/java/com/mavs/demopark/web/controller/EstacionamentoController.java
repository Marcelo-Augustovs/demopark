package com.mavs.demopark.web.controller;

import com.mavs.demopark.entity.ClienteVaga;
import com.mavs.demopark.jwt.JwtUserDetails;
import com.mavs.demopark.repository.projection.ClienteVagaProjection;
import com.mavs.demopark.service.ClienteService;
import com.mavs.demopark.service.ClienteVagaService;
import com.mavs.demopark.service.EstacionamentoService;
import com.mavs.demopark.service.JasperService;
import com.mavs.demopark.web.dto.EstacionamentoCreateDto;
import com.mavs.demopark.web.dto.EstacionamentoResponseDto;
import com.mavs.demopark.web.dto.PageableDto;
import com.mavs.demopark.web.dto.mapper.ClienteVagaMapper;
import com.mavs.demopark.web.dto.mapper.PageableMapper;
import com.mavs.demopark.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Estacionamentos",description = "Operações de registro de entrada e saída de um veículo do estacionamento.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {
    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    private final ClienteService clienteService;

    private final JasperService jasperService;

    @Operation(summary = "operação de check-in", description = "Recurso para dar entrada de um veiculo no estacionamento " +
            "Requisição exige uso de bearen token. Acesso restrito a Role='ADMIN'",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                    headers = @Header(name = HttpHeaders.LOCATION,description = "URL de acesso ao recurso criado"),
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = EstacionamentoResponseDto.class))),
            @ApiResponse(responseCode = "403",description = "Recurso não permitido ao perfil de CLIENT",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErroMessage.class))),
            @ApiResponse(responseCode = "404",description = "Causas Possiveis <br/>" +
                    "- CPF do cliente não cadastrado no sistema; <br/>" +
                    "- Nenhuma vaga livre foi localizada;",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErroMessage.class))),
            @ApiResponse(responseCode = "422",description = "Recurso não permitido ao perfil de CLIENT",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErroMessage.class)))
    })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn(@RequestBody @Valid EstacionamentoCreateDto dto){
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
        
    }

    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole( 'ADMIN', 'CLIENT' )")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo){
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkout(@PathVariable String recibo){
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "Localizar os registros de estacionamento do cliente por CPF",description = "Localizar os " +
        "registros de estacionamento do cliente por CPF. Requisição exige uso de um bearer token.",
        security =@SecurityRequirement(name = "security"),
        parameters = {
            @Parameter(in = PATH, name = "cpf",description = "N° do CPF referente ao cliente a ser consultado",
                required = true
            ),
            @Parameter(in = QUERY, name = "page",description = "Representa a página retornada",
                        content = @Content(schema = @Schema(type = "integer",defaultValue = "0"))
            ),
            @Parameter(in = QUERY, name = "size",description = "Representa o total de elementos por página",
                        content = @Content(schema = @Schema(type = "integer",defaultValue = "5"))
            ),
            @Parameter(in = QUERY,name = "sort",description = "Campo padrão de ordenação 'dataEntrada,asc'. ",
                        array = @ArraySchema(schema = @Schema(type = "string",defaultValue = "dataEntrada,asc")),
                        hidden = true
            )
        },
        responses = {
            @ApiResponse(responseCode = "200",description = "Recurso localizado com sucesso",
                    content = @Content(mediaType = " application/json;charset=UTF-8",
                        schema = @Schema(implementation = PageableDto.class))),
            @ApiResponse(responseCode = "403",description = "Recurso não permitido ao perfil CLIENT",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErroMessage.class)))
        })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentoPorCpf(@PathVariable String cpf,
                                                                  @PageableDefault(size = 5,sort = "dataEntrada",
                                                                   direction = Sort.Direction.ASC) Pageable pageable){
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCliente(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @PageableDefault(size = 5,sort = "dataEntrada",
                                                                          direction = Sort.Direction.ASC) Pageable pageable){
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorUsuarioID(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> getRelatorio(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {
        String cpf = clienteService.buscarPorUsuarioId(user.getId()).getCpf();
        jasperService.addParams("CPF",cpf);

        byte[] bytes = jasperService.gerarPdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition","inline; filename=" + System.currentTimeMillis()  + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }

}

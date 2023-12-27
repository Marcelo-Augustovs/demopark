package com.mavs.demopark.web;

import com.mavs.demopark.entity.Usuario;
import com.mavs.demopark.service.UsuarioService;
import com.mavs.demopark.web.dto.UsuarioCreateDto;
import com.mavs.demopark.web.dto.UsuarioResponseDto;
import com.mavs.demopark.web.dto.UsuarioSenhaDto;
import com.mavs.demopark.web.dto.mapper.UsuarioMapper;
import com.mavs.demopark.web.exception.ErroMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios",description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuario.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;


    @Operation(summary = "Criar um novo usuário",description = "Recurso para criar um novo usuário",
               responses = {
                    @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuario já cadastrado no sistema",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                        content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
               })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Recuperar usuário pelo id",description = "Recuperar usuário pelo id",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
        UsuarioResponseDto userDto = UsuarioMapper.toDto(usuarioService.buscarPorId(id));
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Listar todos os usuários",description = "Listar todos os usuários cadatrados",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Lista com todos os usuários cadatrados",
                            content = @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAllUsers(){
        List<Usuario> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

    @Operation(summary = "Atualizar senha",description = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode = "204",description = "senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = void.class))),
                    @ApiResponse(responseCode = "400", description = "senha não confere",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos Invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErroMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id , @RequestBody UsuarioSenhaDto dto){
        Usuario user = usuarioService.editarSenha(id,dto.getSenhaAtual(),dto.getNovaSenha(),dto.getConfirmarSenha());
        return ResponseEntity.noContent().build();
    }
}

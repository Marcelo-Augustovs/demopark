package com.mavs.demopark.web.controller;

import com.mavs.demopark.entity.Usuario;
import com.mavs.demopark.service.UsuarioService;
import com.mavs.demopark.web.controller.dto.UsuarioCreateDto;
import com.mavs.demopark.web.controller.dto.UsuarioResponseDto;
import com.mavs.demopark.web.controller.dto.UsuarioSenhaDto;
import com.mavs.demopark.web.controller.dto.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
        UsuarioResponseDto userDto = UsuarioMapper.toDto(usuarioService.buscarPorId(id));
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAllUsers(){
        List<Usuario> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id , @RequestBody UsuarioSenhaDto dto){
        Usuario user = usuarioService.editarSenha(id,dto.getSenhaAtual(),dto.getNovaSenha(),dto.getConfirmarSenha());
        return ResponseEntity.noContent().build();
    }
}

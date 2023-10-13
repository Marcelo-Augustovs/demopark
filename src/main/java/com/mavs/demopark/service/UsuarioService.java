package com.mavs.demopark.service;

import com.mavs.demopark.entity.Usuario;
import com.mavs.demopark.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;


    @Transactional
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado"));
    }

    @Transactional
    public Usuario editarSenha(Long id, String password) {
        Usuario user = buscarPorId(id);
        user.setPassword(password);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getAllUsers() {
        List<Usuario> users = new ArrayList<>();
        users = repository.findAll();
        return users;
    }
}

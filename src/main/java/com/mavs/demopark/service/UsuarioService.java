package com.mavs.demopark.service;

import com.mavs.demopark.entity.Usuario;
import com.mavs.demopark.exception.UsernameUniqueViolationException;
import com.mavs.demopark.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
       try {
           return repository.save(usuario);
       }catch (DataIntegrityViolationException ex){
           throw new UsernameUniqueViolationException(String.format("Username %s já foi cadastrado",usuario.getUsername()));
       }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado"));
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmarSenha) {
        Usuario user = buscarPorId(id);
        if(!user.getPassword().equals(senhaAtual)){
            throw new RuntimeException("Sua Senha não confere.");
        }
        if(!novaSenha.equals(confirmarSenha)){
            throw new RuntimeException("Nova Senha não confere com confirmação de senha");
        }
        user.setPassword(novaSenha);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getAllUsers() {
        List<Usuario> users = new ArrayList<>();
        users = repository.findAll();
        return users;
    }
}

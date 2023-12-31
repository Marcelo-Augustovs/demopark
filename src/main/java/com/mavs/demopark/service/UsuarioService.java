package com.mavs.demopark.service;

import com.mavs.demopark.entity.Usuario;
import com.mavs.demopark.exception.EntityNotFoundException;
import com.mavs.demopark.exception.PasswordInvalidException;
import com.mavs.demopark.exception.PasswordInvalidException2;
import com.mavs.demopark.exception.UsernameUniqueViolationException;
import com.mavs.demopark.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder ;


    @Transactional
    public Usuario salvar(Usuario usuario) {
       try {
           usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
           return repository.save(usuario);
       }catch (DataIntegrityViolationException ex){
           throw new UsernameUniqueViolationException(String.format("Username %s já foi cadastrado",usuario.getUsername()));
       }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format( "Usuario id=%s não encontrado",id)) );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmarSenha) {
        Usuario user = buscarPorId(id);
        if(novaSenha.length() > 6 || novaSenha.length() < 6){
            throw new PasswordInvalidException2("A senha tem que possuir 6 digitos e não pode ser nula.");
        }
        if(!passwordEncoder.matches(senhaAtual, user.getPassword())){
            throw new PasswordInvalidException("Sua Senha não confere.");
        }
        if(!novaSenha.equals(confirmarSenha)){
            throw new PasswordInvalidException("Nova Senha não confere com confirmação de senha");
        }
        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getAllUsers() {
        List<Usuario> users = new ArrayList<>();
        users = repository.findAll();
        return users;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario com '%s' não encontrado",username)));
    }

    @Transactional(readOnly = true)
    public Usuario.Role buscarRolePorUsername(String username) {
        return repository.findRoleByUsername(username);
    }
}

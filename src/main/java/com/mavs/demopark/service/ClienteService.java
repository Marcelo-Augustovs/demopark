package com.mavs.demopark.service;

import com.mavs.demopark.entity.Cliente;
import com.mavs.demopark.exception.CpfUniqueViolationException;
import com.mavs.demopark.exception.EntityNotFoundException;
import com.mavs.demopark.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente){
       try {
           return clienteRepository.save(cliente);
       }
       catch (DataIntegrityViolationException ex){
           throw new CpfUniqueViolationException(
                   String.format("CPF: '%s' não pode ser cadastrado, ja existe no sistema", cliente.getCpf()));
       }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id=%s não encontrado",id)));
    }
}

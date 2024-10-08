package com.mavs.demopark.service;

import com.mavs.demopark.entity.ClienteVaga;
import com.mavs.demopark.exception.EntityNotFoundException;
import com.mavs.demopark.repository.ClienteVagaRepository;
import com.mavs.demopark.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository repository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga){
        return repository.save(clienteVaga);
    }

    public ClienteVaga buscarPorRecibo(String recibo) {
        return repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Recibo '%s' não encontrado no sistema ou check-out já realizado.",recibo)
                )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {
        return repository.findByClienteCpf(cpf, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioID(long id, Pageable pageable) {
        return repository.findAllByClienteUsuarioId(id, pageable);
    }
}

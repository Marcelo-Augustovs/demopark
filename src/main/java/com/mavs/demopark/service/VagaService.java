package com.mavs.demopark.service;

import com.mavs.demopark.entity.Vaga;
import com.mavs.demopark.exception.CodigoUniqueViolationException;
import com.mavs.demopark.exception.EntityNotFoundException;
import com.mavs.demopark.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mavs.demopark.entity.Vaga.statusVaga.LIVRE;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga){
        try{
            return vagaRepository.save(vaga);
        }
        catch (DataIntegrityViolationException ex){
            throw new CodigoUniqueViolationException(String.format("Vaga com codigo '%s' já cadastrada",vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo){
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga com codigo '%s' não foi encontrada", codigo))
        );
    }

    public Vaga buscarPorVagaLivre() {
       return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(
               () -> new EntityNotFoundException("nenhuma vagas livre encontrada")
       );
    }
}

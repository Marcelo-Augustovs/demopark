package com.mavs.demopark.web.dto.mapper;

import com.mavs.demopark.entity.Vaga;
import com.mavs.demopark.web.dto.VagaCreateDto;
import com.mavs.demopark.web.dto.VagaReponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVagas(VagaCreateDto dto){
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaReponseDto toDto(Vaga vaga){
        return new ModelMapper().map(vaga, VagaReponseDto.class);
    }
}

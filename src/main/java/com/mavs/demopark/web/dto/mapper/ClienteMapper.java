package com.mavs.demopark.web.dto.mapper;

import com.mavs.demopark.entity.Cliente;
import com.mavs.demopark.web.dto.ClienteCreateDto;
import com.mavs.demopark.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto){
        return new ModelMapper().map(dto,Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente){
        return new ModelMapper().map(cliente,ClienteResponseDto.class);
    }

    public static List<ClienteResponseDto> toListDto(List<Cliente> clientes) {
        return clientes.stream().map(cliente -> toDto(cliente)).collect(Collectors.toList());
    }
}

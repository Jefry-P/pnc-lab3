package com.ncapas.lab3.common.mappers;

import com.ncapas.lab3.domain.dto.request.CreateSpecimenRequest;
import com.ncapas.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.ncapas.lab3.domain.dto.response.specimen.SpecimenResponse;
import com.ncapas.lab3.domain.entity.Specimen;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpecimenMapper {

    public Specimen toEntityCreate(CreateSpecimenRequest request) {
        return Specimen.builder()
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public Specimen toEntityUpdate(UpdateSpecimenRequest request, UUID id) {
        return Specimen.builder()
                .id(id)
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public SpecimenResponse toDto(Specimen specimen) {
        return SpecimenResponse.builder()
                .id(specimen.getId())
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();
    }

    // TODO: El estudiante deberá agregar aquí el método para mapear un Page<Specimen> a Page<SpecimenResponse>
    // pista: utilizando .map(this::toDto)
    public Page<SpecimenResponse> toDtoList(Page<Specimen> specimen) {
        return specimen.map(this::toDto);
    }
}

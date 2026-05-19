package com.ncapas.lab3.service;

import com.ncapas.lab3.domain.dto.request.CreateSpecimenRequest;
import com.ncapas.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.ncapas.lab3.domain.dto.response.PageableResponse;
import com.ncapas.lab3.domain.dto.response.specimen.SpecimenResponse;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface SpecimenService {
    SpecimenResponse createSpecimen(CreateSpecimenRequest request);
    PageableResponse<SpecimenResponse> getAllSpecimens(int page, int size, String sortBy, String sortOrder);
    SpecimenResponse getSpecimenById(UUID id);
    SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request);
    SpecimenResponse deleteSpecimen(UUID id);
}

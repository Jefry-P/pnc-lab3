package com.ncapas.lab3.service.impl;

import com.ncapas.lab3.common.mappers.SpecimenMapper;
import com.ncapas.lab3.domain.dto.request.CreateSpecimenRequest;
import com.ncapas.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.ncapas.lab3.domain.dto.response.PageableResponse;
import com.ncapas.lab3.domain.dto.response.specimen.SpecimenResponse;
import com.ncapas.lab3.domain.entity.Specimen;
import com.ncapas.lab3.exceptions.ResourceNotFoundException;
import com.ncapas.lab3.repository.SpecimenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    private UUID specimenId;
    private Specimen specimenEntity;
    private SpecimenResponse specimenResponse;

    @BeforeEach
    void setUp() {
        specimenId = UUID.randomUUID();
        specimenEntity = new Specimen();
        specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Ancient Arrow")
                .region("Hyrule")
                .dangerLevel(5)
                .isFriendly(false)
                .build();
    }

    @Test
    @DisplayName("Create specimen successfully")
    void createSpecimen_Success() {
        CreateSpecimenRequest request = new CreateSpecimenRequest();
        
        when(specimenMapper.toEntityCreate(request)).thenReturn(specimenEntity);
        when(specimenRepository.save(specimenEntity)).thenReturn(specimenEntity);
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.createSpecimen(request);

        assertNotNull(result);
        assertEquals(specimenId, result.getId());
        assertEquals("Ancient Arrow", result.getName());
        verify(specimenRepository, times(1)).save(specimenEntity);
    }

    @Test
    @DisplayName("Get specimen by ID - Success")
    void getSpecimenById_Success() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.getSpecimenById(specimenId);

        assertNotNull(result);
        assertEquals(specimenId, result.getId());
        verify(specimenRepository, times(1)).findById(specimenId);
    }

    @Test
    @DisplayName("Get specimen by ID - Not Found Exception")
    void getSpecimenById_NotFound() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            specimenService.getSpecimenById(specimenId);
        });

        verify(specimenRepository, times(1)).findById(specimenId);
        verifyNoInteractions(specimenMapper);
    }

    @Test
    @DisplayName("Get all specimens - Success")
    void getAllSpecimens_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Specimen> specimenPage = new PageImpl<>(List.of(specimenEntity));
        Page<SpecimenResponse> responsePage = new PageImpl<>(List.of(specimenResponse));

        when(specimenRepository.findAll(pageable)).thenReturn(specimenPage);
        when(specimenMapper.toDtoList(specimenPage)).thenReturn(responsePage);

        PageableResponse<SpecimenResponse> result = specimenService.getAllSpecimens(0, 10, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(specimenId, result.getContent().getFirst().getId());
        verify(specimenRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Get all specimens - Empty List Exception")
    void getAllSpecimens_EmptyList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Specimen> emptyPage = new PageImpl<>(Collections.emptyList());
        Page<SpecimenResponse> emptyResponsePage = new PageImpl<>(Collections.emptyList());

        when(specimenRepository.findAll(pageable)).thenReturn(emptyPage);
        when(specimenMapper.toDtoList(emptyPage)).thenReturn(emptyResponsePage);

        assertThrows(ResourceNotFoundException.class, () -> {
            specimenService.getAllSpecimens(0, 10, "name", "asc");
        });

        verify(specimenRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Update specimen - Success")
    void updateSpecimen_Success() {
        UpdateSpecimenRequest request = new UpdateSpecimenRequest();
        
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);
        when(specimenMapper.toEntityUpdate(request, specimenId)).thenReturn(specimenEntity);
        when(specimenRepository.save(specimenEntity)).thenReturn(specimenEntity);

        SpecimenResponse result = specimenService.updateSpecimen(specimenId, request);

        assertNotNull(result);
        assertEquals(specimenId, result.getId());
        verify(specimenRepository, times(1)).save(specimenEntity);
    }

    @Test
    @DisplayName("Delete specimen - Success")
    void deleteSpecimen_Success() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.deleteSpecimen(specimenId);

        assertNotNull(result);
        assertEquals(specimenId, result.getId());
        verify(specimenRepository, times(1)).deleteById(specimenId);
    }
}

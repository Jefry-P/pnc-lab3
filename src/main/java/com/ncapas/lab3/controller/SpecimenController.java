package com.ncapas.lab3.controller;

import com.ncapas.lab3.common.mappers.SpecimenMapper;
import com.ncapas.lab3.domain.dto.request.CreateSpecimenRequest;
import com.ncapas.lab3.domain.dto.response.GeneralResponse;
import com.ncapas.lab3.domain.dto.response.specimen.SpecimenResponse;
import com.ncapas.lab3.service.SpecimenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/specimen")
@AllArgsConstructor
public class SpecimenController {

    private final SpecimenService specimenService;

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllSpecimens (@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortOrder) {
        return buildResponse(
                "Specimens found",
                HttpStatus.OK,
                specimenService.getAllSpecimens(page, size, sortBy, sortOrder)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById (@PathVariable UUID id) {
        return buildResponse(
                "Specimen found",
                HttpStatus.OK,
                specimenService.getSpecimenById(id)
        );
    }

    @PostMapping()
    public ResponseEntity<GeneralResponse> createProduct(@Valid @RequestBody CreateSpecimenRequest specimen) {
        return buildResponse(
                "Product created successfully",
                HttpStatus.CREATED,
                specimenService.createSpecimen(specimen)
        );
    }


    public ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build()
                );
    }
}

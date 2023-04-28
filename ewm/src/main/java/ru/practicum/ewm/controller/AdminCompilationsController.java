package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.CompilationDto;
import ru.practicum.ewm.entity.dto.NewCompilationDto;
import ru.practicum.ewm.entity.dto.UpdateCompilationRequest;
import ru.practicum.ewm.error.exeptions.BadRequestException;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationsController {

    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null) {
            throw new BadRequestException("NewCompilationDto.title is empty");
        }
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compilationId) {
        compilationService.deleteById(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compilationId,
            @Valid @RequestBody UpdateCompilationRequest request
    ) {
        return compilationService.updateCompilation(compilationId, request);
    }
}

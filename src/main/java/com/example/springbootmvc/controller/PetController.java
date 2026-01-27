package com.example.springbootmvc.controller;

import com.example.springbootmvc.dto.PetDto;
import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class PetController {
    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/pet")
    public ResponseEntity<PetDto> createPet(@Valid @RequestBody PetDto userDto) {
        Pet pet = petService.createPet(PetDto.toPet(userDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PetDto.toPetDto(pet));
    }

    @PutMapping("/pet/{petId}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long petId, @Valid @RequestBody PetDto userDto) {
        Pet pet = petService.updatePet(petId, PetDto.toPet(userDto));

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(PetDto.toPetDto(pet));
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<PetDto> getPet(@PathVariable Long petId) {
        Pet pet = petService.getPet(petId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PetDto.toPetDto(pet));
    }
}

package com.example.springbootmvc.service;

import com.example.springbootmvc.exceptions.EntityNotFound;
import com.example.springbootmvc.exceptions.PetNotFoundException;
import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private RepoService repoService;
    private Long newPetId = 1L;

    public PetService(RepoService repoService) {
        this.repoService = repoService;
    }

    public Pet createPet(Pet pet) {
        User user = repoService.getUsers().get(pet.getUserId());
        if (user == null) {
            throw new PetNotFoundException("Создание Питомца невозможно, так указанный для питомца Пользователь с userId=%s не существует".formatted(pet.getUserId()));
        }

        pet.setId(newPetId++);
        repoService.getPets().put(pet.getId(), pet);

        user.getPets().add(pet);

        return pet;
    }

    public Pet updatePet(Long petId, Pet updatePet) {
        Pet pet = findPet(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Обновление Питомца невозможно, так питомец с id=%s не существует".formatted(petId));
        }

        User user = repoService.getUsers().get(pet.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("Обновление Питомца невозможно, так указанный для питомца Пользователь с userId=%s не существует".formatted(pet.getUserId()));
        }

        user.getPets().remove(pet);

        pet.setName(updatePet.getName());
        pet.setUserId(updatePet.getUserId());

        user.getPets().add(pet);

        return pet;
    }

    public void deletePet(Long petId) {
        Pet pet = findPet(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Удаление Питомца невозможно, так питомец с id=%s не существует".formatted(petId));
        }

        User user = repoService.getUsers().get(pet.getUserId());
        if (user != null) {
            user.getPets().remove(pet);
        }

        repoService.getPets().remove(petId);
    }

    public Pet getPet(Long petId) {
        Pet pet = findPet(petId);
        if (pet == null) {
            throw new EntityNotFound("Питомец с id=%s не найден".formatted(petId));
        }

        return pet;
    }

    public Pet findPet(Long petId) {
        return repoService.getPets().get(petId);
    }
}

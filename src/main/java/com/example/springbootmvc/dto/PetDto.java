package com.example.springbootmvc.dto;

import com.example.springbootmvc.model.Pet;
import jakarta.validation.constraints.*;

public class PetDto {
    @Null(message = "Не надо указывать id Питомца при создании. Вы его не знаете")
    private Long id;
    @Size(min = 1, max = 256, message = "Требуется указать Имя питомца (не длиннее 256 символов)")
    private String name;
    @Min(value = 1, message = "Не может быть id Пользователя меньше 1")
    @Max(Long.MAX_VALUE)
    @NotNull(message = "Необходимо указать id Пользователя Питомца")
    private Long userId;

    public PetDto(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public static PetDto toPetDto(Pet pet) {
        return new PetDto(pet.getId(), pet.getName(), pet.getUserId());
    }

    public static Pet toPet(PetDto petDto) {
        return new Pet(petDto.getId(), petDto.getName(), petDto.getUserId());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

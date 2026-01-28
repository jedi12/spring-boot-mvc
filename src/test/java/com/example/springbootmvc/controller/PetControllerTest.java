package com.example.springbootmvc.controller;

import com.example.springbootmvc.dto.PetDto;
import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.model.User;
import com.example.springbootmvc.service.PetService;
import com.example.springbootmvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @BeforeEach
    void init() {
        mapper = JsonMapper.builder()
                .disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)
                .build();
    }

    @Test
    void createPetSuccess() throws Exception {
        User user = new User(null, "Петров Петр Петрович", "petrov@mail.server", 19);
        user = userService.createUser(user);

        PetDto petDto = new PetDto(null, "Собака Мухтар", user.getId());
        String petJson = mapper.writeValueAsString(petDto);

        String createdUserJson = mockMvc.perform(
                        post("/api/v1/pet").contentType(MediaType.APPLICATION_JSON).content(petJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet createdPet = mapper.readValue(createdUserJson, Pet.class);

        Assertions.assertNotNull(createdPet.getId());
        Assertions.assertEquals(createdPet.getName(), petDto.getName());
        Assertions.assertEquals(createdPet.getUserId(), petDto.getUserId());
    }

    @Test
    void deletePetSuccess() throws Exception {
        User user = new User(null, "Сидоров Сидр Сидорович", "sidorov@mail.server", 34);
        user = userService.createUser(user);

        Pet pet = new Pet(null, "Собака Мухтар", user.getId());
        pet = petService.createPet(pet);

        mockMvc.perform(
                delete("/api/v1/pet/{petId}", pet.getId()))
                .andExpect(status().is(204));
    }
}
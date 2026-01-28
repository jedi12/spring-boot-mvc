package com.example.springbootmvc.controller;

import com.example.springbootmvc.dto.UserDto;
import com.example.springbootmvc.model.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        mapper = JsonMapper.builder()
                .disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)
                .build();
    }

    @Test
    void createUserSuccess() throws Exception {
        UserDto userDto = new UserDto(null, "Иванов Иван Иванович", "ivanov@mail.server", 25, null);
        String userJson = mapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(
                post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUserDto = mapper.readValue(createdUserJson, UserDto.class);

        Assertions.assertNotNull(createdUserDto.getId());
        Assertions.assertEquals(createdUserDto.getName(), userDto.getName());
        Assertions.assertEquals(createdUserDto.getEmail(), userDto.getEmail());
        Assertions.assertEquals(createdUserDto.getAge(), userDto.getAge());
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User(null, "Иванов Иван Иванович", "ivanov@mail.server", 25);
        user = userService.createUser(user);
        UserDto userDto = UserDto.toUserDto(user);

        String foundUserJson = mockMvc.perform(get("/api/v1/user/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundUserDto = mapper.readValue(foundUserJson, UserDto.class);

        Assertions.assertEquals(foundUserDto.getId(), userDto.getId());
        Assertions.assertEquals(foundUserDto.getName(), userDto.getName());
        Assertions.assertEquals(foundUserDto.getEmail(), userDto.getEmail());
        Assertions.assertEquals(foundUserDto.getAge(), userDto.getAge());
        Assertions.assertEquals(foundUserDto.getPets(), userDto.getPets());
    }
}
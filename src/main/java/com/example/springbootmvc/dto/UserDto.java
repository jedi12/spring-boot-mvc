package com.example.springbootmvc.dto;

import com.example.springbootmvc.model.User;
import jakarta.validation.constraints.*;

import java.util.List;

public class UserDto {
    @Null(message = "Не надо указывать id Пользователя при создании. Вы его не знаете")
    private Long id;
    @Size(min = 1, max = 256, message = "Требуется указать Имя пользователя (не длиннее 256 символов)")
    private String name;
    @Email
    @Size(max = 256)
    private String email;
    @Min(value = 1, message = "Младенцы до одного года не могут заводить себе собак. Укажите правильный возраст")
    @Max(value = 150, message = "Ни один человек еще не прожил больше 150 лет. Укажите правильный возраст")
    private Integer age;
    @Null(message = "Для изменения информации о питомцах Пользователя используйте другой 'endpoint'")
    private List<PetDto> pets;

    public UserDto() {}

    public UserDto(Long id, String name, String email, Integer age, List<PetDto> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getPets().stream().map(PetDto::toPetDto).toList());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail(), userDto.getAge());
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public List<PetDto> getPets() {
        return pets;
    }
    public void setPets(List<PetDto> pets) {
        this.pets = pets;
    }
}

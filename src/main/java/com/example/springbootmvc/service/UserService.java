package com.example.springbootmvc.service;

import com.example.springbootmvc.exceptions.EntityNotFound;
import com.example.springbootmvc.exceptions.UserNotFoundException;
import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private RepoService repoService;
    private Long newUserId = 1L;

    public UserService(RepoService repoService) {
        this.repoService = repoService;
    }

    public User createUser(User user) {
        user.setId(newUserId++);
        repoService.getUsers().put(user.getId(), user);
        return user;
    }

    public User updateUser(Long userId, User updateUser) {
        User user = findUser(userId);
        if (user == null) {
            throw new UserNotFoundException("Обновление пользователя невозможно, так пользователь с id=%s не существует".formatted(userId));
        }

        user.setName(updateUser.getName());
        user.setEmail(updateUser.getEmail());
        user.setAge(updateUser.getAge());

        return user;
    }

    public void deleteUser(Long userId) {
        User user = findUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Удаление пользователя невозможно, так пользователь с id=%s не существует".formatted(userId));
        }

        for (Pet pet: user.getPets()) {
            repoService.getPets().remove(pet.getId());
        }

        repoService.getUsers().remove(userId);
    }

    public User getUser(Long userId) {
        User user = findUser(userId);
        if (user == null) {
            throw new EntityNotFound("Пользователь с id=%s не найден".formatted(userId));
        }

        return user;
    }

    public User findUser(Long userId) {
        return repoService.getUsers().get(userId);
    }
}

package com.example.springbootmvc.service;

import com.example.springbootmvc.exceptions.EntityNotFound;
import com.example.springbootmvc.exceptions.UserNotFoundException;
import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final String USER_PREF = "User: ";

    private final RepoService repoService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper mapper;
    private Long newUserId = 1L;

    public UserService(RepoService repoService, StringRedisTemplate stringRedisTemplate, ObjectMapper mapper) {
        this.repoService = repoService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.mapper = mapper;
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
        try {
            String userStr = stringRedisTemplate.opsForValue().get(USER_PREF + userId);
            if (userStr != null) {
                log.info("Пользователь userId={} взят из кеша", userId);
                return mapper.readValue(userStr, User.class);
            }
            log.info("Пользователь userId={} отсутствует в кеше", userId);
        } catch (Exception e) {
            log.error("Ошибка при работе с кешем", e);
        }

        User user = findUser(userId);
        if (user == null) {
            throw new EntityNotFound("Пользователь с id=%s не найден".formatted(userId));
        }
        log.info("Пользователь userId={} получен из базы", userId);

        try {
            String userStr = mapper.writeValueAsString(user);
            stringRedisTemplate.opsForValue().set(USER_PREF + userId, userStr, 5, TimeUnit.MINUTES);
            log.info("Пользователь userId={} передан в кеш", userId);
        } catch (Exception e) {
            log.error("Ошибка при работе с кешем", e);
        }

        return user;
    }

    public User findUser(Long userId) {
        return repoService.getUsers().get(userId);
    }
}

package com.example.springbootmvc.service;

import com.example.springbootmvc.model.Pet;
import com.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class RepoService {
    private Map<Long, User> users = new TreeMap<>();
    private Map<Long, Pet> pets = new HashMap<>();

    public Map<Long, User> getUsers() {
        return users;
    }

    public Map<Long, Pet> getPets() {
        return pets;
    }
}
package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepositories;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepositories roleRepositories;

    public RoleServiceImpl(RoleRepositories roleRepositories) {
        this.roleRepositories = roleRepositories;
    }
    @Override
    public List<Role> findAll() {
        return roleRepositories.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepositories.findAll();
    }
}
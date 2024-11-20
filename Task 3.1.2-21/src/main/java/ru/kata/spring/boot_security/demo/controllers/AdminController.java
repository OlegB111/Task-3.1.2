package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userService;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/page")
    public String adminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userServiceImpl.getUserByLogin(userDetails.getUsername());
        model.addAttribute("principal", user);
        model.addAttribute("users", userServiceImpl.getAllUsers());
        model.addAttribute("roles", roleServiceImpl.findAll());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping
    public String addCreateNewUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rolesAdd", roleServiceImpl.getRoles());
            return "new";
        }

        try {
            userServiceImpl.saveUser(user);
        } catch (Exception e) {
            bindingResult.rejectValue("email", "error.user", "Пользователь с таким логином уже существует");
            model.addAttribute("rolesAdd", roleServiceImpl.getRoles());
            return "new";
        }

        return "redirect:/admin/page";
    }

    @GetMapping("/redactor/{id}")
    public String patchAdminRedactor(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userServiceImpl.findOne(id));
        model.addAttribute("rolesAdd", roleServiceImpl.getRoles());
        return "edit";
    }

    @PostMapping("/redactor/{id}")
    public String updateUser(Model model, @PathVariable("id") Long id, @Valid @ModelAttribute("user") User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rolesAdd", roleServiceImpl.getRoles()
            );
            return "edit";
        }
            User existingUser = userServiceImpl.getUserById(id).orElse(null);

            if (existingUser != null) {
                existingUser.setName(updatedUser.getName());
                existingUser.setAge(updatedUser.getAge());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setRoles(updatedUser.getRoles());

                userServiceImpl.saveUser(existingUser);
            }

            return "redirect:/admin/page";
        }

    @PostMapping("/delete/{id}")
    public String adminDelete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        userServiceImpl.delete(id);
        return "redirect:/admin/page";
    }

    @GetMapping("/addUser")
    public String addNewUser(Model model, @ModelAttribute("user") User user) {
        List<Role> roles = roleServiceImpl.getRoles();
        model.addAttribute("rolesAdd", roles);
        return "new";
    }


}

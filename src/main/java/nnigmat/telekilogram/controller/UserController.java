package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String listUser(Model model) {
        Iterable<User> users = userRepo.findAll();

        model.addAttribute("users", users);

        return "user_list";
    }
}

package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String listUser(@AuthenticationPrincipal User user, Model model) {
        Iterable<User> users = userRepo.findAll();

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("Role", Role.class);

        return "user_list";
    }

    @PostMapping("/give_ban/{user}")
    public String ban(@PathVariable User user){
        Set<Role> ban = new HashSet<Role>();
        if (user.getAuthorities().contains(Role.BAN)) {
            ban.add(Role.USER);
        } else {
            ban.add(Role.BAN);
        }
        user.setRoles(ban);
        userRepo.save(user);

        return "redirect:/user";
    }

    @PostMapping("/give_moderator/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String moderator(@PathVariable User user){
        Set<Role> ban = new HashSet<Role>();
        if (user.getAuthorities().contains(Role.MODERATOR)) {
            ban.add(Role.USER);
        } else {
            ban.add(Role.MODERATOR);
        }
        user.setRoles(ban);
        userRepo.save(user);

        return "redirect:/user";
    }
}

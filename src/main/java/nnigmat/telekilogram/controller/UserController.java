package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.UserService;
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
    private UserService userService;

    @GetMapping
    public String listUser(@AuthenticationPrincipal User user, Model model) {
        Iterable<User> users = userService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("Role", Role.class);

        return "user_list";
    }

    @PostMapping("/give_ban/{user}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String ban(@AuthenticationPrincipal User author, @PathVariable User user){
        if (!author.equals(user) && !user.isAdmin()) {
            userService.globalBan(user);
        }
        return "redirect:/user";
    }

    @PostMapping("/give_moderator/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String moderator(@AuthenticationPrincipal User author, @PathVariable User user){
        if (!author.equals(user) && !user.isAdmin()) {
            userService.makeGlobalModerator(user);
        }
        return "redirect:/user";
    }
}

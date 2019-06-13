package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/list")
    public String listRoom(@AuthenticationPrincipal User user, Model model) {
        Iterable<Room> rooms = user.getRooms();

        model.addAttribute("user", user);
        model.addAttribute("rooms", rooms);
        model.addAttribute("Role", Role.class);

        return "room_list";
    }

    @PostMapping("/connect/{room}")
    public String connect(@AuthenticationPrincipal User user, @PathVariable Room room) {
        if (room.hasMember(user)) user.setCurrentRoom(room);
        userRepo.save(user);
        return "redirect:/room";
    }
}

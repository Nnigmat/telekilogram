package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomController {

    @GetMapping("/list")
    public String listRoom(@AuthenticationPrincipal User user, Model model) {
        Iterable<Room> rooms = user.getRooms();

        model.addAttribute("user", user);
        model.addAttribute("rooms", rooms);
        model.addAttribute("Role", Role.class);

        return "room_list";
    }
}

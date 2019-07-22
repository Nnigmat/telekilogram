package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Optional;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    public String listRoom(@AuthenticationPrincipal User user, Model model) {
        Iterable<Room> rooms = userService.getUserRooms(user);

        model.addAttribute("user", user);
        model.addAttribute("rooms", rooms);
        model.addAttribute("Role", Role.class);

        return "room_list";
    }

    @PostMapping("/connect/{room}")
    public String connect(@AuthenticationPrincipal User user, @PathVariable Room room) {
        if(user != null && room != null) {
            if (room.hasMember(user)) {
                user.setCurrentRoom(room);
                userService.save(user);
            }
        }

        return "redirect:/room";
    }

    @PostMapping("/delete/{room}")
    public String delete(@AuthenticationPrincipal User user, @PathVariable Room room) {
        if (user != null && room != null) {
            if (room.isCreator(user)) {
                roomService.delete(room);

                Optional<Room> defaultRoom = roomService.getDefaultRoom();
                defaultRoom.ifPresent(user::setCurrentRoom);
                userService.save(user);
            }
        }

        return "redirect:/room";
    }
}

package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import nnigmat.telekilogram.service.tos.RoomTO;
import nnigmat.telekilogram.service.tos.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    public String listRoom(@AuthenticationPrincipal UserTO user, Model model) {
        Collection<RoomTO> rooms = userService.getUserRoomsById(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("rooms", rooms);
        model.addAttribute("Role", Role.class);

        return "room_list";
    }

    @PostMapping("/connect/{room}")
    public String connect(@AuthenticationPrincipal UserTO user, @PathVariable RoomTO room) {
        if(user != null && room != null) {
            if (room.hasMember(user)) {
                user.setCurrentRoomId(room.getId());
                userService.save(user);
            }
        }

        return "redirect:/room";
    }

    @PostMapping("/delete/{room}")
    public String delete(@AuthenticationPrincipal UserTO user, @PathVariable RoomTO room) {
        if (user != null && room != null && room.isCreator(user)) {
            // Get members that connected right now to the room and default room (id = 0)
            Collection<UserTO> connectedMembers = roomService.getConnectedMembersById(room.getId());
            RoomTO defaultRoom = roomService.getDefaultRoom();
            if (defaultRoom != null) {
                // Relocate member to default room
                for (UserTO member : connectedMembers) {
                    member.setCurrentRoomId(defaultRoom.getId());
                }

                userService.save(user);
                roomService.delete(room);
            }
        }

        return "redirect:/room";
    }
}

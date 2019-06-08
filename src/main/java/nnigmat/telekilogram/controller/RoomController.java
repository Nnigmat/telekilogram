package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping
    public String listMessages(Principal principal, Model model) {
        User userFromDb = userRepo.findByUsername(principal.getName());
        Room roomFromDb = userFromDb.getCurrent_room();

        Iterable<Message> messages = messageRepo.findByRoom(roomFromDb);
        model.addAttribute("messages", messages);
        model.addAttribute("user", userFromDb);

        return "room";
    }
}

package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.event.MouseEvent;
import java.security.Principal;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping
    public String listMessages(@AuthenticationPrincipal User user, Model model) {
        Room roomFromDb = user.getCurrentRoom();

        Iterable<Message> messages = messageRepo.findByRoom(roomFromDb);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        model.addAttribute("room", roomFromDb);

        return "room";
    }

    @PostMapping
    public void addMessage(Principal principal) {

    }
}

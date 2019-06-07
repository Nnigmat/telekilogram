package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    /** Return the list of messages */
    @GetMapping
    public String list(Model model) {
        Iterable<Message> messages = messageRepo.findAll();

        model.addAttribute("messages", messages);

        return "main";
    }

    /** Add new message
     *  After redirects to the list of messages */
    @PostMapping
    public String addMessage(
            @RequestParam(required = true, defaultValue = "") String user,
            @RequestParam(required = true, defaultValue = "") String text
    ) {
        Message newMessage = new Message(user, text);

        messageRepo.save(newMessage);

        return "redirect:/room";
    }
}

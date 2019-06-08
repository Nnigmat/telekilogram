package nnigmat.telekilogram.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/room")
public class RoomController {

    @GetMapping
    public String listMessages(Principal principal, Model model) {

        return "room";
    }
}

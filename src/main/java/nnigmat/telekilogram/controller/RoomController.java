package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.event.MouseEvent;
import java.security.Principal;
import java.util.Optional;

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
        model.addAttribute("Role", Role.class);

        return "room";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'MODERATOR')")
    public String addMessage(@AuthenticationPrincipal User user, @RequestParam String text) {
        Message message = new Message(text, user.getCurrentRoom(), user);

        messageRepo.save(message);

        return "redirect:/room";
    }

    @PostMapping("/{roomId}/deleteMessage/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String deleteMessage(@PathVariable Long roomId, @PathVariable Long messageId) {
        Optional<Message> messageFromDb = messageRepo.findById(messageId);
        messageFromDb.ifPresent(message -> messageRepo.delete(message));
        return "redirect:/room";
    }
}

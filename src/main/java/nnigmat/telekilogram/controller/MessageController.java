package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static nnigmat.telekilogram.controller.CommandsController.executeCommand;

@Controller
public class MessageController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private RoomRepo roomRepo;

    @GetMapping("/room")
    public String listMessages(@AuthenticationPrincipal User user, Model model) {
        Room roomFromDb = user.getCurrentRoom();

        Iterable<Message> messages = messageRepo.findByRoom(roomFromDb);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        model.addAttribute("room", roomFromDb);
        model.addAttribute("Role", Role.class);

        return "main";
    }

    @PostMapping("/room")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'MODERATOR')")
    public String addMessage(@AuthenticationPrincipal User user, @RequestParam String text) {
        Checker checker = new Checker(text);
        if (checker.isCommand()) {
            String commandName = checker.checkCommand();
            System.out.println(commandName);
        } else if (!checker.isEmpty()) {
            Message message = new Message(text, user.getCurrentRoom(), user);
            messageRepo.save(message);
        }
        return "redirect:/room";
    }

    @PostMapping("/deleteMessage/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String deleteMessage(@PathVariable Long messageId) {
        Optional<Message> messageFromDb = messageRepo.findById(messageId);
        messageFromDb.ifPresent(message -> messageRepo.delete(message));
        return "redirect:/room";
    }
}

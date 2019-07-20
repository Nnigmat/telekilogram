package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.MessageService;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MessageController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/room")
    public String listMessages(@AuthenticationPrincipal User user, Model model) {
        Room roomFromDb = userService.getUserCurrentRoom(user);

        Iterable<Message> messages = messageService.findMessagesByRoom(roomFromDb);
        model.addAttribute("members", roomFromDb.getMembers());
        model.addAttribute("moderators", roomFromDb.getModerators());
        model.addAttribute("admins", roomFromDb.getAdmins());
        model.addAttribute("canDeleteMessage", roomService.isCreator(roomFromDb, user) || roomService.isModerator(roomFromDb, user) || roomService.isAdmin(roomFromDb, user));
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
            CommandExecutor executor = new CommandExecutor(text, commandName, user, roomService, userService);
            executor.execute();
        } else if (!checker.isEmpty()) {
            Message message = new Message(text, user.getCurrentRoom(), user);
            messageService.save(message);
        }
        return "redirect:/room";
    }

    @PostMapping("/deleteMessage/{messageId}")
    public String deleteMessage(@AuthenticationPrincipal User user, @PathVariable Long messageId) {
        Room currentRoom = user.getCurrentRoom();
        if (roomService.isCreator(currentRoom, user) || roomService.isModerator(currentRoom, user) || roomService.isAdmin(currentRoom, user)) {
            messageService.deleteById(messageId);
        }
        return "redirect:/room";
    }
}

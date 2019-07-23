package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.service.MessageService;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import nnigmat.telekilogram.service.tos.MessageTO;
import nnigmat.telekilogram.service.tos.RoomTO;
import nnigmat.telekilogram.service.tos.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MessageController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/room")
    public String listMessages(@AuthenticationPrincipal UserTO user, Model model) {
        RoomTO currentRoom = roomService.findById(user.getCurrentRoomId());

        Iterable<MessageTO> messages = messageService.findMessagesByRoom(currentRoom);
        model.addAttribute("members", currentRoom.getMembers());
        model.addAttribute("moderators", currentRoom.getModerators());
        model.addAttribute("admins", currentRoom.getAdmins());
        model.addAttribute("canDeleteMessage", roomService.isCreator(currentRoom, user) || roomService.isModerator(currentRoom, user) || roomService.isAdmin(currentRoom, user));
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        model.addAttribute("room", currentRoom);
        model.addAttribute("Role", Role.class);

        return "main";
    }

    @PostMapping("/room")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'MODERATOR')")
    public String addMessage(@AuthenticationPrincipal UserTO user, @RequestParam String text) {
        Checker checker = new Checker(text);
        if (checker.isCommand()) {
            String commandName = checker.checkCommand();
            CommandExecutor executor = new CommandExecutor(text, commandName, user);
            executor.execute();
        } else if (!checker.isEmpty()) {
            RoomTO room = roomService.findById(user.getCurrentRoomId());
            MessageTO message = new MessageTO(user, room, text);
            messageService.save(message);
        }

        return "redirect:/room";
    }

    @PostMapping("/deleteMessage/{messageId}")
    public String deleteMessage(@AuthenticationPrincipal UserTO user, @PathVariable Long messageId) {
        RoomTO currentRoom = roomService.findById(user.getCurrentRoomId());
        if (roomService.isCreator(currentRoom, user) || roomService.isModerator(currentRoom, user) || roomService.isAdmin(currentRoom, user)) {
            messageService.deleteById(messageId);
        }

        return "redirect:/room";
    }
}

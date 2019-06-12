package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.stereotype.Controller;

import java.util.regex.Pattern;

@Controller
public class CommandsController {

    private static RoomRepo roomRepo;
    private static UserRepo userRepo;

    public static String executeCommand(String command, User user, RoomRepo roomRepo1, UserRepo userRepo1) {
        roomRepo = roomRepo1;
        userRepo = userRepo1;

        command = command.trim();
        String[] temp = command.split(" ");

        if (command.equals("//help")) {
            return "help";
        }

        String res = "";
        switch (temp[0]) {
            case ("//room"):
                res = roomCommands(command, user);
                break;
            case ("//user"):
                res = userCommands(temp);
                break;
            default:
                res = "No such command";
        }

        return res;
    }

    private static String roomCommands(String command, User user) {

        Pattern pattern = Pattern.compile("//room rename \"[^\"]+\"");
        if (pattern.matcher(command).matches() && (user.getCurrentRoom().getCreator().equals(user) || user.isAdmin())) {
            String newName = getName(command);
            rename(newName, user);
        }

        pattern = Pattern.compile("//room remove \"[^\"]+\"");
        if (pattern.matcher(command).matches() && (user.getCurrentRoom().getCreator().equals(user) || user.isAdmin())) {
            String name = getName(command);
            remove(name, user);
        }

        pattern = Pattern.compile("//room connect \"[^\"]+\"");
        if (pattern.matcher(command).matches()) {
            String name = getName(command);
            connect(name, user);
        }

        pattern = Pattern.compile("//room connect \"[^\"]+\" -l \"[^\"\\s]+\"");
        if (pattern.matcher(command).matches()) {
            String name = getName(command);
        }

        return "redirect:/room";
    }

    private static String getName(String command) {
        int start = command.indexOf("\"");
        int end = command.lastIndexOf("\"");
        return command.substring(start+1, end);
    }

    private static void connect(String name, User user) {
        Room room = roomRepo.findByName(name);
        if (room != null && user.hasAccess(room)) {
            user.setCurrentRoom(room);
        }
    }

    private static void remove(String name, User user) {
        Room room = roomRepo.findByName(name);
        if (room != null && room.getId() != 1)
            roomRepo.delete(room);
    }

    private static void rename(String newName, User user) {
        Room room = user.getCurrentRoom();
        room.setName(newName);
        roomRepo.save(room);
    }

    private static String userCommands(String[] command) {
        switch (command[1]) {
            case ("rename"):
            case ("ban"):
            case ("moderator"):
            default:
        }
        return "";
    }
}

package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;

public class CommandExecutor {

    private String command;
    private String commandName;
    private User user;
    private RoomRepo roomRepo;
    private UserRepo userRepo;

    public CommandExecutor(String command, String commandName, User user, RoomRepo roomRepo, UserRepo userRepo) {
        this.command = command;
        this.commandName = commandName;
        this.user = user;
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
    }

    public void execute() {
        switch (this.commandName) {
            case ("help"): break;
            case ("room_public_create"): this.room_public_create();
                                        break;
            case ("room_private_create"): this.room_private_create();
                                        break;
            case ("room_remove"): this.room_remove();
                                        break;
            case ("room_rename"): this.room_rename();
                                        break;
            case ("room_self_connect"): this.room_self_connect();
                                        break;
            case ("room_user_connect"): this.room_user_connect();
                                        break;
            case ("room_disconnect"): this.room_disconnect();
                                        break;
            case ("room_specified_disconnect"): this.room_specified_disconnect();
                                        break;
            case ("room_user_disconnect"): this.room_user_disconnect();
                                        break;
            case ("room_user_with_time_disconnect"): this.room_user_with_time_disconnect();
                                        break;
            case ("user_rename"): this.user_rename();
                                        break;
            case ("user_ban"): this.user_ban();
                                        break;
            case ("user_make_moderator"): this.user_make_moderator();
                                        break;
            case ("user_unmake_moderator"): this.user_unmake_moderator();
                                        break;
        }
    }

    public Pair<String, Integer> parse(int startPos) {
        int start = command.indexOf("\"", startPos);
        int end = command.indexOf("\"", start+1);
        return Pair.of((String)command.substring(start+1, end),(Integer)end+1);
    }

    private void room_public_create() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();
        Room room = roomRepo.findByName(roomName);
        if (room == null && !this.user.isBanned()) {
            Room newRoom = new Room(roomName, this.user, false);
            newRoom.addMemeber(user);
            roomRepo.save(newRoom);
        }
    }

    private void room_private_create() {
    }

    private void room_remove() {
    }

    private void room_rename() {
    }

    private void room_self_connect() {
    }

    private void room_user_connect() {
    }

    private void room_disconnect() {
    }

    private void room_specified_disconnect() {
    }

    private void room_user_disconnect() {
    }

    private void room_user_with_time_disconnect() {
    }

    private void user_rename() {
    }

    private void user_ban() {
    }

    private void user_make_moderator() {
    }

    private void user_unmake_moderator() {
    }

    public String getCommand() {
        return this.command;
    }

//    private static String roomCommands(String command, User user) {
//
//        Pattern pattern = Pattern.compile("//room rename \"[^\"]+\"");
//        if (pattern.matcher(command).matches() && (user.getCurrentRoom().getCreator().equals(user) || user.isAdmin())) {
//            String newName = getName(command);
//            rename(newName, user);
//        }
//
//        pattern = Pattern.compile("//room remove \"[^\"]+\"");
//        if (pattern.matcher(command).matches() && (user.getCurrentRoom().getCreator().equals(user) || user.isAdmin())) {
//            String name = getName(command);
//            remove(name, user);
//        }
//
//        pattern = Pattern.compile("//room connect \"[^\"]+\"");
//        if (pattern.matcher(command).matches()) {
//            String name = getName(command);
//            connect(name, user);
//        }
//
//        pattern = Pattern.compile("//room connect \"[^\"]+\" -l \"[^\"\\s]+\"");
//        if (pattern.matcher(command).matches()) {
//            String name = getName(command);
//        }
//
//        return "redirect:/room";
//    }
//
//    private static String getName(String command) {
//        int start = command.indexOf("\"");
//        int end = command.lastIndexOf("\"");
//        return command.substring(start+1, end);
//    }
//
//    private static void connect(String name, User user) {
//        Room room = roomRepo.findByName(name);
//        if (room != null && user.hasAccess(room)) {
//            user.setCurrentRoom(room);
//        }
//    }
//
//    private static void remove(String name, User user) {
//        Room room = roomRepo.findByName(name);
//        if (room != null && room.getId() != 1)
//            roomRepo.delete(room);
//    }
//
//    private static void rename(String newName, User user) {
//        Room room = user.getCurrentRoom();
//        room.setName(newName);
//        roomRepo.save(room);
//    }
//
//    private static String userCommands(String[] command) {
//        switch (command[1]) {
//            case ("rename"):
//            case ("ban"):
//            case ("moderator"):
//            default:
//        }
//        return "";
//    }
}

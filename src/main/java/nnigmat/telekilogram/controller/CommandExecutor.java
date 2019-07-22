package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CommandExecutor {

    private String command;
    private String commandName;
    private User user;
    private RoomService roomService;
    private UserService userService;

    public CommandExecutor(String command, String commandName, User user, RoomService roomService, UserService userService) {
        this.command = command;
        this.commandName = commandName;
        this.user = user;
        this.roomService = roomService;
        this.userService = userService;
    }

    public void execute() {
        switch (this.commandName) {
            case (""): break;
            case ("help"): break;
            case ("room_public_create"): this.roomCreate(false);
                                        break;
            case ("room_private_create"): this.roomCreate(true);
                                        break;
            case ("room_remove"): this.roomRemove();
                                        break;
            case ("room_rename"): this.roomRename();
                                        break;
            case ("room_self_connect"): this.roomSelfConnect();
                                        break;
            case ("room_user_connect"): this.roomUserConnect();
                                        break;
            case ("room_disconnect"): this.roomDisconnect();
                                        break;
            case ("room_specified_disconnect"): this.roomSpecifiedDisconnect();
                                        break;
            case ("room_user_disconnect"): this.roomUserDisconnect();
                                        break;
            case ("room_user_with_time_disconnect"): this.roomUserWithTimeDisconnect();
                                        break;
            case ("user_rename"): this.userRename();
                                        break;
            case ("user_ban"): this.userBan();
                                        break;
            case ("user_make_moderator"): this.userModerator(true);
                                        break;
            case ("user_unmake_moderator"): this.userModerator(false);
                                        break;
        }
    }

    public Pair<String, Integer> parse(int startPos) {
        int start = command.indexOf("\"", startPos);
        int end = command.indexOf("\"", start+1);
        return Pair.of((String)command.substring(start+1, end),(Integer)end+1);
    }

    private void roomCreate(boolean closed) {
        // Take the name of the new room
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        // Find room. If it exists we can't create the new room
        List<Room> rooms = roomService.findByName(roomName);
        if (rooms.isEmpty() && !this.user.isBanned()) {
            Room newRoom = new Room(roomName, this.user, closed);
            newRoom.addMember(user);
            roomService.save(newRoom);
        }
    }

    private void roomRemove() {
        // Doesn't work properly
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomService.findByName(roomName);
        for (Room room : rooms) {
            if (room != null && room.getId() != 1 && (user.isAdmin() || room.isCreator(user))) {
                for (User u : room.getMembers()) {
                    room.removeMember(u);
                }
                roomService.delete(room);
            }
        }
    }

    private void roomRename() {
        Pair<String, Integer> pair = parse(0);
        String newRoomName = pair.getFirst();

        Room room = user.getCurrentRoom();
        if (room != null && (user.isAdmin() || room.isCreator(user))) {
            room.setName(newRoomName);
            userService.save(user);
            roomService.save(room);
        }
    }

    private void roomSelfConnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomService.findByName(roomName);
        for (Room room : rooms) {
            if (room.hasMember(user)) {
                user.setCurrentRoom(room);
                userService.save(user);
                break;
            }
        }
    }

    private void roomUserConnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String userName = pair1.getFirst();

        User usr = userService.findByUsername(userName);
        List<Room> rooms = roomService.findByName(roomName);
        for (Room room : rooms) {
            if (room.isCreator(user) || roomService.isAdmin(room, user) || roomService.isModerator(room, user)) {
                room.addMember(usr);
                userService.save(usr);
                break;
            }
        }
    }

    private void roomDisconnect() {
        Room room = user.getCurrentRoom();
        room.removeMember(user);
        user.removeRoom(room);
        Optional<Room> startRoom = roomService.findById((long) 1);
        startRoom.ifPresent(value -> user.setCurrentRoom(value));

        if (room.getMembers().size() == 0) {
            roomService.delete(room);
        }

        roomService.save(room);
        userService.save(user);
    }

    private void roomSpecifiedDisconnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomService.findByName(roomName);
        for (Room room : rooms) {
            if (room.hasMember(user)) {
                if (user.getCurrentRoom().equals(room)) {
                    Optional<Room> startRoom = roomService.findById((long) 1);
                    startRoom.ifPresent(value -> user.setCurrentRoom(value));
                }
                room.removeMember(user);
                roomService.save(room);
                userService.save(user);
            }
        }
    }

    private void roomUserDisconnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String userName = pair1.getFirst();

        List<Room> rooms = roomService.findByName(roomName);
        User usr = userService.findByUsername(userName);
        for (Room room : rooms) {
            if (room.hasMember(usr) && (user.isModerator() || user.isAdmin() || room.isCreator(user))) {
                room.removeMember(usr);
                roomService.save(room);
                userService.save(usr);
            }
        }
    }

    private void roomUserWithTimeDisconnect() {
    }

    private void userRename() {
        Pair<String, Integer> pair = parse(0);
        String userName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String newUserName = pair1.getFirst();

        User usr = userService.findByUsername(userName);
        User otherUsr = userService.findByUsername(newUserName);
        if (usr != null && otherUsr == null) {
            usr.setUsername(newUserName);
            userService.save(usr);
        }
    }

    private void userBan() {
    }

    private void userModerator(boolean make) {
        Pair<String, Integer> pair = parse(0);
        String userName = pair.getFirst();

        User usr = userService.findByUsername(userName);
        if (usr != null && user.isAdmin()) {
            if (make) {
                usr.getRoles().add(Role.MODERATOR);
            } else {
                usr.getRoles().add(Role.USER);
            }
            userService.save(usr);
        }
    }

    public String getCommand() {
        return this.command;
    }
}

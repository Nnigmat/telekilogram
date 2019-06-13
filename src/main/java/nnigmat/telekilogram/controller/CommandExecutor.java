package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.data.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            case (""): break;
            case ("help"): break;
            case ("room_public_create"): this.room_create(false);
                                        break;
            case ("room_private_create"): this.room_create(true);
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
            case ("user_make_moderator"): this.user_moderator(true);
                                        break;
            case ("user_unmake_moderator"): this.user_moderator(false);
                                        break;
        }
    }

    public Pair<String, Integer> parse(int startPos) {
        int start = command.indexOf("\"", startPos);
        int end = command.indexOf("\"", start+1);
        return Pair.of((String)command.substring(start+1, end),(Integer)end+1);
    }

    private void room_create(boolean closed) {
        // Take the name of the new room
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        // Find room. If it exists we can't create the new room
        List<Room> rooms = roomRepo.findByName(roomName);
        if (rooms.isEmpty() && !this.user.isBanned()) {
            Room newRoom = new Room(roomName, this.user, closed);
            newRoom.addMember(user);
            roomRepo.save(newRoom);
        }
    }

    private void room_remove() {
        // Doesn't work properly
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomRepo.findByName(roomName);
        for (Room room : rooms) {
            if (room != null && room.getId() != 1 && (user.isAdmin() || room.isCreator(user))) {
                for (User u : room.getMembers()) {
                    room.removeMember(u);
                }
                roomRepo.delete(room);
            }
        }
    }

    private void room_rename() {
        Pair<String, Integer> pair = parse(0);
        String newRoomName = pair.getFirst();

        Room room = user.getCurrentRoom();
        if (room != null && (user.isAdmin() || room.isCreator(user))) {
            room.setName(newRoomName);
            userRepo.save(user);
            roomRepo.save(room);
        }
    }

    private void room_self_connect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomRepo.findByName(roomName);
        for (Room room : rooms) {
            if (room.hasMember(user)) {
                user.setCurrentRoom(room);
                userRepo.save(user);
                break;
            }
        }
    }

    private void room_user_connect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String userName = pair1.getFirst();

        User usr = userRepo.findByUsername(userName);
        List<Room> rooms = roomRepo.findByName(roomName);
        for (Room room : rooms) {
            if (room.isCreator(user) || user.isAdmin() || user.isModerator()) {
                room.addMember(usr);
                userRepo.save(usr);
                roomRepo.save(room);
                break;
            }
        }
    }

    private void room_disconnect() {
        Room room = user.getCurrentRoom();
        room.removeMember(user);
        user.removeRoom(room);
        Optional<Room> startRoom = roomRepo.findById((long) 1);
        startRoom.ifPresent(value -> user.setCurrentRoom(value));

        if (room.getMembers().size() == 0) {
            roomRepo.delete(room);
        }

        roomRepo.save(room);
        userRepo.save(user);
    }

    private void room_specified_disconnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        List<Room> rooms = roomRepo.findByName(roomName);
        for (Room room : rooms) {
            if (room.hasMember(user)) {
                if (user.getCurrentRoom().equals(room)) {
                    Optional<Room> startRoom = roomRepo.findById((long) 1);
                    startRoom.ifPresent(value -> user.setCurrentRoom(value));
                }
                room.removeMember(user);
                roomRepo.save(room);
                userRepo.save(user);
            }
        }
    }

    private void room_user_disconnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String userName = pair1.getFirst();

        List<Room> rooms = roomRepo.findByName(roomName);
        User usr = userRepo.findByUsername(userName);
        for (Room room : rooms) {
            if (room.hasMember(usr) && (user.isModerator() || user.isAdmin() || room.isCreator(user))) {
                room.removeMember(usr);
                roomRepo.save(room);
                userRepo.save(usr);
            }
        }
    }

    private void room_user_with_time_disconnect() {
    }

    private void user_rename() {
        Pair<String, Integer> pair = parse(0);
        String userName = pair.getFirst();

        Pair<String, Integer> pair1= parse(pair.getSecond());
        String newUserName = pair1.getFirst();

        User usr = userRepo.findByUsername(userName);
        User otherUsr = userRepo.findByUsername(newUserName);
        if (usr != null && otherUsr == null) {
            usr.setUsername(newUserName);
            userRepo.save(usr);
        }
    }

    private void user_ban() {
    }

    private void user_moderator(boolean make) {
        Pair<String, Integer> pair = parse(0);
        String userName = pair.getFirst();

        User usr = userRepo.findByUsername(userName);
        if (usr != null && user.isAdmin()) {
            if (make) {
                usr.getRoles().add(Role.MODERATOR);
            } else {
                usr.getRoles().add(Role.USER);
            }
            userRepo.save(usr);
        }
    }

    public String getCommand() {
        return this.command;
    }
}

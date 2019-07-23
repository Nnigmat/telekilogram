package nnigmat.telekilogram.controller;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.UserService;
import nnigmat.telekilogram.service.tos.RoomTO;
import nnigmat.telekilogram.service.tos.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommandExecutor {

    private RoomService roomService;
    private UserService userService;

    private String command;
    private String commandName;
    private UserTO user;

    public CommandExecutor(String command, String commandName, UserTO user) {
        this.command = command;
        this.commandName = commandName;
        this.user = user;
    }

    @Autowired
    public CommandExecutor(RoomService roomService, UserService userService) {
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
        Collection<RoomTO> rooms = roomService.findByName(roomName);
        if (rooms.isEmpty() && !this.user.isBanned()) {
            RoomTO newRoom = new RoomTO(roomName, this.user, closed);
            roomService.save(newRoom);
        }
    }

    private void roomRemove() {
        // Doesn't work properly
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Collection<RoomTO> rooms = roomService.findByName(roomName);
        for (RoomTO room : rooms) {
            if (room != null && room.getId() != 0 && (user.isAdmin() || room.isCreator(user))) {
                for (UserTO usr : room.getMembers()) {
                    room.removeMember(usr);
                }
                roomService.delete(room);
            }
        }
    }

    private void roomRename() {
        Pair<String, Integer> pair = parse(0);
        String newRoomName = pair.getFirst();

        RoomTO room = roomService.findById(user.getCurrentRoomId());
        if (room != null && (user.isAdmin() || room.isCreator(user))) {
            room.setName(newRoomName);
            roomService.save(room);
        }
    }

    private void roomSelfConnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Collection<RoomTO> rooms = roomService.findByName(roomName);
        for (RoomTO room : rooms) {
            if (room.hasMember(user)) {
                user.setCurrentRoomId(room.getId());
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

        UserTO usr = userService.findByUsername(userName);
        Collection<RoomTO> rooms = roomService.findByName(roomName);
        for (RoomTO room : rooms) {
            if (room.isCreator(user) || roomService.isAdmin(room, user) || roomService.isModerator(room, user)) {
                room.addMember(usr);
                userService.save(usr);
                break;
            }
        }
    }

    private void roomDisconnect() {
        RoomTO room = roomService.findById(user.getCurrentRoomId());
        room.removeMember(user);

        RoomTO startRoom = roomService.findById((long) 0);
        user.setCurrentRoomId(startRoom.getId());

        if (room.getMembers().size() == 0) {
            roomService.delete(room);
        }

        roomService.save(room);
        userService.save(user);
    }

    private void roomSpecifiedDisconnect() {
        Pair<String, Integer> pair = parse(0);
        String roomName = pair.getFirst();

        Collection<RoomTO> rooms = roomService.findByName(roomName);
        for (RoomTO room : rooms) {
            if (room.hasMember(user)) {
                if (user.getCurrentRoomId().equals(room.getId())) {
                    RoomTO startRoom = roomService.findById((long) 0);
                    user.setCurrentRoomId(startRoom.getId());
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

        Collection<RoomTO> rooms = roomService.findByName(roomName);
        UserTO usr = userService.findByUsername(userName);
        for (RoomTO room : rooms) {
            if (room.hasMember(usr) && (user.isModerator() || user.isAdmin() || room.isCreator(user))) {
                room.removeMember(usr);
                roomService.save(room);
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

        UserTO usr = userService.findByUsername(userName);
        UserTO otherUsr = userService.findByUsername(newUserName);
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

        UserTO usr = userService.findByUsername(userName);
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

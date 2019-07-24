package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.service.mappers.RoomMapper;
import nnigmat.telekilogram.service.mappers.UserMapper;
import nnigmat.telekilogram.service.tos.RoomTO;
import nnigmat.telekilogram.service.tos.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;

@Service
@Transactional
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    public Collection<RoomTO> findByName(String name) {
        if (name == null) {
            return null;
        }
        return roomMapper.tos(roomRepo.findByName(name));
    }

    public void save(RoomTO roomTO) {
        if (roomTO != null) {
            roomRepo.save(roomMapper.en(roomTO));
        }
    }

    public void delete(RoomTO roomTO) {
        if (roomTO != null) {
            roomRepo.delete(roomMapper.en(roomTO));
        }
    }

    public RoomTO findById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Room> room = roomRepo.findById(id);
        return room.map(value -> roomMapper.to(value)).orElse(null);
    }

    public Boolean isCreator(RoomTO roomTO, UserTO userTO) {
        if (roomTO == null || userTO == null) {
            return false;
        }
        return roomTO.getCreator().equals(userTO);
    }


    public Boolean isMember(RoomTO roomTO, UserTO userTO) {
        if (roomTO == null || userTO == null) {
            return false;
        }
        return roomTO.getMembers().contains(userTO);
    }

    public Boolean isModerator(RoomTO roomTO, UserTO userTO) {
        if (roomTO == null || userTO == null) {
            return false;
        }
        return roomTO.getModerators().contains(userTO);
    }

    public Boolean isAdmin(RoomTO roomTO, UserTO userTO) {
        if (roomTO == null || userTO == null) {
            return false;
        }
        return roomTO.getAdmins().contains(userTO);
    }


    public RoomTO getDefaultRoom() {
        Optional<Room> room = roomRepo.findById(0L);
        return room.map(value -> roomMapper.to(value)).orElse(null);
    }

    public Collection<UserTO> getConnectedMembersById(Long id) {
        if (id == null) {
            return null;
        }

        Optional<Room> room = roomRepo.findById(id);
        Collection<User> members = room.map(Room::getMembers).orElse(null);
        if (members == null) {
            return null;
        }

        Set<User> connectedMembers = new HashSet<>();
        for (User member : members) {
            if (member.getCurrentRoom().equals(room.get())) {
                connectedMembers.add(member);
            }
        }
        return userMapper.tos(connectedMembers);
    }
}

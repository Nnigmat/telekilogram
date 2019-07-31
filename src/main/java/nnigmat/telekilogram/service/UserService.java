package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.RoomRepo;
import nnigmat.telekilogram.repos.UserRepo;
import nnigmat.telekilogram.service.mappers.RoomMapper;
import nnigmat.telekilogram.service.mappers.UserMapper;
import nnigmat.telekilogram.service.tos.RoomTO;
import nnigmat.telekilogram.service.tos.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.to(userRepo.findByUsername(username));
    }

    public RoomTO getUserCurrentRoom(UserTO userTO) {
        if (userTO == null) {
            return null;
        }

        return roomRepo.findById(userTO.getCurrentRoomId()).map(roomMapper::to).orElse(null);
    }

    public void save(UserTO userTO) {
        if (userTO == null) {
            return;
        }
        User user = userMapper.en(userTO);
        Optional<Room> room = roomRepo.findById(user.getCurrentRoom().getId());
        room.ifPresent(user::setCurrentRoom);
        userRepo.save(user);
    }

    public UserTO findByUsername(String username) {
        if (username == null) {
            return null;
        }

        return userMapper.to(userRepo.findByUsername(username));
    }

    public Collection<UserTO> findAll() {
        return userMapper.tos(userRepo.findAll());
    }

    public void globalBan(UserTO userTO) {
        if (userTO == null || userTO.isBanned()) {
            return;
        }

        Set<Role> ban = new HashSet<Role>() {{ add(Role.BAN); }};
        userTO.setRoles(ban);
        save(userTO);
    }

    public void globalUnban(UserTO userTO) {
        if (userTO == null || !userTO.isBanned()) {
            return;
        }

        Set<Role> ban = new HashSet<Role>() {{ add(Role.USER); }};
        userTO.setRoles(ban);
        save(userTO);
    }

    public void addGlobalModerator(UserTO userTO) {
        if (userTO == null || userTO.isModerator()) {
            return;
        }

        Set<Role> moderator = new HashSet<Role>() {{ add(Role.MODERATOR); }};
        userTO.setRoles(moderator);
        save(userTO);
    }

    public void removeGlobalModerator(UserTO userTO) {
        if (userTO == null || !userTO.isModerator()) {
            return;
        }

        Set<Role> moderator = new HashSet<Role>() {{ add(Role.USER); }};
        userTO.setRoles(moderator);
        save(userTO);
    }

    public Collection<RoomTO> getUserRoomsById(Long id) {
        if (id == null) {
            return null;
        }

        Optional<User> user = userRepo.findById(id);
        return roomMapper.tos(user.map(User::getRooms).orElse(null));
    }
}

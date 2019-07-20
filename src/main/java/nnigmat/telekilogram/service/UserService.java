package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Role;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public Room getUserCurrentRoom(User user) {
        return user.getCurrentRoom();
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void globalBan(User user) {
        Set<Role> ban = new HashSet<Role>();
        if (user.isBanned()) {
            ban.add(Role.USER);
        } else {
            ban.add(Role.BAN);
        }
        user.setRoles(ban);
        save(user);
    }

    public void makeGlobalModerator(User user) {
        Set<Role> ban = new HashSet<Role>();
        if (user.isModerator()) {
            ban.add(Role.USER);
        } else {
            ban.add(Role.MODERATOR);
        }
        user.setRoles(ban);
        save(user);
    }
}

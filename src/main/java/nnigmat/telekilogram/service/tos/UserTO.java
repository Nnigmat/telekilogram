package nnigmat.telekilogram.service.tos;

import nnigmat.telekilogram.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class UserTO implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Long currentRoomId;
    private Set<Role> roles;
    private Set<Long> roomIds;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(Long currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Long> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(Set<Long> roomsId) {
        this.roomIds = roomsId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() { return this.getAuthorities().contains(Role.ADMIN); }

    public boolean isModerator() { return this.getAuthorities().contains(Role.MODERATOR); }

    public boolean isBanned() { return this.getAuthorities().contains(Role.BAN); }

    public boolean equals(UserTO user) {
        return this.getId().equals(user.getId());
    }
}

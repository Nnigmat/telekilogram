package nnigmat.telekilogram.service.tos;

import nnigmat.telekilogram.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
public class RoomTO {
    private Long id;
    private String name;
    private UserTO creator;
    private Set<UserTO> members;
    private Set<UserTO> moderators;
    private Set<UserTO> admins;
    private boolean closed;

    public RoomTO() {}

    public RoomTO(String name, UserTO creator, boolean closed) {
        this.name = name;
        this.creator = creator;
        this.members = new HashSet<UserTO>() {{ add(creator); }};
        this.closed = closed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserTO getCreator() {
        return creator;
    }

    public void setCreator(UserTO creator) {
        this.creator = creator;
    }

    public Set<UserTO> getMembers() {
        return members;
    }

    public void setMembers(Set<UserTO> members) {
        this.members = members;
    }

    public Set<UserTO> getModerators() {
        return moderators;
    }

    public void setModerators(Set<UserTO> moderators) {
        this.moderators = moderators;
    }

    public Set<UserTO> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<UserTO> admins) {
        this.admins = admins;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean hasMember(UserTO userTO) {
        if (userTO != null) {
            for (UserTO member : members) {
                if (member.equals(userTO)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCreator(UserTO userTO) {
        return userTO != null && creator.equals(userTO);
    }

    public void removeMember(UserTO userTO) {
        members.remove(userTO);
    }

    public void addMember(UserTO userTO) {
        members.add(userTO);
    }

    public boolean isAdmin(UserTO userTO) {
        return userTO != null && admins.contains(userTO);
    }

    public void addModerator(UserTO userTO) {
        moderators.add(userTO);
    }

    public void removeModerator(UserTO userTO) {
        moderators.remove(userTO);
    }

    public void addAdmin(UserTO userTO) {
        admins.add(userTO);
    }

    public void removeAdmin(UserTO userTO) {
        admins.remove(userTO);
    }
}

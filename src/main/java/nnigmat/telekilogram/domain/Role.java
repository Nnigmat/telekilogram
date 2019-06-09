package nnigmat.telekilogram.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, MODERATOR, BAN;

    @Override
    public String getAuthority() {
        return name();
    }
}

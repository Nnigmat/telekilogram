package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

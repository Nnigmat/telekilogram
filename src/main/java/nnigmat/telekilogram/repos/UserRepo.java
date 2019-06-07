package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}

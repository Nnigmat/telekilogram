package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}

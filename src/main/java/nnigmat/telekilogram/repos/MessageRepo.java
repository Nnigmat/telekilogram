package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Collection<Message> findByRoom(Room room);
}

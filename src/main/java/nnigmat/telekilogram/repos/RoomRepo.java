package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Long> {
    Collection<Room> findByName(String name);
}

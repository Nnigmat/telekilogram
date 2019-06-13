package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Long> {
    List<Room> findByName(String name);
}

package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room, Long> {
    Room findByName(String name);
}

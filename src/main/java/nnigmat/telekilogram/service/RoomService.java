package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;

    public List<Room> findByName(String name) {
        return roomRepo.findByName(name);
    }

    public void save(Room room) {
        if (room != null) {
            roomRepo.save(room);
        }
    }

    public void delete(Room room) {
        if (room != null) {
            roomRepo.delete(room);
        }
    }

    public Optional<Room> findById(Long id) {
        return roomRepo.findById(id);
    }
}

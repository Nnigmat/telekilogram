package nnigmat.telekilogram.service.mappers;

import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.service.tos.RoomTO;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RoomMapper {
    RoomTO to(Room room);
    Room en(RoomTO roomTO);

    Collection<RoomTO> tos(Collection<Room> rooms);
}

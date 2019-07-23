package nnigmat.telekilogram.service.mappers;

import nnigmat.telekilogram.domain.User;
import nnigmat.telekilogram.service.tos.UserTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface UserMapper {
    @Mappings({
            @Mapping(source = "currentRoom.id", target = "currentRoomId"),
            @Mapping(target = "roomIds", expression = "java(user.getRooms().stream().map(room -> new Long(room.getId())).collect(Collectors.toSet()))")
    })
    UserTO to(User user);
    @InheritInverseConfiguration
    User en(UserTO userTO);

    Collection<UserTO> tos(Collection<User> users);
}

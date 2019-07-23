package nnigmat.telekilogram.service.mappers;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.service.tos.MessageTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, UserMapper.class})
public interface MessageMapper {
    MessageTO to(Message message);
    Message en(MessageTO messageTO);

    Collection<MessageTO> tos(Collection<Message> messages);
}

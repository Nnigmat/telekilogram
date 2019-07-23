package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.repos.MessageRepo;
import nnigmat.telekilogram.service.mappers.MessageMapper;
import nnigmat.telekilogram.service.mappers.RoomMapper;
import nnigmat.telekilogram.service.tos.MessageTO;
import nnigmat.telekilogram.service.tos.RoomTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RoomMapper roomMapper;

    public Collection<MessageTO> findMessagesByRoom(RoomTO roomTO) {
        if (roomTO == null) {
            return null;
        }
        Collection<Message> messages = messageRepo.findByRoom(roomMapper.en(roomTO));
        return  messageMapper.tos(messages);
    }

    public void save(MessageTO messageTO) {
        if (messageTO == null) {
            return;
        }
        messageRepo.save(messageMapper.en(messageTO));
    }

    public MessageTO findMessageById(Long id) {
        if (id == null) {
            return null;
        }
        return messageRepo.findById(id).map(value -> messageMapper.to(value)).orElse(null);
    }

    public void delete(MessageTO messageTO) {
        if (messageTO == null) {
            return;
        }
        messageRepo.delete(messageMapper.en(messageTO));
    }

    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        MessageTO message = this.findMessageById(id);
        delete(message);
    }
}

package nnigmat.telekilogram.service;

import nnigmat.telekilogram.domain.Message;
import nnigmat.telekilogram.domain.Room;
import nnigmat.telekilogram.repos.MessageRepo;
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

    public Collection<Message> findMessagesByRoom(Room room) {
        return  messageRepo.findByRoom(room);
    }

    public void save(Message message) {
        messageRepo.save(message);
    }

    public Optional<Message> findMessageById(Long id) {
        return messageRepo.findById(id);
    }

    public void delete(Message message) {
        messageRepo.delete(message);
    }

    public void deleteById(Long id) {
        Optional<Message> message = this.findMessageById(id);
        message.ifPresent(this::delete);
    }
}

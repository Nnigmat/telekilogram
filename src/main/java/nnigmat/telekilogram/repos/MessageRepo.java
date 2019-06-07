package nnigmat.telekilogram.repos;

import nnigmat.telekilogram.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {

}

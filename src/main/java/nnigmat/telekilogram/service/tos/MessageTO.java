package nnigmat.telekilogram.service.tos;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MessageTO {
    private Long id;
    private UserTO author;
    private RoomTO room;
    private String text;
    private Date createdTime;

    public MessageTO(){}

    public MessageTO(UserTO author, RoomTO room, String text) {
        this.author = author;
        this.room = room;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserTO getAuthor() {
        return author;
    }

    public void setAuthor(UserTO author) {
        this.author = author;
    }

    public RoomTO getRoom() {
        return room;
    }

    public void setRoom(RoomTO room) {
        this.room = room;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public String getFormattedTime() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy HH:mm");

        return df.format(this.createdTime);
    }
}

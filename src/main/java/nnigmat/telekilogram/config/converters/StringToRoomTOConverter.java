package nnigmat.telekilogram.config.converters;

import nnigmat.telekilogram.service.RoomService;
import nnigmat.telekilogram.service.tos.RoomTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class StringToRoomTOConverter implements Converter<String, RoomTO> {

    @Autowired
    private RoomService roomService;

    public StringToRoomTOConverter() {}

    @Override
    public RoomTO convert(String roomId) {
        return roomService.findById(Long.valueOf(roomId));
    }
}

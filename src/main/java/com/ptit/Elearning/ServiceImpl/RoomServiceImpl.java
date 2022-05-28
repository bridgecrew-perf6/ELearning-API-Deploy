package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Room;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.RoomRepository;
import com.ptit.Elearning.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    RoomRepository roomRepository;


    @Override
    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room getById(int roomId) {
        return roomRepository.findByRoomId(roomId).orElseThrow(()->new NotFoundException("Could not find room with id: "+roomId));
    }
}

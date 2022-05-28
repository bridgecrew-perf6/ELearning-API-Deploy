package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Room;

import java.util.List;

public interface RoomService {
    public List<Room> getAllRoom();
    public Room getById(int roomId);
}

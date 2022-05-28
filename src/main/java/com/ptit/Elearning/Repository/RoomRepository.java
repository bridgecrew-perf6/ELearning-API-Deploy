package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    public Optional<Room> findByRoomId(int roomId);
}

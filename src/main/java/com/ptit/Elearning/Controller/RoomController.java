package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Entity.Room;
import com.ptit.Elearning.Service.RoomService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @ApiOperation("Lấy danh sách các phòng học #56")
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/get-all")
    public ResponseEntity<List<Room>> getAll(){
        return ResponseEntity.ok(roomService.getAllRoom());
    }
}

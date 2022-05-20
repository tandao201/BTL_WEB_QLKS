package hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hotel.model.Room;
import hotel.service.RoomService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/room", produces = "application/json")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Room createRoom(@RequestBody Room room) {
		return roomService.save(room);
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchRoom(@RequestParam(required = false, defaultValue = "") String keyword) {
		List<Room> rooms = roomService.searchRoom(keyword);
		return ResponseEntity.ok(rooms);
	}

	@GetMapping()
	public ResponseEntity<?> getAllRoom() {
		List<Room> rooms = roomService.getAllRoom();
		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable String id) {
		Long idUp = Long.parseLong(id);
		Room room = roomService.getRoomById(idUp);
		return ResponseEntity.ok(room);
	}

	@PutMapping()
	public ResponseEntity<?> updateUser(@RequestBody Room room) {
		Room roomByid = roomService.getRoomById(room.getId());
		roomByid = room;
		Room roomUpdate = roomService.updateRoom(roomByid);
		return ResponseEntity.ok(roomUpdate);
	}

	@DeleteMapping("/{id}")
	public void deleteRoom(@PathVariable("id") String id) {
		Long idUp = Long.valueOf(id);
		roomService.deleteRoom(idUp);
	}
}

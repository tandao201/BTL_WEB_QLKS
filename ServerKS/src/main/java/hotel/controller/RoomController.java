package hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import hotel.common.APIResponse;
import hotel.model.Room;
import hotel.model.dto.LockedRoomRequest;
import hotel.model.dto.RoomDto;
import hotel.model.dto.UpdateRoomDto;
import hotel.model.mapper.RoomMapper;
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

	@GetMapping("/suggest")
	public ResponseEntity<?> getRoomSuggest() {
		List<Room> rooms = roomService.getRoomSuggest();
		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getRoomById(@PathVariable String id) {
		Long idUp = Long.parseLong(id);
		Room room = roomService.getRoomById(idUp);
		return ResponseEntity.ok(room);
	}

	@PutMapping()
	public ResponseEntity<?> updateRoom(@RequestBody UpdateRoomDto room) {
		Room roomByid = roomService.getRoomById(room.getId());
		roomByid.setName(room.getName());
		roomByid.setPrice(room.getPrice());
		roomByid.setImage(room.getImage());
		roomByid.setQuantity(room.getQuantity().intValue());
		roomByid.setCategoryId(room.getCategoryId());
		roomByid.setCategory(room.getCategory());
		roomByid.setDescription(room.getDescription());
		Room roomUpdate = roomService.updateRoom(roomByid);
		return ResponseEntity.ok(roomUpdate);
	}

	@DeleteMapping("/{id}")
	public void deleteRoom(@PathVariable("id") String id) {
		Long idUp = Long.valueOf(id);
		roomService.deleteRoom(idUp);
	}

	@GetMapping("/roomPage")
	public ResponseEntity<?> getPageRoom(Pageable pageable) {

		return ResponseEntity.ok(roomService.getPageRoom(pageable));
	}

	@GetMapping("/roomPageAdmin")
	public ResponseEntity<?> getPageRoomAdmin(Pageable pageable) {

		return ResponseEntity.ok(roomService.getPageRoomAdmin(pageable));
	}

	@PutMapping("/locked")
	public ResponseEntity<?> lockedRoomByAdmin(@RequestBody LockedRoomRequest request) {
		APIResponse apiResponse = new APIResponse();
		Room room = roomService.getRoomById(Long.parseLong(request.getId()));

		if (room != null) {
			room.setLocked(request.isLocked());
			Room roomUpdate = roomService.updateRoom(room);
			RoomDto roomDto = RoomMapper.toRoomDto(roomUpdate);
			apiResponse.setData(roomDto);
		} else {
			apiResponse.setStatus(404);
		}

		return ResponseEntity.ok(apiResponse);
	}
}

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hotel.model.Room;
import hotel.model.RoomBooking;
import hotel.model.User;
import hotel.model.dto.RoomBookingRequestDto;
import hotel.service.RoomBookingService;
import hotel.service.RoomService;
import hotel.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/room-booking", produces = "application/json")
public class RoomBookingController {

	@Autowired
	private RoomBookingService roomBookingService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@GetMapping
	public ResponseEntity<?> getAllRoomBooking() {

		List<RoomBooking> roomBookings = roomBookingService.getAllRoomBooking();
		return ResponseEntity.ok(roomBookings);
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public RoomBooking createRoomBooking(@RequestBody RoomBookingRequestDto request) {
		RoomBooking roomBooking = new RoomBooking(request.getCheckInAt(), request.getCheckOutAt(),
				request.getTotalMoney(), false);
		User user = userService.getUserByUsername(request.getUsername());
		Room room = roomService.getRoomById(request.getIdRoom());

		roomBooking.setUsername(user.getUsername());
		roomBooking.setCategory(room.getCategory());
		roomBooking.setName(room.getName());
//		userService.updateUser(user);
		user.getRoomBookings().add(roomBooking);
		room.getRoomBookings().add(roomBooking);
		room.setQuantity(room.getQuantity() - 1);
//		roomService.updateRoom(room);
		return roomBookingService.save(roomBooking);
	}

	@PutMapping()
	public ResponseEntity<?> updateRoomBooking(@RequestBody RoomBooking roomBooking) {
		RoomBooking roomByid = roomBookingService.getRoomBookingById(roomBooking.getId());
		roomByid = roomBooking;
		roomByid.setStatus(true);
		RoomBooking roomUpdate = roomBookingService.updateRoom(roomByid);
		return ResponseEntity.ok(roomUpdate);
	}

	@PutMapping("/payment")
	public ResponseEntity<?> updateStatus(@RequestBody String id) {
		RoomBooking roomByid = roomBookingService.getRoomBookingById(Long.valueOf(id));
		roomByid.setStatus(true);
		RoomBooking roomUpdate = roomBookingService.updateRoom(roomByid);
		return ResponseEntity.ok(roomUpdate);
	}

	@DeleteMapping("/{id}")
	public void deleteRoom(@PathVariable("id") String id) {
		Long idUp = Long.valueOf(id);
		roomBookingService.deleteRoomking(idUp);
	}

	@GetMapping("/roomPage/{username}")
	public ResponseEntity<?> getPageRoom(Pageable pageable, @PathVariable(required = false) String username) {

		return ResponseEntity.ok(roomBookingService.getPageRoomBooked(pageable, username));
	}

	@GetMapping("/roomPageAdmin")
	public ResponseEntity<?> getPageRoomAmin(Pageable pageable) {

		return ResponseEntity.ok(roomBookingService.getPageRoomBookedAdmin(pageable));
	}

}

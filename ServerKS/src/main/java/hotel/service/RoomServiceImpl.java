package hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotel.model.Room;
import hotel.repository.RoomRepository;

@Component
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room save(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRoom() {
		return roomRepository.findAll();
	}

	@Override
	public Room getRoomById(Long id) {
		return roomRepository.findById(id).get();
	}

	@Override
	public Room getRoomByRoomname(String roomname) {
		Optional<Room> room = roomRepository.findByName(roomname);
		if (room.isPresent()) {
			return room.get();
		}
		return null;
	}

	@Override
	public List<Room> searchRoom(String keyword) {
		List<Room> rooms = roomRepository.findRoomsWithPartOfName(keyword);
		return rooms;
	}

	@Override
	public Room updateRoom(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public void deleteRoom(Long id) {
		roomRepository.deleteById(id);

	}

}

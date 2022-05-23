package hotel.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hotel.common.RoomData;
import hotel.model.Room;

@Service
public interface RoomService {
	public Room save(Room room);

	public List<Room> getAllRoom();

	public Room getRoomById(Long id);

	public Room getRoomByRoomname(String roomname);

	public List<Room> searchRoom(String keyword);

	public Room updateRoom(Room room);

	public void deleteRoom(Long id);

	public List<Room> getRoomSuggest();

	public RoomData getPageRoom(Pageable pageable);

}

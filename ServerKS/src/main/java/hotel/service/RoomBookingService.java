package hotel.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hotel.common.RoomBookedData;
import hotel.model.RoomBooking;

@Service
public interface RoomBookingService {
	public RoomBooking save(RoomBooking roomBooking);

	public List<RoomBooking> getAllRoomBooking();

	public RoomBooking getRoomBookingById(Long id);

	public List<RoomBooking> searchRoom(String keyword);

	public RoomBooking updateRoom(RoomBooking roomBooking);

	public void deleteRoomking(Long id);

	RoomBookedData getPageRoomBooked(Pageable pageable, String username);

	RoomBookedData getPageRoomBookedAdmin(Pageable pageable);
}

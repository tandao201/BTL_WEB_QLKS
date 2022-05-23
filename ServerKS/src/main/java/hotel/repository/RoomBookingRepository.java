package hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hotel.model.Room;
import hotel.model.RoomBooking;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
	@Query(value = "select * from room order by rand() limit 3", nativeQuery = true)
	List<Room> findRoomSuggest();

}

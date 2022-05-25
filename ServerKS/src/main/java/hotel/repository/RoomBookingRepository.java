package hotel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hotel.model.Room;
import hotel.model.RoomBooking;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
	@Query(value = "select * from room where locked=0 order by rand() limit 3", nativeQuery = true)
	List<Room> findRoomSuggest();

	@Query(value = "select sum(price) from room_booking where status = true", nativeQuery = true)
	Long countTurnover();

	@Query(value = "select count(id) from room_booking where status = true", nativeQuery = true)
	Long countRevervationDone();

	Page<RoomBooking> findAllByUsername(String username, Pageable pageable);
}

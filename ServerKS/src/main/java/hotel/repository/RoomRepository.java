package hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hotel.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findByName(String name);

	@Query(value = "SELECT * FROM ROOM WHERE name like CONCAT('%',:keyword,'%')", nativeQuery = true)
	List<Room> findRoomsWithPartOfName(String keyword);

	@Query(value = "select * from room a  inner join room_categories b on a.categori_id = b.id_cate ", nativeQuery = true)
	List<Room> findRoomsInnerJoinCate();

	@Query(value = "UPDATE room a " + "SET a.enabled = :#{#room.enabled} WHERE a.id = :#{#room.id}", nativeQuery = true)
	int updateEnabledById(@Param("room") Room room);

	@Query(value = "UPDATE room a " + "SET a.locked = :#{#room.locked} WHERE a.id = :#{#room.id}", nativeQuery = true)
	int updateLockedById(@Param("room") Room room);
}

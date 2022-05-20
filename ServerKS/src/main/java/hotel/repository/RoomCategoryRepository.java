package hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hotel.model.RoomCategories;

public interface RoomCategoryRepository extends JpaRepository<RoomCategories, Long> {

}

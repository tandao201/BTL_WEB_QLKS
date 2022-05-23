package hotel.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hotel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "SELECT * FROM USER WHERE name like CONCAT('%',:keyword,'%')", nativeQuery = true)
	List<User> findUsersWithPartOfName(String keyword);

	@Query(value = "SELECT * FROM USER WHERE username like CONCAT(:username) and  enabled=1 and role like CONCAT('%',:role,'%') ", nativeQuery = true)
	Optional<User> findOneByUsernameIgnoreCaseEnabled(String username, String role);

	Optional<User> findByUsername(String username);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user a " + "SET a.enabled = TRUE WHERE a.username = ?1", nativeQuery = true)
	int enableUser(String username);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user a " + "SET a.actived = TRUE WHERE a.username = ?1", nativeQuery = true)
	int activeUser(String username);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user a " + "SET a.actived = FALSE WHERE a.username = ?1", nativeQuery = true)
	int inactiveUser(String username);

	Optional<User> findByActivedAndUsername(Boolean actived, String username);

	@Query(value = "SELECT * FROM USER WHERE actived=1 and role like CONCAT(:role) ", nativeQuery = true)
	Optional<User> findByActivedAndRole(String role);

}

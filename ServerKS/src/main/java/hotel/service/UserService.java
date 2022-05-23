package hotel.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hotel.common.UserData;
import hotel.model.User;
import hotel.model.dto.UserDto;

@Service
public interface UserService {
	public User save(User user);

	public List<UserDto> getAllUser();

	public UserDto getUserById(Long id);

	public User getUserByUsername(String username);

	public List<UserDto> searchUser(String keyword);

	public User updateUser(User user);

	public void deleteUser(Long id);

	UserData getPageUser(Pageable pageable);

	User findUserById(Long id);
}

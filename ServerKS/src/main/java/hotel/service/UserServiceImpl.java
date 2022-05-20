package hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotel.model.User;
import hotel.model.dto.UserDto;
import hotel.model.mapper.UserMapper;
import hotel.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserDto> getAllUser() {
		List<User> users = userRepository.findAll();
		List<UserDto> rs = new ArrayList<>();
		for (User user : users) {
			rs.add(UserMapper.toUserDto(user));
		}
		return rs;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDto getUserById(Long id) {
		return UserMapper.toUserDto(userRepository.getById(id));
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findUsersWithPartOfName(keyword);
		List<UserDto> rs = new ArrayList<>();
		for (User user : users) {
			rs.add(UserMapper.toUserDto(user));
		}
		return rs;
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User getUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent())
			return user.get();
		return null;
	}

}

package hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import hotel.common.PaginationMeta;
import hotel.common.UserData;
import hotel.model.User;
import hotel.model.dto.PaginationMetaDto;
import hotel.model.dto.UserDto;
import hotel.model.mapper.PaginationMetaMapper;
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
	public User findUserById(Long id) {
		return userRepository.getById(id);
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

	@Override
	public UserData getPageUser(Pageable pageable) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Sort.Direction.DESC, "locked"));
		orders.add(new Order(Sort.Direction.ASC, "username"));
		Sort sort = Sort.by(orders).ascending();
		Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<User> page = userRepository.findAll(pageable2);
		List<User> users = page.getContent();
		List<UserDto> userDto = new ArrayList<>();
		for (User user : users) {
			userDto.add(UserMapper.toUserDto(user));
		}

		PaginationMeta userPagination = PaginationMeta.createPagination(page);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(userPagination);
		UserData userData = new UserData(userDto, dto);
		return userData;
	}

}

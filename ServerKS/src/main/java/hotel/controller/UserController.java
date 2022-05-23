package hotel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hotel.common.APIResponse;
import hotel.model.User;
import hotel.model.dto.LockedUserRequest;
import hotel.model.dto.PasswordChangeDto;
import hotel.model.dto.UpdateRequestDto;
import hotel.model.dto.UserDto;
import hotel.model.mapper.UserMapper;
import hotel.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {
	@Autowired
	private UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userService.save(user);
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchUser(@RequestParam(required = false, defaultValue = "") String keyword) {
		List<UserDto> user = userService.searchUser(keyword);
		return ResponseEntity.ok(user);
	}

	@GetMapping()
	public ResponseEntity<?> getAllUser() {
		List<UserDto> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		UserDto userDto = userService.getUserById(id);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping()
	public ResponseEntity<?> updateUser(@RequestBody UpdateRequestDto updateRequestDto) {
		APIResponse apiResponse = new APIResponse();
		User user = userService.getUserByUsername(updateRequestDto.getUsername());

		if (user != null) {
			user.setEmail(updateRequestDto.getEmail());
			user.setPhone(updateRequestDto.getPhone());
			user.setAvatar(updateRequestDto.getAvatar());
			user.setName(updateRequestDto.getName());
			User userUpdate = userService.updateUser(user);
			UserDto userDto = UserMapper.toUserDto(userUpdate);
			apiResponse.setData(userDto);
		} else {
			apiResponse.setStatus(404);
		}

		return ResponseEntity.ok(apiResponse);
	}

	@PutMapping("/admin")
	public ResponseEntity<?> updateUserByAdmin(@RequestBody UpdateRequestDto updateRequestDto) {
		APIResponse apiResponse = new APIResponse();
		User user = userService.getUserByUsername(updateRequestDto.getUsername());

		if (user != null) {
			user.setEmail(updateRequestDto.getEmail());
			user.setPhone(updateRequestDto.getPhone());
			user.setRole(updateRequestDto.getRole());
			user.setName(updateRequestDto.getName());
			User userUpdate = userService.updateUser(user);
			UserDto userDto = UserMapper.toUserDto(userUpdate);
			apiResponse.setData(userDto);
		} else {
			apiResponse.setStatus(404);
		}

		return ResponseEntity.ok(apiResponse);
	}

	@PutMapping("/admin/locked")
	public ResponseEntity<?> lockedUserByAdmin(@RequestBody LockedUserRequest request) {
		APIResponse apiResponse = new APIResponse();
		User user = userService.findUserById(Long.parseLong(request.getId()));

		if (user != null) {
			user.setLocked(request.isLocked());
			User userUpdate = userService.updateUser(user);
			UserDto userDto = UserMapper.toUserDto(userUpdate);
			apiResponse.setData(userDto);
		} else {
			apiResponse.setStatus(404);
		}

		return ResponseEntity.ok(apiResponse);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable("id") String id) {
		Long idDelete = Long.valueOf(id);
		userService.deleteUser(idDelete);
	}

	@PostMapping("/password")
	public ResponseEntity<?> updatePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
		APIResponse apiResponse = new APIResponse();

		User user = userService.getUserByUsername(passwordChangeDto.getUsername());
		if (!bCryptPasswordEncoder.matches(passwordChangeDto.getPasswordCur(), user.getPassword())) {
			apiResponse.setStatus(404);
			Map<String, String> data = new HashMap<>();
			data.put("phone", user.getPhone());
			data.put("email", user.getEmail());
			data.put("username", user.getUsername());
			data.put("avatar", user.getAvatar());
			data.put("name", user.getName());
			data.put("error", "Nhập mật khẩu không đúng");
			apiResponse.setData(data);
		} else {
			if (user != null) {
				String encodedPassword = bCryptPasswordEncoder.encode(passwordChangeDto.getPasswordNew());
				user.setPassword(encodedPassword);
				User userUpdate = userService.updateUser(user);
				UserDto userDto = UserMapper.toUserDto(userUpdate);
				apiResponse.setData(userDto);
			} else {
				Map<String, String> data = new HashMap<>();
				data.put("error", "Không tồn tại user");
				apiResponse.setStatus(404);
			}
		}

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/userPage")
	public ResponseEntity<?> getPageRoom(Pageable pageable) {

		return ResponseEntity.ok(userService.getPageUser(pageable));
	}

}

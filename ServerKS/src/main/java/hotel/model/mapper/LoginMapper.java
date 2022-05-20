package hotel.model.mapper;

import hotel.Constant.Role;
import hotel.model.User;
import hotel.model.dto.SignUpRequestDto;

public class LoginMapper {
	public static User signUpDtoToUser(SignUpRequestDto signUpRequestDto) {
		User user = new User();
		user.setUsername(signUpRequestDto.getUsername());
		user.setPassword(signUpRequestDto.getPassword());
		user.setEmail(signUpRequestDto.getEmail());
		user.setAvatar("");
		user.setName("customer");
		user.setEnabled(false);
		user.setLocked(false);
		user.setRole(String.valueOf(Role.USER));
		return user;
	}
}

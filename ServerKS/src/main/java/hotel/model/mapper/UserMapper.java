package hotel.model.mapper;

import hotel.model.User;
import hotel.model.dto.UserDto;

public class UserMapper {
	public static UserDto toUserDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setUsername(user.getUsername());
		dto.setAvatar(user.getAvatar());
		dto.setRole(user.getRole());
		dto.setActived(user.getActived());
		dto.setLocked(false);
		boolean check = false;
		if (user.getEnabled() != null) {
			check = user.getEnabled();
		}
		dto.setEnabled(check);
		dto.setPhone(user.getPhone());
		return dto;
	}

}

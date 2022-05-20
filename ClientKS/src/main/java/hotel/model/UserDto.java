package hotel.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	private String id;
	private String name;
	private String email;
	private String username;
	private boolean enabled;
	private boolean locked;
	private String avatar;
	private String role;
	private String phone;
	private Boolean actived;

}
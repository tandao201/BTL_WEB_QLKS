package hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
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

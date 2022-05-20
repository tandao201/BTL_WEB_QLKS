package hotel.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
	private String username;
	private String password;
	private String email;
}

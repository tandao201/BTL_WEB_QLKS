package hotel.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeDto {
	public String passwordCur;
	public String passwordNew;
	public String username;
}

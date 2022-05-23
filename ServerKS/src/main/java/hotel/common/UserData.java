package hotel.common;

import java.util.List;

import hotel.model.dto.PaginationMetaDto;
import hotel.model.dto.UserDto;
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
public class UserData {
	private List<UserDto> users;
	private PaginationMetaDto paginationMeta;
}

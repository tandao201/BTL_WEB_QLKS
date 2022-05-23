package hotel.common;

import java.util.List;

import hotel.model.Room;
import hotel.model.dto.PaginationMetaDto;
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
public class RoomData {

	private List<Room> rooms;
	private PaginationMetaDto paginationMeta;
}

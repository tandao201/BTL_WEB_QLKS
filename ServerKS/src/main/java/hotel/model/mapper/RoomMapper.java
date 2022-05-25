package hotel.model.mapper;

import hotel.model.Room;
import hotel.model.dto.RoomDto;

public class RoomMapper {
	public static RoomDto toRoomDto(Room room) {
		RoomDto dto = new RoomDto();
		dto.setId(room.getId());
		dto.setName(room.getName());
		dto.setPrice(room.getPrice());
		dto.setDescription(room.getDescription());
		dto.setImage(room.getImage());
		dto.setQuantity(room.getQuantity());
		dto.setCategoriId(room.getCategoryId());
		dto.setNameCate(room.getCategory());
		boolean checkLock = false;
		if (room.getEnabled() != null) {
			checkLock = room.getLocked();
		}
		dto.setLocked(checkLock);
		boolean check = false;
		if (room.getEnabled() != null) {
			check = room.getEnabled();
		}
		dto.setEnabled(check);
		return dto;
	}

}
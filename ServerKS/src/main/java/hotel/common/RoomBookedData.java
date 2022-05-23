package hotel.common;

import java.util.List;

import hotel.model.RoomBooking;
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
public class RoomBookedData {
	private List<RoomBooking> rooms;
	private PaginationMetaDto paginationMeta;
}

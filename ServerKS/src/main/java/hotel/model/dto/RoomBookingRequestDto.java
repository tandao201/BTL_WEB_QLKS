package hotel.model.dto;

import java.time.LocalDateTime;

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
public class RoomBookingRequestDto {
	private Long id;
	private LocalDateTime checkInAt;
	private LocalDateTime checkOutAt;
	private Long totalMoney;
	private boolean status;
	private Long idRoom;
	private String username;

	public RoomBookingRequestDto(LocalDateTime checkInAt, LocalDateTime checkOutAt, Long totalMoney, boolean status,
			Long idRoom, String username) {
		super();
		this.checkInAt = checkInAt;
		this.checkOutAt = checkOutAt;
		this.totalMoney = totalMoney;
		this.status = status;
		this.idRoom = idRoom;
		this.username = username;
	}

}

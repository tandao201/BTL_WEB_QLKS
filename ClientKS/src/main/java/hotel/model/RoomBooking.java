package hotel.model;

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
public class RoomBooking {
	private Long id;
	private LocalDateTime checkInAt;
	private LocalDateTime checkOutAt;
	private Long totalMoney;
	private boolean status;
}

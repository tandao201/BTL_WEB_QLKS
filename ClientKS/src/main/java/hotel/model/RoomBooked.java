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
public class RoomBooked {
	private Long id;
	private String name;
	private String category;
	private LocalDateTime checkInAt;
	private LocalDateTime checkOutAt;
	private Long price;
	private boolean status;
}

package hotel.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "room_booking")
public class RoomBooking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String name;
	private String category;
	private LocalDateTime checkInAt;
	private LocalDateTime checkOutAt;
	private Long price;
	private boolean status;

	public RoomBooking(LocalDateTime checkInAt, LocalDateTime checkOutAt, Long price, boolean status) {
		super();
		this.checkInAt = checkInAt;
		this.checkOutAt = checkOutAt;
		this.price = price;
		this.status = status;
	}

}

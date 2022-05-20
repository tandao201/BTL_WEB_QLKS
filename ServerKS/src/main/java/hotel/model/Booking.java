package hotel.model;

import java.time.LocalDateTime;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
public class Booking {
	@OneToOne
	private String idRoom;
	@ManyToOne
	private String idUser;
	private LocalDateTime checkInAt;
	private LocalDateTime checkOutAt;

}

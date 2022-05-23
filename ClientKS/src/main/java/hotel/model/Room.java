package hotel.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Room {
	private int id;
	private String name;
	private int price;
	private String description;
	private String image;
	private Boolean locked;
	private Boolean enabled;
	private int quantity;

	private int categoryId;
	private String category;
	List<RoomBookingRequestDto> roomBookings = new ArrayList<>();

	public Room(String name, int price, String description, String image, Boolean locked, Boolean enabled, int quantity,
			int categoryId, String category, List<RoomBookingRequestDto> roomBookings) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.image = image;
		this.locked = locked;
		this.enabled = enabled;
		this.quantity = quantity;
		this.categoryId = categoryId;
		this.category = category;
		this.roomBookings = roomBookings;
	}

}

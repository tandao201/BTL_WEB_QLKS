package hotel.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Room {
	private Long id;
	private String name;
	private Long price;
	private String description;
	private String image;
	private Boolean locked;
	private Boolean enabled;
	private int quantity;

	private Long categoryId;
	private String category;

	public Room(String name, Long price, String description, String image, Boolean locked, Boolean enabled,
			int quantity, Long categoryId, String category) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.image = image;
		this.locked = locked;
		this.enabled = enabled;
		this.quantity = quantity;
		this.category = category;
		this.categoryId = categoryId;
	}

}

package hotel.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	private Long price;
	private String description;
	private String image;
	private Boolean locked;
	private Boolean enabled;
	private int quantity;

	private Long categoryId;
	private String category;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "rb_id", referencedColumnName = "id")
	List<RoomBooking> roomBookings = new ArrayList<>();

	public Room(String name, Long price, String description, String image, Boolean locked, Boolean enabled,
			int quantity, String category, Long categoryId) {
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

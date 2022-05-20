package hotel.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class RoomCategories {
	private Long idCate;
	private String nameCate;

	public RoomCategories(String nameCate) {
		super();
		this.nameCate = nameCate;
	}

}

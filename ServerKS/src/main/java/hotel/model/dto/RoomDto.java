package hotel.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class RoomDto {
	private Long id;
	private String name;
	private Long price;
	private String description;
	private String image;
	private Boolean locked;
	private Boolean enabled;
	private int quantity;
	private Long categoriId;
	private Long idCate;
	private String nameCate;
}

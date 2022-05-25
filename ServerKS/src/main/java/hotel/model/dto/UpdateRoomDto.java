package hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomDto {
	private Long id;
	private String name;
	private Long price;
	private String image;
	private Long quantity;
	private Long categoryId;
	private String category;
	private String description;
}
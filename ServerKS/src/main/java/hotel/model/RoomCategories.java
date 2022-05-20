package hotel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "room_categories")
public class RoomCategories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCate;
	private String nameCate;

}

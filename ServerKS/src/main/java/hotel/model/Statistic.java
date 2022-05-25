package hotel.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Statistic {
	private Long turnover;
	private Long totalRevervation;
	private Long totalAccount;
}

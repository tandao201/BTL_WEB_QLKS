package hotel.common;

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
public class PaginationMeta {
	private Integer totalCount;
	private Integer pageSize;
	private Integer totalPage;
	private Integer pageNumber;
	private Integer isLast;
	private Integer isFirst;
}

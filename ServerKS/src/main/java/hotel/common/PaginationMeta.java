package hotel.common;

import org.springframework.data.domain.Page;

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
	private long totalCount;
	private Integer pageSize;
	private Integer totalPage;
	private Integer pageNumber;
	private Boolean isLast;
	private Boolean isFirst;

	public static <T> PaginationMeta createPagination(Page<T> page) {
		PaginationMeta meta = new PaginationMeta();
		meta.setIsFirst(page.isFirst());
		meta.setIsLast(page.isLast());
		meta.setPageNumber(page.getNumber() + 1);
		meta.setPageSize(page.getSize());
		meta.setTotalCount(page.getTotalElements());
		meta.setTotalPage(page.getTotalPages());
		return meta;
	}
}

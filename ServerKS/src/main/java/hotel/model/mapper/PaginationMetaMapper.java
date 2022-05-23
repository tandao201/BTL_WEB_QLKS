package hotel.model.mapper;

import hotel.common.PaginationMeta;
import hotel.model.dto.PaginationMetaDto;

public class PaginationMetaMapper {
	public static PaginationMetaDto toPaginationDto(PaginationMeta paginationMeta) {
		PaginationMetaDto dto = new PaginationMetaDto();
		Long tmp = paginationMeta.getTotalCount();
		dto.setTotalCount(tmp.intValue());
		dto.setPageSize(paginationMeta.getPageSize());
		dto.setTotalPage(paginationMeta.getTotalPage());
		dto.setPageNumber(paginationMeta.getPageNumber());
		int first = paginationMeta.getIsFirst() ? 1 : 0;
		int last = paginationMeta.getIsLast() ? 1 : 0;
		dto.setIsFirst(first);
		dto.setIsLast(last);
		return dto;
	}
}

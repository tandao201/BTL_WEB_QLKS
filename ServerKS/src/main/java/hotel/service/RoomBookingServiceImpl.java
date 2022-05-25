package hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import hotel.common.PaginationMeta;
import hotel.common.RoomBookedData;
import hotel.model.RoomBooking;
import hotel.model.dto.PaginationMetaDto;
import hotel.model.mapper.PaginationMetaMapper;
import hotel.repository.RoomBookingRepository;

@Component
public class RoomBookingServiceImpl implements RoomBookingService {

	@Autowired
	RoomBookingRepository bookingRepository;

	@Override
	public RoomBooking save(RoomBooking roomBooking) {

		return bookingRepository.save(roomBooking);
	}

	@Override
	public List<RoomBooking> getAllRoomBooking() {
		return bookingRepository.findAll();
	}

	@Override
	public RoomBooking getRoomBookingById(Long id) {

		return bookingRepository.getById(id);
	}

	@Override
	public List<RoomBooking> searchRoom(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomBooking updateRoom(RoomBooking roomBooking) {
		return bookingRepository.save(roomBooking);
	}

	@Override
	public void deleteRoomking(Long id) {
		bookingRepository.deleteById(id);

	}

	@Override
	public RoomBookedData getPageRoomBooked(Pageable pageable, String username) {
		Sort sort = Sort.by("status").ascending();
		Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<RoomBooking> roomPage = bookingRepository.findAllByUsername(username, pageable2);
		List<RoomBooking> rooms = roomPage.getContent();
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomBookedData roomData = new RoomBookedData(rooms, dto);
		return roomData;
	}

	@Override
	public RoomBookedData getPageRoomBookedAdmin(Pageable pageable) {
		Sort sort = Sort.by("status").ascending();
		Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<RoomBooking> roomPage = bookingRepository.findAll(pageable2);
		List<RoomBooking> rooms = roomPage.getContent();
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomBookedData roomData = new RoomBookedData(rooms, dto);
		return roomData;
	}

}

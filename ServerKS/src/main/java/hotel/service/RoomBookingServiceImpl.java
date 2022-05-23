package hotel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

		Page<RoomBooking> roomPage = bookingRepository.findAll(pageable);
		List<RoomBooking> rooms = roomPage.getContent();
		List<RoomBooking> main = new ArrayList<>();
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getUsername().equalsIgnoreCase(username))
				main.add(rooms.get(i));
		}
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomBookedData roomData = new RoomBookedData(main, dto);
		return roomData;
	}

	@Override
	public RoomBookedData getPageRoomBookedAdmin(Pageable pageable) {
		Page<RoomBooking> roomPage = bookingRepository.findAll(pageable);
		List<RoomBooking> rooms = roomPage.getContent();
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomBookedData roomData = new RoomBookedData(rooms, dto);
		return roomData;
	}

}

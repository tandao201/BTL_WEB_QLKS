package hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import hotel.common.PaginationMeta;
import hotel.common.RoomData;
import hotel.model.Room;
import hotel.model.dto.PaginationMetaDto;
import hotel.model.mapper.PaginationMetaMapper;
import hotel.repository.RoomRepository;

@Component
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room save(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRoom() {
		return roomRepository.findAll();
	}

	@Override
	public Room getRoomById(Long id) {
		return roomRepository.findById(id).get();
	}

	@Override
	public Room getRoomByRoomname(String roomname) {
		Optional<Room> room = roomRepository.findByName(roomname);
		if (room.isPresent()) {
			return room.get();
		}
		return null;
	}

	@Override
	public List<Room> searchRoom(String keyword) {
		List<Room> rooms = roomRepository.findRoomsWithPartOfName(keyword);
		return rooms;
	}

	@Override
	public Room updateRoom(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public void deleteRoom(Long id) {
		roomRepository.deleteById(id);

	}

	@Override
	public List<Room> getRoomSuggest() {
		return roomRepository.findRoomSuggest();
	}

	@Override
	public RoomData getPageRoom(Pageable pageable) {
		Sort sort = Sort.by("locked").descending();
		Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<Room> roomPage = roomRepository.findByLocked(pageable2, false);
		List<Room> rooms = roomPage.getContent();
		List<Room> main = new ArrayList<>();
		for (Room room : rooms) {
			if (room.getQuantity() >= 1 && room.getLocked() == false) {
				main.add(room);
			}
		}
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomData roomData = new RoomData(main, dto);
		return roomData;
	}

	@Override
	public RoomData getPageRoomAdmin(Pageable pageable) {
		Sort sort = Sort.by("price").descending();
		Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<Room> roomPage = roomRepository.findAll(pageable2);
		List<Room> rooms = roomPage.getContent();
		PaginationMeta roomPagination = PaginationMeta.createPagination(roomPage);
		PaginationMetaDto dto = PaginationMetaMapper.toPaginationDto(roomPagination);
		RoomData roomData = new RoomData(rooms, dto);
		return roomData;
	}

	@Override
	public Optional<Room> findRoomByNameOptional(String name) {
		return roomRepository.findByName(name);
	}

}
